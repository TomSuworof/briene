// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.salat.briene.controllers;

import com.salat.briene.entities.User;
import com.salat.briene.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.beans.Transient;

@Log4j2
@Controller
@RequestMapping("/api/avatars")
@RequiredArgsConstructor
public class AvatarController {
    private final UserService userService;

    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    @Transient
    public @ResponseBody ResponseEntity<String> getUserAvatar(@PathVariable String username) {
        log.debug("getUserAvatar() called. Username: {}", username);
        User user = userService.loadUserByUsername(username);
        log.trace("getUserAvatar(). User: {}", () -> user);
        return ResponseEntity.ok().body(user.getAvatar());
    }
}
