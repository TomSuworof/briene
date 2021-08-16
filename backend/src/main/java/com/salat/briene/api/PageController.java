//package com.salat.briene.api;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//@RequiredArgsConstructor
//@RequestMapping("/experimental")
//public class PageController {
//
//    @GetMapping("")
//    public String getIndexPage() {
//        return "index_vue";
//    }
//
//    @GetMapping("/articles")
//    public String getArticlesPage() {
//        return "articles_vue";
//    }
//
//    @GetMapping("/authors/{authorName}")
//    public String getAuthorPage(@PathVariable String authorName, Model model) {
//        model.addAttribute(authorName);
//        return "author_vue";
//    }
//}
