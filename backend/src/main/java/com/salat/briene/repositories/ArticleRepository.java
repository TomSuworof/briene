package com.salat.briene.repositories;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.ArticleState;
import com.salat.briene.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID> {

    Optional<Article> findArticleByTitleAndState(String title, ArticleState state);

    List<Article> findArticlesByState(ArticleState state);


    List<Article> findArticlesByState(ArticleState state, Pageable pageable);

    Long countArticlesByState(ArticleState state);


    List<Article> findArticlesByAuthor(User author);

    List<Article> findArticlesByAuthorAndState(User author, ArticleState state);
}
