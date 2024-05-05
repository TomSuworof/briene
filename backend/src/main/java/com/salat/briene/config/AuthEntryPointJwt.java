// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.salat.briene.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    private static final String UNAUTHORIZED_ERROR = "Error: Unauthorized.";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.error(authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, UNAUTHORIZED_ERROR);
    }
}