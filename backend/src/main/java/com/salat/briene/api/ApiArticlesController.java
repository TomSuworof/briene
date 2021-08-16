package com.salat.briene.api;

import com.google.gson.Gson;
import com.salat.briene.entities.Article;
import com.salat.briene.exceptions.IllegalArticleStateException;
import com.salat.briene.services.ArticleService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiArticlesController {
    private final ArticleService articleService;

    @GetMapping("/articles")
    public ResponseEntity<String> getArticles() {
        try {
            List<ArticleContainer> publishedArticles = articleService.getArticlesByState("published")
                    .stream().map(ArticleContainer::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok().body(new Gson().toJson(publishedArticles));
        } catch (IllegalArticleStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Getter
    private static class ArticleContainer {
        private final Long id;
        private final String title;

        public ArticleContainer(Article article) {
            this.id = article.getId();
            this.title = article.getTitle();
        }
    }
}
