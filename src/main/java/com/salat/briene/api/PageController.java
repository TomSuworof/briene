package com.salat.briene.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/experimental")
public class PageController {
    @GetMapping("")
    public String getIndexPage() {
        return "index_vue";
    }

    @GetMapping("/articles")
    public String getArticlesPage() {
        return "articles_vue";
    }
}
