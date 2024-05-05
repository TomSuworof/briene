// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.salat.briene.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manticoresearch.client.ApiException;
import com.manticoresearch.client.api.IndexApi;
import com.manticoresearch.client.api.SearchApi;
import com.manticoresearch.client.api.UtilsApi;
import com.manticoresearch.client.model.*;
import com.salat.briene.entities.Article;
import com.salat.briene.entities.ArticleState;
import com.salat.briene.payload.response.ArticleDTO;
import com.salat.briene.payload.response.ArticleWithContent;
import com.salat.briene.payload.response.PageResponseDTO;
import com.salat.briene.payload.response.SearchResponseDTO;
import com.salat.briene.repositories.ArticleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class SearchService {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String ARTICLES_INDEX = "articles";

    private final ArticleRepository articleRepository;

    private final IndexApi indexApi;
    private final SearchApi searchApi;
    private final UtilsApi utilsApi;

    @PostConstruct
    private void init() {
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                utilsApi.sql(
                        "drop table if exists articles",
                        true);

                utilsApi.sql(
                        """
                                    create table articles(
                                        article_id text,
                                        title text,
                                        summary text,
                                        content text,
                                        author_username text,
                                        author_bio text
                                    ) morphology='stem_enru'
                                """,
                        true);
            } catch (ApiException e) {
                log.error("init::articles::forEach(). Exception", e);
            }

            articleRepository
                    .findAll()
                    .stream()
                    .parallel()
                    .forEach(article -> {
                        log.trace("init::articles::forEach(). Going to index article with ID: {}", article::getId);

                        InsertDocumentRequest request = new InsertDocumentRequest();

                        request.index(ARTICLES_INDEX).setDoc(Map.of(
                                "article_id", article.getId(),
                                "title", article.getTitle(),
                                "summary", article.getSummary(),
                                "content", article.getContent(),
                                "author_username", article.getAuthor().getUsername(),
                                "author_bio", article.getAuthor().getBio()));

                        try {
                            indexApi.insert(request);
                        } catch (ApiException e) {
                            log.error("init::articles::forEach(). Exception", e);
                        }
                    });
        });
    }

    @Transactional
    public SearchResponseDTO search(String query, Integer limit, Integer offset) {
        log.debug("search() called. query: {}, limit: {}, offset: {}", query, limit, offset);
        try {
            // Create SearchRequest
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.index(ARTICLES_INDEX);
            QueryFilter queryFilter = new QueryFilter();
            queryFilter.setQueryString(query);
            searchRequest.setFulltextFilter(queryFilter);

            // Execute search
            SearchResponse searchResponse = searchApi.search(searchRequest);
            SearchResponseHits hits = searchResponse.getHits();

            // Check if response has null fields
            if (hits == null) {
                return new SearchResponseDTO(new PageResponseDTO<>(false, false, Collections.emptyList(), 0));
            }

            List<Object> hitsObjects = hits.getHits();

            if (hitsObjects == null) {
                return new SearchResponseDTO(new PageResponseDTO<>(false, false, Collections.emptyList(), 0));
            }

            // Parse response object

            List<ArticleDTO> articles = hitsObjects
                    .stream()
                    .map(o -> (LinkedHashMap<String, Object>) o)
                    .map(m -> m.get("_source"))
                    .map(o -> (LinkedHashMap<String, String>) o)
                    .map(map -> map.get("article_id"))
                    .map(UUID::fromString)
                    .map(articleRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .filter(article -> article.getState() == ArticleState.PUBLISHED)
                    .map(ArticleDTO::new)
                    .toList();

            log.trace("search(). Search response count: {}", articles::size);

            // Construct response object DTO
            return new SearchResponseDTO(
                    new PageResponseDTO<>(
                            offset > 0 && articles.size() > 0,
                            (offset + limit) < articles.size(),
                            articles
                                    .stream()
                                    .skip(offset)
                                    .limit(Math.min(articles.size(), limit))
                                    .toList(),
                            articles.size()));
        } catch (ApiException e) {
            log.error("search(). Failed to perform search request", e);
        }

        return null;
    }

    public void save(Article article) {
        log.debug("save() called.");
        try {
            ArticleWithContent articleWithContent = new ArticleWithContent(article);
            Map<String, Object> doc = objectMapper.readValue(objectMapper.writeValueAsString(articleWithContent),
                    new TypeReference<>() {
                    });

            UpdateDocumentRequest updateRequest = new UpdateDocumentRequest();
            updateRequest.index(ARTICLES_INDEX).id((long) article.getId().hashCode()).setDoc(doc);
            indexApi.update(updateRequest);
        } catch (JsonProcessingException e) {
            log.error("save(). Failed to convert article to JSON", e);
        } catch (ApiException e) {
            log.error("save(). Failed to update article", e);
        }
    }

    public void delete(UUID articleID) {
        log.debug("delete() called");
        try {
            DeleteDocumentRequest deleteRequest = new DeleteDocumentRequest();
            deleteRequest.index(ARTICLES_INDEX).id((long) articleID.hashCode());
            indexApi.delete(deleteRequest);
        } catch (ApiException e) {
            log.error("delete(). Failed to delete article", e);
        }
    }
}
