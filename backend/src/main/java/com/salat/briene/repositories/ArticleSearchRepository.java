package com.salat.briene.repositories;

import com.salat.briene.entities.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ArticleSearchRepository extends ElasticsearchRepository<Article, UUID> {

}
