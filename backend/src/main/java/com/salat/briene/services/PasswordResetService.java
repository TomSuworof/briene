package com.salat.briene.services;

import com.salat.briene.entities.PasswordResetRequest;
import com.salat.briene.entities.User;
import com.salat.briene.exceptions.PasswordResetRequestInvalidException;
import com.salat.briene.payload.request.UserDataRequest;
import com.salat.briene.exceptions.PasswordResetRequestNotFoundException;
import com.salat.briene.repositories.PasswordResetRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.mail.EmailException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final UserService userService;
    private final PasswordResetRepository passwordResetRepository;
    private final MailService mailService;

    public void createPasswordResetRequestFor(String username) {
        passwordResetRepository.save(new PasswordResetRequest(username));
    }

    public String sendEmailWithCodeTo(String username) throws EmailException {
        User requiredUser = userService.loadUserByUsername(username);

        String code = passwordResetRepository
                .findByUsername(username)
                .orElseThrow(PasswordResetRequestNotFoundException::new)
                .getId().toString();

        String email = requiredUser.getEmail();
        mailService.send(email, "password_change", code);

        return hideEmail(email);
    }

    private String hideEmail(String email) {
        StringBuilder emailAnswer = new StringBuilder();
        String emailName = email.split("@")[0];
        String emailDomain = email.split("@")[1];
        emailAnswer.append(emailName.charAt(0));
        emailAnswer.append("*".repeat(emailName.length()));
        emailAnswer.append(emailDomain);
        return emailAnswer.toString();
    }

    public void setNewPassword(UUID requestId, String newPassword) {
        Optional<PasswordResetRequest> passwordResetRequestOpt = passwordResetRepository.findById(requestId);

        if (passwordResetRequestOpt.isEmpty()) {
            throw new PasswordResetRequestNotFoundException();
        }

        PasswordResetRequest passwordResetRequest = passwordResetRequestOpt.get();

        if (!passwordResetRequest.isValid()) {
            throw new PasswordResetRequestInvalidException();
        }

        UserDataRequest newUserData = new UserDataRequest();
        newUserData.setPassword(Optional.ofNullable(newPassword));

        UUID userId = userService.loadUserByUsername(passwordResetRequest.getUsername()).getId();

        passwordResetRepository.delete(passwordResetRequest);
        userService.updateUser(userId, newUserData);
    }
}