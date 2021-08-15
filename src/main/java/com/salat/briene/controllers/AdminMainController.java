package com.salat.briene.controllers;

import com.salat.briene.services.ArticleService;
import com.salat.briene.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminMainController {
    private final ArticleService articleService;
    private final UserService userService;

    @GetMapping("/admin")
    public String getAdminPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("articles", articleService.getArticlesByState(null)); // means 'all'
        return "admin";
    }
}
