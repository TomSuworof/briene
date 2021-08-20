package com.salat.briene.api;

import com.salat.briene.entities.User;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class ApiUserController {

    @GetMapping("/current")
    public User getCurrentUser(
            KeycloakPrincipal<KeycloakSecurityContext> principal
    ) {
        User user = new User();
        user.setUsername(principal.getKeycloakSecurityContext().getToken().getPreferredUsername());
        return user;
    }
}