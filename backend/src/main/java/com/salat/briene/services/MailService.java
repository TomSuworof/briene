package com.salat.briene.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    //private final MailConfig mailConfig;

    // todo
    public void send(String to, String theme, String message) {
//        try {
//            HtmlEmail email = new HtmlEmail();
//            email.setHostName(mailConfig.getHost());
//            email.setSmtpPort(mailConfig.getPort());
//            email.setAuthenticator(new DefaultAuthenticator(
//                    mailConfig.getUsername(),
//                    mailConfig.getPassword())
//            );
//            email.setSSLOnConnect(true);
//            email.setFrom(mailConfig.getUsername());
//            email.setCharset("utf-8");
//            email.addTo(to);
//            return switch (theme) {
//                case "password_change" -> sendPasswordReset(email, message);
//                case "role_change" -> sendRoleChanged(email, message);
//                case "registration_confirm" -> sendRegistrationConfirm(email, message);
//                default -> false;
//            };
//        } catch (EmailException e) {
//            return false;
//        }
    }

//    private void sendPasswordReset(HtmlEmail email, String link) {
//        try {
//            email.setSubject("Password reset");
//            email.setHtmlMsg("<html>\n" +
//                    "<body>\n" +
//                    "\n" +
//                    "<h1>You sent request for password reset.</h1\n" +
//                    "<p>" +
//                    "<a href=\"" + link + "\">Reset password</a>\n" +
//                    "\n" +
//                    "</p>\n" +
//                    "<p>If you did not send the request, ignore this message or contact us via reply on the message.</p>\n" +
//                    "<p>acloud team <3</p>\n" +
//                    "</body>\n" +
//                    "</html>");
//            email.send();
//            return true;
//        } catch (EmailException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    private void sendRoleChanged(HtmlEmail email, String role) {
//        role = role.equals("blocked") ? role : "an " + role;
//        try {
//            email.setSubject("Your role was changed");
//            email.setHtmlMsg("<html>\n" +
//                    "<body>\n" +
//                    "\n" +
//                    "<h1>Your role was changed.</h1\n" +
//                    "<p>Heads of universe decided that you should be " + role + ".</p>\n" +
//                    "<p>acloud team <3</p>\n" +
//                    "</body>\n" +
//                    "</html>");
//            email.send();
//            return true;
//        } catch (EmailException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    private void sendRegistrationConfirm(HtmlEmail email, String name) {
//        try {
//            email.setSubject("Welcome to acloud!");
//            email.setHtmlMsg("<html>\n" +
//                    "<body>\n" +
//                    "\n" +
//                    "<h1>" + name + ", you signed up at Acloud.</h1\n" +
//                    "<p>Heads of universe appreciate and greet you.</p>\n" +
//                    "<a href=\"https://acl0ud.herokuapp.com\">Start simplify your life</a>\n" +
//                    "<p>acloud team <3</p>\n" +
//                    "<p>P.s.: do not worry: 0 was set by developers, it's okay</p>" +
//                    "</body>\n" +
//                    "</html>");
//            email.send();
//            return true;
//        } catch (EmailException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
}