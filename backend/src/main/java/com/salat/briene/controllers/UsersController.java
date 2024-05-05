// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.salat.briene.controllers;

import com.salat.briene.entities.RoleEnum;
import com.salat.briene.entities.User;
import com.salat.briene.exceptions.UserNotFoundException;
import com.salat.briene.payload.request.UserDataRequest;
import com.salat.briene.payload.response.UserDTO;
import com.salat.briene.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@Log4j2
@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {
    private final UserService userService;

    @PostMapping("/edit")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<UserDTO> editUser(
            @RequestParam UUID id,
            @RequestParam String password,
            @RequestBody UserDataRequest newUserData,
            Authentication authentication
    ) {
        log.debug("editUser() called. ID: {}. User info cannot be displayed", id);
        User currentUser = userService.getUserFromAuthentication(authentication);

        if (currentUser.is(RoleEnum.ADMIN) || (currentUser.getId().equals(id) && userService.isCurrentPasswordSameAs(id, password))) {
            UserDTO user = userService.updateUser(id, newUserData);
            return ResponseEntity.ok().body(user);
        } else {
            throw new UserNotFoundException();
        }
    }
}
