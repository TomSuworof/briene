//package com.salat.briene.controllers;
//
//import com.salat.briene.entities.ArticleState;
//import com.salat.briene.entities.User;
//import com.salat.briene.exceptions.AnonymousUserException;
//import com.salat.briene.exceptions.ArticleNotFoundException;
//import com.salat.briene.exceptions.IllegalArticleStateException;
//import com.salat.briene.services.UserService;
//import com.salat.briene.entities.Article;
//import com.salat.briene.services.ArticleService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.HashMap;
//import java.util.Set;
//
//@Controller
//@RequiredArgsConstructor
//public class ArticlesController {
//    private final UserService userService;
//    private final ArticleService articleService;
//
//    @GetMapping("/articles")
//    public String getArticles(Model model) {
//        try {
//            model.addAttribute("articles", articleService.getArticlesByState("published"));
//            return "articles";
//        } catch (IllegalArticleStateException e) {
//            return "redirect:/error";
//        }
//    }
//
//    @GetMapping("/articles/{id}")
//    public String getArticle(@PathVariable Long id, Model model) {
//        try {
//            Article article = articleService.getArticleById(id);
//
//            if (article.getState().equals(ArticleState.ARTICLE_PUBLISHED)) {
//                model.addAttribute("article", article);
//                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
//                model.addAttribute("publicationDate", formatter.format(article.getPublicationDate()));
//            }
//
//            try {
//                User currentUser = userService.getUserFromContext();
//                if (articleService.canUserEditArticle(currentUser, article)) {
//                    model.addAttribute("canEdit", true);
//                }
//
//                model.addAttribute("inBookmarks", currentUser.getBookmarkedArticles().contains(article));
//            } catch (AnonymousUserException ignored) {
//            }
//
//            return "article";
//        } catch (IOException e) {
//            e.printStackTrace();
//            model.addAttribute("error", "Cannot open article");
//            return "articles";
//        }
//    }
//
//    @GetMapping("/articles/{id}/{action}")
//    public String editBookmarks(@PathVariable String id, @PathVariable String action, Model model) {
//        Long articleId = Long.parseLong(id);
//        switch (action) {
//            case "add_to_bookmarks" -> {
//                return addToBookmarks(articleId, model);
//            }
//            case "remove_from_bookmarks" -> {
//                return removeFromBookmarks(articleId, model);
//            }
//            default -> {
//                model.addAttribute("error", "Cannot execute command");
//                return "articles";
//            }
//        }
//    }
//
//    private String addToBookmarks(Long id, Model model) {
//        try {
//            Article article = articleService.getArticleById(id);
//            User currentUser = userService.getUserFromContext();
//
//            Set<Article> bookmarks = currentUser.getBookmarkedArticles();
//            bookmarks.add(article);
//
//            userService.updateUser(currentUser.getId(), new HashMap<>(){{
//                put("bookmarks", bookmarks);
//            }});
//
//        } catch (ArticleNotFoundException | AnonymousUserException ignored) {
//        }
//        model.addAttribute("id", id);
//        return "redirect:/articles/{id}";
//    }
//
//    private String removeFromBookmarks(Long id, Model model) {
//        try {
//            Article article = articleService.getArticleById(id);
//            User currentUser = userService.getUserFromContext();
//
//            Set<Article> bookmarks = currentUser.getBookmarkedArticles();
//            bookmarks.remove(article);
//
//            userService.updateUser(currentUser.getId(), new HashMap<>(){{
//                put("bookmarks", bookmarks);
//            }});
//
//        } catch (ArticleNotFoundException | AnonymousUserException ignored) {
//        }
//        model.addAttribute("id", id);
//        return "redirect:/articles/{id}";
//    }
//}
