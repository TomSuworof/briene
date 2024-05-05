// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.salat.briene.services;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.ArticleState;
import com.salat.briene.entities.Tag;
import com.salat.briene.exceptions.TagNotFoundException;
import com.salat.briene.payload.response.ArticleDTO;
import com.salat.briene.payload.response.PageResponseDTO;
import com.salat.briene.repositories.ArticleRepository;
import com.salat.briene.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final ArticleRepository articleRepository;

    public PageResponseDTO<ArticleDTO> getPageWithArticles(String tagName, Integer limit, Integer offset) {
        Tag tag = tagRepository.findByName(tagName).orElseThrow(TagNotFoundException::new);

        Page<Article> articles = articleRepository.findArticlesByStateAndTags_Id(ArticleState.PUBLISHED, tag.getId(), PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "publicationDate")));

        return new PageResponseDTO<>(
                offset > 0 && articles.getTotalElements() > 0,
                (offset + limit) < articles.getTotalElements(),
                articles.getContent().stream().map(ArticleDTO::new).collect(Collectors.toList()),
                articles.getTotalElements());
    }

    public List<Tag> getTagsWithExclusion(List<String> excludedTagNames) {
        List<Tag> tags = tagRepository.findAll();

        if (excludedTagNames != null) {
            List<Tag> excludedTags = excludedTagNames.stream()
                    .map(tagRepository::findByName)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
            tags.removeAll(excludedTags);
        }

        return tags;
    }

    public Set<Tag> mapToTags(List<String> tags) {
        saveTags(tags);
        return tagRepository.findAll().stream().filter(tag -> tags.contains(tag.getName())).collect(Collectors.toSet());
    }

    public void saveTags(List<String> tags) {
        List<String> oldTags = tagRepository
                .findAll()
                .stream()
                .map(Tag::getName)
                .collect(Collectors.toList());
        List<Tag> newTags = tags
                .stream()
                .filter(tag -> !oldTags.contains(tag))
                .map(Tag::new) // tag -> new Tag(tag)
                .collect(Collectors.toList());
        tagRepository.saveAll(newTags);
    }
}
