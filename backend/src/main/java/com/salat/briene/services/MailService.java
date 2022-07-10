package com.salat.briene.services;

import com.salat.briene.config.MailConfig;
import com.salat.briene.entities.Article;
import lombok.RequiredArgsConstructor;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final MailConfig mailConfig;

    public void sendPasswordChangeCode(String emailAddress, String code) throws EmailException {
        HtmlEmail email = getEmailTemplate(emailAddress);

        email.setSubject("Password reset");
        email.setHtmlMsg("<html>\n" +
                "<body>\n" +
                "\n" +
                "<h1>You sent request for password reset.</h1\n" +
                "<p>" +
                "Here is your code: " + code +
                "\n" +
                "</p>\n" +
                "<p>If you did not send the request, ignore this message or contact us via reply on the message.</p>\n" +
                "<p>briene team <3</p>\n" +
                "</body>\n" +
                "</html>");
        email.send();
    }

    public void sendRoleChanged(String emailAddress, String role) throws EmailException {
//        role = role.equals("blocked") ? role : "an " + role;
        HtmlEmail email = getEmailTemplate(emailAddress);

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

    public void sendRegistrationConfirm(String emailAddress, String name) throws EmailException {
        HtmlEmail email = getEmailTemplate(emailAddress);

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

    public void sendNotificationAboutNewArticle(String emailAddress, Article article) throws EmailException {
        HtmlEmail email = getEmailTemplate(emailAddress);

        email.setSubject(String.format("%s published new article", article.getAuthor().getUsername()));
        email.setHtmlMsg("<html>\n" +
                "<body>\n" +
                "\n" +
                "<h1>" + article.getTitle() + "</h1\n" +
                "<p style=\"white-space: pre-line;\">" + article.getSummary() + "</p>\n" +
                "<a href=\"https://briene.herokuapp.com/articles/" + article.getUrl() + "\">Read now</a>\n" +
                "<p>briene team <3</p>\n" +
                "</body>\n" +
                "</html>");
        email.send();
    }

    private HtmlEmail getEmailTemplate(String to) throws EmailException {
        HtmlEmail email = new HtmlEmail();
        email.setHostName(mailConfig.getHost());
        email.setSmtpPort(mailConfig.getPort());
        email.setAuthenticator(new DefaultAuthenticator(
                mailConfig.getUsername(),
                mailConfig.getPassword())
        );
        email.setSSLOnConnect(true);
        email.setFrom(mailConfig.getUsername(), "Briene Team");
        email.setCharset("utf-8");
        email.addTo(to);
        return email;
    }
}