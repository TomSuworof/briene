package com.salat.briene.services;

import com.salat.briene.entities.PasswordResetRequest;
import com.salat.briene.entities.User;
import com.salat.briene.exceptions.PasswordResetRequestNotFoundException;
import com.salat.briene.exceptions.UserNotFoundException;
import com.salat.briene.repositories.PasswordResetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final UserService userService;
    private final PasswordResetRepository passwordResetRepository;
    private final MailService mailService;

    private String email;

    public void sendPasswordResetLinkTo(String username) throws UserNotFoundException {
            String id = UUID.randomUUID().toString();
            Date created = new Date();
            User requiredUser = (User) userService.loadUserByUsername(username);
            email = requiredUser.getEmail();
            passwordResetRepository.save(new PasswordResetRequest(id, username, created));

            String link = "";// "https://acl0ud.herokuapp.com/password_reset/change_password/" + id;

            mailService.send(email, "password_change", link);
    }

    public boolean isRequestValid(String id) {
        Optional<PasswordResetRequest> request = passwordResetRepository.findById(id);
        if(request.isPresent()) {
            Calendar now = new GregorianCalendar();
            Date dateCreated = request.get().getCreated();
            Calendar dateExpired = new GregorianCalendar();
            dateExpired.setTime(dateCreated);
            dateExpired.add(Calendar.HOUR, 24);
            return now.before(dateExpired);
        } else {
            return false;
        }
    }

    public String getEmail() {
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

    public void setNewPasswordOf(String passwordNew, String requestId) throws PasswordResetRequestNotFoundException, UserNotFoundException {
        Optional<PasswordResetRequest> passwordResetRequestOpt = passwordResetRepository.findById(requestId);

        if (passwordResetRequestOpt.isEmpty()) {
            throw new PasswordResetRequestNotFoundException();
        }

        PasswordResetRequest passwordResetRequest = passwordResetRequestOpt.get();

        Map<String, String> newData = new HashMap<>(){{
            put("password", passwordNew);
        }};

        Long userId = ((User) userService.loadUserByUsername(passwordResetRequest.getUsername())).getId();

        passwordResetRepository.delete(passwordResetRequest);
        userService.updateUser(userId, newData);
    }

    public String getSecretQuestionOf(String userId) throws PasswordResetRequestNotFoundException, UserNotFoundException {
        Optional<PasswordResetRequest> passwordResetRequest = passwordResetRepository.findById(userId);

        if (passwordResetRequest.isPresent()) {
            User userForNewPassword = (User) userService.loadUserByUsername(passwordResetRequest.get().getUsername());
            return userForNewPassword.getSecretQuestion();
        } else {
            throw new PasswordResetRequestNotFoundException();
        }
    }

    public String getSecretAnswerOf(String id) throws PasswordResetRequestNotFoundException, UserNotFoundException {
        Optional<PasswordResetRequest> passwordResetRequest = passwordResetRepository.findById(id);

        if (passwordResetRequest.isPresent()) {
            User userForNewPassword = (User) userService.loadUserByUsername(passwordResetRequest.get().getUsername());
            return userForNewPassword.getSecretAnswer();
        } else {
            throw new PasswordResetRequestNotFoundException();
        }
    }
}