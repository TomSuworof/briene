package com.salat.briene.config;

import lombok.NonNull;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

@Configuration
public class SpaRedirectFilterConfiguration {

    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> spaRedirectFiler() {
        FilterRegistrationBean<OncePerRequestFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(createRedirectFilter());
        registration.addUrlPatterns("/*");
        registration.setName("frontendRedirectFilter");
        registration.setOrder(1);
        return registration;
    }

    private OncePerRequestFilter createRedirectFilter() {
        return new OncePerRequestFilter() {

            // Forwards all routes except '/index.html', '/200.html', '/favicon.ico', '/robots.txt', '/sw.js' '/api/', '/api/**'
            private final String REGEX = "(?!/actuator|/api|/_nuxt|/static|/index\\.html|/200\\.html|/favicon\\.ico|/robots\\.txt|sw\\.js).*$";
            private final Pattern pattern = Pattern.compile(REGEX);

            @Override
            protected void doFilterInternal(@NonNull HttpServletRequest req,
                                            @NonNull HttpServletResponse res,
                                            @NonNull FilterChain chain) throws ServletException, IOException {
                if (pattern.matcher(req.getRequestURI()).matches() && !req.getRequestURI().equals("/")) {
                    // Delegate/Forward to `/` if `pattern` matches, and it is not `/`
                    // Required because of 'mode: history's usage in frontend routing
                    RequestDispatcher rd = req.getRequestDispatcher("/");
                    rd.forward(req, res);
                } else {
                    chain.doFilter(req, res);
                }
            }
        };
    }
}