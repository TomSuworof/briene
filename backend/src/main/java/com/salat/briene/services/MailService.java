package com.salat.briene.services;

import com.salat.briene.config.MailConfig;
import lombok.RequiredArgsConstructor;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final MailConfig mailConfig;

    public void send(String to, String theme, String message) throws EmailException {
        HtmlEmail email = new HtmlEmail();
        email.setHostName(mailConfig.getHost());
        email.setSmtpPort(mailConfig.getPort());
        email.setAuthenticator(new DefaultAuthenticator(
                mailConfig.getUsername(),
                mailConfig.getPassword())
        );
        email.setSSLOnConnect(true);
        email.setFrom(mailConfig.getUsername());
        email.setCharset("utf-8");
        email.addTo(to);
        switch (theme) {
            case "password_change" -> sendPasswordReset(email, message);
            case "role_change" -> sendRoleChanged(email, message);
            case "registration_confirm" -> sendRegistrationConfirm(email, message);
        }
    }

    private void sendPasswordReset(HtmlEmail email, String link) throws EmailException {
        email.setSubject("Password reset");
        email.setHtmlMsg("<html>\n" +
                "<body>\n" +
                "\n" +
                "<h1>You sent request for password reset.</h1\n" +
                "<p>" +
                "<a href=\"" + link + "\">Reset password</a>\n" +
                "\n" +
                "</p>\n" +
                "<p>If you did not send the request, ignore this message or contact us via reply on the message.</p>\n" +
                "<p>briene team <3</p>\n" +
                "</body>\n" +
                "</html>");
        email.send();
    }

    private void sendRoleChanged(HtmlEmail email, String role) throws EmailException {
        role = role.equals("blocked") ? role : "an " + role;
        email.setSubject("Your role was changed");
        email.setHtmlMsg("<html>\n" +
                "<body>\n" +
                "\n" +
                "<h1>Your role was changed.</h1\n" +
                "<p>Heads of universe decided that you should be " + role + ".</p>\n" +
                "<p>briene team <3</p>\n" +
                "</body>\n" +
                "</html>");
        email.send();
    }

    private void sendRegistrationConfirm(HtmlEmail email, String name) throws EmailException {
        email.setSubject("Welcome to briene!");
        email.setHtmlMsg("<html>\n" +
                "<body>\n" +
                "\n" +
                "<h1>" + name + ", you signed up at briene.</h1\n" +
                "<p>Heads of universe appreciate and greet you.</p>\n" +
                "<a href=\"https://briene.herokuapp.com\">Start building better world</a>\n" +
                "<p>briene team <3</p>\n" +
                "</body>\n" +
                "</html>");
        email.send();
    }
}