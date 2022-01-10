package com.salat.briene.services;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.ArticleState;
import com.salat.briene.payload.response.PageResponseDTO;
import com.salat.briene.repositories.ArticleSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final ArticleSearchRepository articleSearchRepository;

    public PageResponseDTO search(String query, Integer limit, Integer offset) {
        Page<Article> articles = articleSearchRepository.findArticleByTitleOrSummaryOrContentOrAuthorUsernameAndState(
                query,
                query,
                query.getBytes(),
                query,
                ArticleState.PUBLISHED,
                PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "publicationDate")));

        return new PageResponseDTO(
                offset > 0 && articles.getTotalElements() > 0,
                (offset + limit) < articles.getTotalElements(),
                articles.getContent()
        );
    }
}
