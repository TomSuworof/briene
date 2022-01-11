package com.salat.briene.repositories;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.ArticleState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ArticleSearchRepository extends ElasticsearchRepository<Article, UUID> {

//    Page<Article> findArticleByTitleOrSummaryOrContentOrAuthorUsernameAndState(
//            String title,
//            String summary,
//            byte[] content,
//            String authorUsername,
//            ArticleState articleState,
//            Pageable pageable);

    Page<Article> findArticleByTitle(String title, Pageable pageable);
}
