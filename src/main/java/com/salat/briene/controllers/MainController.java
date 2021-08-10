package com.salat.briene.controllers;

import com.salat.briene.entities.User;
import com.salat.briene.exceptions.AnonymousUserException;
import com.salat.briene.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;

    @GetMapping("/")
    public String getStartPage() {
        try {
            User currentUser = userService.getUserFromContext();
            return "redirect:/articles";
        } catch (AnonymousUserException e) {
            return "index";
        }
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @GetMapping("/terms_of_use")
    public String getTerms() {
        return "terms_of_use";
    }

    @GetMapping("/favicon.ico")
    public String getFavicon() {
        return "forward:/public/img/favicon.png";
    }

    @GetMapping("/error")
    public String getErrorPage() {
        return "error";
    }

    @GetMapping("/experimental")
    public String getVue() {
        return "index_vue";
    }
}