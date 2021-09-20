package com.salat.briene.controllers;

import com.salat.briene.entities.RoleEnum;
import com.salat.briene.entities.User;
import com.salat.briene.payload.request.UserDataRequest;
import com.salat.briene.exceptions.UserNotFoundException;
import com.salat.briene.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {
    private static final String USER_UPDATED = "User data of {%s} was changed";

    private final UserService userService;

    @PostMapping("/edit")
    public ResponseEntity<String> editUser(
            @RequestParam UUID id,
            @RequestParam String password,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String bio,
            @RequestParam(required = false) String passwordNew,
            Authentication authentication) {
        User currentUser = userService.getUserFromAuthentication(authentication);

        if (currentUser.is(RoleEnum.ADMIN) || (currentUser.getId().equals(id) && userService.isCurrentPasswordSameAs(id, password))) {

            UserDataRequest newUserData = new UserDataRequest();
            newUserData.setEmail(Optional.ofNullable(email));
            newUserData.setBio(Optional.ofNullable(bio));
            newUserData.setPassword(Optional.ofNullable(passwordNew));

            userService.updateUser(id, newUserData);

            return ResponseEntity.ok().body(USER_UPDATED.formatted(id));
        } else {
            throw new UserNotFoundException();
        }
    }
}
