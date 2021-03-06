package com.salat.briene.controllers;

import com.salat.briene.payload.request.PasswordChangeRequest;
import com.salat.briene.payload.response.PasswordResetResponse;
import com.salat.briene.services.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/password_reset")
@RequiredArgsConstructor
public class PasswordResetController {
    private final PasswordResetService passwordResetService;

    @PostMapping("/create_request")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PasswordResetResponse> createPasswordResetRequest(@RequestParam String username) {
        passwordResetService.createPasswordResetRequestFor(username);
        String hiddenEmail = passwordResetService.sendEmailWithCodeTo(username);

        PasswordResetResponse response = new PasswordResetResponse(hiddenEmail);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/change_password")
    @ResponseBody
    public void changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest) {
        passwordResetService.setNewPassword(passwordChangeRequest.getRequestId(), passwordChangeRequest.getNewPassword());
    }
}
