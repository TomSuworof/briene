package com.salat.briene.controllers;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.User;
import com.salat.briene.exceptions.AnonymousUserException;
import com.salat.briene.exceptions.ArticleNotFoundException;
import com.salat.briene.services.ArticleService;
import com.salat.briene.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PersonalAreaArticlesController {
    private final UserService userService;
    private final ArticleService articleService;

    @GetMapping("/personal_area/articles")
    public String getArticles(@RequestParam(required = false) String type, Model model) {
        User currentUser = userService.getUserFromContext();
        List<Article> articles = articleService.getArticlesByAuthorAndState(currentUser, type);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("articles", articles);
        model.addAttribute("show_admin_page", userService.isUser(currentUser, "admin"));
        return "personal_area";
    }

    @GetMapping("/personal_area/articles/delete/{id}")
    public String deleteArticle(@PathVariable Long id, Model model) {
        try {
            Article article = articleService.getArticleById(id);

            User currentUser = userService.getUserFromContext();
            if (userService.isUser(currentUser, "admin") || article.getAuthor().equals(currentUser)) {
                articleService.deleteArticleById(id);
                return "redirect:/personal_area";
            } else {
                throw new ArticleNotFoundException();
            }
        } catch (ArticleNotFoundException | AnonymousUserException e) {
            e.printStackTrace();
            model.addAttribute("articlesError", "Cannot delete article");
            return "articles";
        }
    }
}
