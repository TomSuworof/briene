package com.salat.briene.controllers;

import com.salat.briene.entities.Tag;
import com.salat.briene.payload.response.ArticleDTO;
import com.salat.briene.payload.response.PageResponseDTO;
import com.salat.briene.services.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping("/getArticlesByTag")
    public @ResponseBody ResponseEntity<PageResponseDTO<ArticleDTO>> getArticlesByTag(
            @RequestParam String tag,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer offset
    ) {
        log.debug("getArticlesByTag() called. Tag: {}, limit: {}, offset: {}", () -> tag, () -> limit, () -> offset);
        PageResponseDTO<ArticleDTO> response = tagService.getPageWithArticles(tag, limit, offset);
        log.trace("getArticlesByTag(). Response to send: {}", () -> response);

        if (!response.isHasBefore() && !response.isHasAfter()) {
            log.trace("getArticlesPaginated(). Response contains all articles. Response status is OK");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            log.trace("getArticlesPaginated(). Response does not contain all articles. Response status is PARTIAL_CONTENT");
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
        }
    }

    // POST only for correct parsing of list
    @PostMapping("/getTags")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<List<String>> getTagsWithExclusion(@RequestBody List<String> excludedTags) {
        log.debug("getTagsWithExclusion() called. Excluded tags: {}", () -> excludedTags);
        return ResponseEntity.ok(tagService.getTagsWithExclusion(excludedTags).stream().map(Tag::getName).toList());
    }
}
