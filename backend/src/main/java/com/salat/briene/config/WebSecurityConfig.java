package com.salat.briene.config;

import com.salat.briene.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig implements WebMvcConfigurer {
    private final UserService userService;
    private final AuthEntryPointJwt unauthorizedHandler;
    private final AuthTokenFilter authTokenFilter;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .httpBasic()
                .and()
                    .authorizeRequests()
                    .antMatchers("/api/articles/**",
                        "/api/auth/**",
                        "/api/authors/**",
                        "/api/avatars/**",
                        "/api/bookmarks/**",
                        "/api/comments/**",
                        "/api/search/**",
                        "/api/tags/**").permitAll()
                    .antMatchers("/api/users/**").authenticated()
                    .antMatchers("/api/admin/**").hasRole("ADMIN")
                .and()
                    .csrf().disable()
                    .exceptionHandling().authenticationEntryPoint(unauthorizedHandler);

        httpSecurity.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}