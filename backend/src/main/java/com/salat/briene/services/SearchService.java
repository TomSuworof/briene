package com.salat.briene.services;

import com.salat.briene.entities.Article;
import com.salat.briene.payload.response.PageResponseDTO;
import com.salat.briene.repositories.ArticleSearchRepository;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private static final String ARTICLE_INDEX = "articles";
    private final ArticleSearchRepository articleSearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public PageResponseDTO search(String query, Integer limit, Integer offset) {
//        Page<Article> articles = articleSearchRepository.findArticleByTitleOrSummaryOrContentOrAuthorUsernameAndState(
//                query,
//                query,
//                query.getBytes(),
//                query,
//                ArticleState.PUBLISHED,
//                PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "publicationDate")));

//        Page<Article> articles = articleSearchRepository.findArticleByTitle(
//                query,
//                PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "publicationDate")));

        QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(query, "title", "summary").fuzziness(Fuzziness.TWO);

        Query searchQuery = new NativeSearchQueryBuilder().withFilter(queryBuilder).build();

        // 2. Execute search
        SearchHits<Article> articleSearchHits = elasticsearchOperations.search(searchQuery, Article.class, IndexCoordinates.of(ARTICLE_INDEX));

        // 3. Map searchHits to product list
        List<Article> articles = new ArrayList<>();

        articleSearchHits.forEach(searchHit -> articles.add(searchHit.getContent()));

//        return new PageResponseDTO(
//                offset > 0 && articles.getTotalElements() > 0,
//                (offset + limit) < articles.getTotalElements(),
//                articles.getContent()
//        );

        return new PageResponseDTO(
                false,
                false,
                articles
        );
    }
}
