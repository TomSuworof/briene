package com.salat.briene.config;

import com.salat.briene.entities.User;
import com.salat.briene.exceptions.BlockedUserException;
import com.salat.briene.services.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    private static final String CANNOT_AUTH_USER = "Cannot set user authentication: {}";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";

    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUsernameFromJwtToken(jwt);

                User user = userService.loadUserByUsername(username);

                if (!user.isAccountNonLocked()) {
                    throw new BlockedUserException(username);
                }

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error(CANNOT_AUTH_USER, e);
        }

        chain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(AUTHORIZATION_HEADER_PREFIX)) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
