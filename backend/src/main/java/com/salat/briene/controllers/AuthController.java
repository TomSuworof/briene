// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.salat.briene.controllers;

import com.salat.briene.config.JwtUtils;
import com.salat.briene.entities.User;
import com.salat.briene.payload.request.LoginRequest;
import com.salat.briene.payload.request.SignupRequest;
import com.salat.briene.payload.response.JwtResponse;
import com.salat.briene.payload.response.UserDTO;
import com.salat.briene.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        log.debug("authenticateUser() called. Login request cannot be displayed");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User user = (User) authentication.getPrincipal();

        Set<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return ResponseEntity.ok().body(new JwtResponse(
                jwt,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<UserDTO> registerUser(@RequestBody SignupRequest signUpRequest) {
        log.debug("registerUser() called. User info cannot be displayed");
        UserDTO user = userService.saveUser(signUpRequest);
        return ResponseEntity.ok().body(user);
    }
}
