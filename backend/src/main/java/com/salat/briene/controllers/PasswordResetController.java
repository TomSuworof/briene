// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.salat.briene.controllers;

import com.salat.briene.payload.request.PasswordChangeRequest;
import com.salat.briene.payload.response.PasswordResetResponse;
import com.salat.briene.services.PasswordResetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Log4j2
@Controller
@RequestMapping("/api/password_reset")
@RequiredArgsConstructor
public class PasswordResetController {
    private final PasswordResetService passwordResetService;

    @PostMapping("/create_request")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<PasswordResetResponse> createPasswordResetRequest(@RequestParam String username) {
        log.debug("createPasswordResetRequest() called. Username: {}", username);
        passwordResetService.createPasswordResetRequestFor(username);
        String hiddenEmail = passwordResetService.sendEmailWithCodeTo(username);

        PasswordResetResponse response = new PasswordResetResponse(hiddenEmail);
        log.trace("createPasswordResetRequest(). Response to send: {}", () -> response);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/change_password")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest) {
        log.debug("changePassword() called. User info cannot be displayed");
        passwordResetService.setNewPassword(passwordChangeRequest.getRequestId(), passwordChangeRequest.getNewPassword());
    }
}
