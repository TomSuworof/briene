package com.salat.briene.services;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.ArticleState;
import com.salat.briene.payload.response.ArticleDTO;
import com.salat.briene.payload.response.PageResponseDTO;
import com.salat.briene.payload.response.SearchResponseDTO;
import com.salat.briene.repositories.ArticleRepository;
import com.salat.briene.repositories.ArticleSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

@Log4j2
@Service
@RequiredArgsConstructor
public class SearchService {
    private static final String ARTICLES_INDEX = "articles";

    private final ArticleRepository articleRepository;
    private final ArticleSearchRepository articleSearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    @PostConstruct
    private void init() {
        Executors.newSingleThreadExecutor().submit(() -> {
            articleSearchRepository.saveAll(articleRepository.findAll());
            log.info("Index was rebuilt");
        });
    }

    public SearchResponseDTO search(String query, Integer limit, Integer offset) {
        QueryBuilder queryBuilder = QueryBuilders
                .multiMatchQuery(query, "title", "summary", "content", "author.username", "author.bio")
                .autoGenerateSynonymsPhraseQuery(true)
                .fuzziness(Fuzziness.AUTO);

        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withSorts(SortBuilders.scoreSort())
                .build();

        SearchHits<Article> articleSearchHits = elasticsearchOperations.search(searchQuery, Article.class, IndexCoordinates.of(ARTICLES_INDEX));

        List<UUID> uuids = articleSearchHits
                .stream()
                .map(SearchHit::getContent)
                .filter(article -> article.getState().equals(ArticleState.PUBLISHED))
                .map(Article::getId)
                .toList();

        List<Article> articles = articleRepository.findAllById(uuids);

        return new SearchResponseDTO(
                articleSearchHits.getSuggest(),
                new PageResponseDTO<>(
                        offset > 0 && articleSearchHits.getTotalHits() > 0,
                        (offset + limit) < articleSearchHits.getTotalHits(),
                        articles.stream().skip(offset).limit(Math.min(articleSearchHits.getTotalHits(), limit)).map(ArticleDTO::new).toList(),
                        articles.size()));
    }
}
