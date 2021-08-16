package com.salat.briene.controllers;

import com.salat.briene.entities.Article;
import com.salat.briene.exceptions.ArticleNotFoundException;
import com.salat.briene.exceptions.IllegalArticleStateException;
import com.salat.briene.services.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminArticlesController {
    private final ArticleService articleService;

    @GetMapping("/admin/articles")
    public String getArticles(@RequestParam(required = false) String type, Model model) {
        try {
            List<Article> articles = articleService.getArticlesByState(type);

            model.addAttribute("articles", articles);
            return "admin";
        } catch (IllegalArticleStateException e) {
            return "redirect:/error";
        }
    }

    @GetMapping("/admin/articles/{id}/delete")
    public String deleteArticle(@PathVariable Long id, Model model) {
        try {
            articleService.deleteArticleById(id);
            return "redirect:/admin";
        } catch (ArticleNotFoundException e) {
            e.printStackTrace();
            model.addAttribute("error", "Cannot delete article");
            return "admin";
        }
    }
}
