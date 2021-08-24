//package com.salat.briene.controllers;
//
//import com.salat.briene.entities.Article;
//import com.salat.briene.entities.User;
//import com.salat.briene.exceptions.IllegalArticleStateException;
//import com.salat.briene.services.ArticleService;
//import com.salat.briene.services.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.util.List;
//
//@Controller
//@RequiredArgsConstructor
//public class PersonalAreaMainController {
//    private final ArticleService articleService;
//    private final UserService userService;
//
//    @GetMapping("/personal_area")
//    public String returnPersonalPage(Model model) {
//        try {
//            User currentUser = userService.getUserFromContext();
//            List<Article> articles = articleService.getArticlesByAuthorAndState(currentUser, "published");
//
//            model.addAttribute("currentUser", currentUser);
//            model.addAttribute("bookmarks", currentUser.getBookmarkedArticles());
//            model.addAttribute("articles", articles);
//            model.addAttribute("show_admin_page", currentUser.is("admin"));
//            return "personal_area";
//        } catch (IllegalArticleStateException e) {
//            return "redirect:/error";
//        }
//    }
//}