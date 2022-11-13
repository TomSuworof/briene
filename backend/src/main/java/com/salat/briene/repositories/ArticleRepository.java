package com.salat.briene.repositories;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.ArticleState;
import com.salat.briene.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID> {

    Optional<Article> findByUrl(String url);

    Optional<Article> findArticleByTitleAndStateAndAuthor_Username(String title, ArticleState state, String username);

    Page<Article> findArticlesByState(ArticleState state, Pageable pageable);


    Page<Article> findArticlesByAuthor(User author, Pageable pageable);

    List<Article> findArticlesByStateAndAuthor_Username(ArticleState state, String username);

    Page<Article> findArticlesByAuthorAndState(User author, ArticleState state, Pageable pageable);

    @Transactional // it is better to use transaction, because we delete from several tables
    @Modifying // without this there wil be error: The statement did not return a result set
    @Query(value = "delete from t_user_bookmarks as bookmarks where bookmarks.bookmarked_article_id = ?1 ; " +
            "delete from t_articles_tags as tags where tags.article_id = ?1 ;" +
            "delete from t_articles as articles where articles.id = ?1 ; ",
            nativeQuery = true)
    // delete article from all bookmarks
    // delete article from tags relation 
    // delete article itself
    // todo make it safer
    void deleteArticleById(@Param("articleId") UUID id);


    Page<Article> findArticlesByStateAndTags_Id(ArticleState state, UUID tagId, Pageable pageable);
}
