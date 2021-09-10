package com.salat.briene.controllers;

import com.salat.briene.entities.User;
import com.salat.briene.exceptions.UserNotFoundException;
import com.salat.briene.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {
    private final UserService userService;

    @PostMapping("/edit")
    public ResponseEntity<?> editUser(
            @RequestParam Long id,
            @RequestParam String password,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String passwordNew,
            Authentication authentication) throws UserNotFoundException {
        User authUser = userService.getUserFromAuthentication(authentication);

        if (authUser.is("admin") || (authUser.getId().equals(id) && userService.isCurrentPasswordSameAs(id, password))) {
            userService.updateUser(id, new HashMap<>() {{
                put("email", email);
                put("password", passwordNew);
            }});
            return ResponseEntity.ok().body("User data of " + id + " was changed");
        } else {
            throw new UserNotFoundException();
        }
    }
}
