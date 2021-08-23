package com.salat.briene.api;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.ArticleState;
import com.salat.briene.exceptions.ArticleNotFoundException;
import com.salat.briene.exceptions.IllegalArticleStateException;
import com.salat.briene.services.ArticleService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin("http://localhost:8081")
@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ApiArticlesController {
    private final ArticleService articleService;

    @Getter
    private static class ArticleContainer {
        private final Long id;
        private final String title;
        private final String author;
        private final String htmlContent;
        private final Date publicationDate;

        public ArticleContainer(Article article) {
            this.id = article.getId();
            this.title = article.getTitle();
            this.author = article.getAuthor().getUsername();
            this.htmlContent = article.makeHTML();
            this.publicationDate = article.getPublicationDate();
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getArticles() {
        try {
            List<ArticleContainer> publishedArticles = articleService.getArticlesByState("published")
                    .stream().map(ArticleContainer::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok().body(publishedArticles);
        } catch (IllegalArticleStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    // todo for the rest of urls need to know about user, who sent the request
//
//    @GetMapping("/{id}")
//    public ResponseEntity<?> getArticle(@PathVariable Long id) {
//        try {
//            Article article = articleService.getArticleById(id);
//
//            if (article.getState().equals(ArticleState.ARTICLE_IN_EDITING)) {
//                throw new ArticleNotFoundException();
//            }
//
//            return ResponseEntity.ok().body(new ArticleContainer(article));
//        } catch (ArticleNotFoundException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    @PostMapping("/load")
////    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
//    public ResponseEntity<?> publishArticle() {
//        return ResponseEntity.ok().body("Empty response");
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteArticle(@PathVariable Long id) {
//        return ResponseEntity.ok().body("Empty response");
//    }
}
