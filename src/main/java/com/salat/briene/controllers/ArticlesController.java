package com.salat.briene.controllers;

import com.salat.briene.entities.ArticleState;
import com.salat.briene.entities.User;
import com.salat.briene.exceptions.AnonymousUserException;
import com.salat.briene.exceptions.ArticleNotFoundException;
import com.salat.briene.services.UserService;
import com.salat.briene.entities.Article;
import com.salat.briene.services.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class ArticlesController {
    private final UserService userService;
    private final ArticleService articleService;

    @GetMapping("/articles")
    public String getArticles(@RequestParam(required = false) String type, Model model) {
        try {
            User currentUser = userService.getUserFromContext();
            if (userService.isUser(currentUser, "admin")) {
                model.addAttribute("isAdmin", true);
                model.addAttribute("articles", articleService.getArticlesByState(type));
            } else {
                model.addAttribute("articles", articleService.getArticlesByState("published"));
            }
            model.addAttribute("canUseEditor", true);
            return "articles";
        } catch (AnonymousUserException ignored) {
        }
        model.addAttribute("articles", articleService.getArticlesByState("published"));
        return "articles";
    }

    @GetMapping("/articles/{id}")
    public String deleteFile(@PathVariable Long id, Model model) {
        try {
            Article article = articleService.getArticleById(id);

            if (article.getState().equals(ArticleState.ARTICLE_PUBLISHED)) {
                model.addAttribute("article", article);
            }

            try {
                User currentUser = userService.getUserFromContext();
                if (articleService.canUserEditArticle(currentUser, article)) {
                    model.addAttribute("canEdit", true);
                }
            } catch (AnonymousUserException ignored) {
            }

            return "article";
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Cannot open article");
            return "articles";
        }
    }

    @GetMapping("/articles/delete/{id}")
    public String deleteArticle(@PathVariable Long id, Model model) {
        try {
            Article article = articleService.getArticleById(id);

            User currentUser = userService.getUserFromContext();
            if (userService.isUser(currentUser, "admin") || article.getAuthor().equals(currentUser)) {
                articleService.deleteArticle(id);
                return "redirect:/articles";
            } else {
                throw new ArticleNotFoundException();
            }
        } catch (ArticleNotFoundException | AnonymousUserException e) {
            e.printStackTrace();
            model.addAttribute("error", "Cannot delete article");
            return "articles";
        }
    }
}
