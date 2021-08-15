package com.salat.briene.controllers;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.User;
import com.salat.briene.exceptions.AnonymousUserException;
import com.salat.briene.exceptions.ArticleNotFoundException;
import com.salat.briene.services.ArticleEditorService;
import com.salat.briene.services.ArticleService;
import com.salat.briene.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class ArticleEditorController {
    private final UserService userService;
    private final ArticleService articleService;
    private final ArticleEditorService articleEditorService;

    @GetMapping("/article_editor")
    public String getEditorPage() {
        return "article_editor";
    }

    @GetMapping("/article_editor/{id}")
    public String getEditorPageWithArticle(@PathVariable Long id, Model model) {
        try {
            Article article = articleService.getArticleById(id);

            User currentUser = userService.getUserFromContext();
            if (articleService.canUserEditArticle(currentUser, article)) {
                model.addAttribute("titleValue", article.getTitle());
                model.addAttribute("contentValue", new String(article.getContent()));
                return "article_editor";
            } else {
                throw new ArticleNotFoundException();
            }
        } catch (ArticleNotFoundException | AnonymousUserException e) {
            e.printStackTrace();
            model.addAttribute("error", "Cannot edit article");
            return "articles";
        }
    }

    @PostMapping("/article_editor/load")
    public String publishArticle(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String action,
            Model model) {
        try {
            User currentUser = userService.getUserFromContext();
            articleEditorService.loadArticle(currentUser, title, content, action);
            return "redirect:/articles";
        } catch (IOException | AnonymousUserException e) {
            e.printStackTrace();
            model.addAttribute("titleValue", title);
            model.addAttribute("contentValue", content);
            model.addAttribute("error", "Error occurred while saving article. Try to remove article with same title");
            return "article_editor";
        }
    }
}
