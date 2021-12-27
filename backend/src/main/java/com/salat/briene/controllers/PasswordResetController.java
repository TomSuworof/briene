package com.salat.briene.controllers;

import com.salat.briene.payload.request.PasswordChangeRequest;
import com.salat.briene.payload.response.PasswordResetResponse;
import com.salat.briene.services.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.mail.EmailException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/password_reset")
@RequiredArgsConstructor
public class PasswordResetController {
    private final PasswordResetService passwordResetService;

    @PostMapping("/create_request")
    public ResponseEntity<PasswordResetResponse> createPasswordResetRequest(@RequestParam String username) throws EmailException {
        passwordResetService.createPasswordResetRequestFor(username);
        String hiddenEmail = passwordResetService.sendEmailWithCodeTo(username);

        PasswordResetResponse response = new PasswordResetResponse(hiddenEmail);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/change_password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest) {
        passwordResetService.setNewPassword(passwordChangeRequest.getRequestId(), passwordChangeRequest.getNewPassword());

        return ResponseEntity.ok().body("Done"); // todo replace message
    }
}
