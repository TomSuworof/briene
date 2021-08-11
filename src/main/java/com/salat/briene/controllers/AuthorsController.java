package com.salat.briene.controllers;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.User;
import com.salat.briene.exceptions.UserNotFoundException;
import com.salat.briene.services.ArticleService;
import com.salat.briene.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthorsController {
    private final UserService userService;
    private final ArticleService articleService;

    @GetMapping("/authors/{authorName}")
    public String getAuthorPage(@PathVariable String authorName, Model model) {
        try {
            User author = userService.loadUserByUsername(authorName);
            List<Article> articles = articleService.getArticlesByAuthorAndState(author, "published");

            model.addAttribute("author", author);
            model.addAttribute("articles", articles);
            return "author";
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }
}
