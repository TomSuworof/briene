package com.salat.briene.controllers;

import com.salat.briene.entities.Tag;
import com.salat.briene.payload.response.PageResponseDTO;
import com.salat.briene.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping("/getArticlesByTag")
    public ResponseEntity<PageResponseDTO> getArticlesByTag(@RequestParam String tag, @RequestParam Integer limit, @RequestParam Integer offset) {
        PageResponseDTO response = tagService.getPageWithArticles(tag, limit, offset);

        if (!response.isHasBefore() && !response.isHasAfter()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
        }
    }

    // POST only for correct parsing of list
    @PostMapping("/getTags")
    public ResponseEntity<List<String>> getTagsWithExclusion(@RequestBody List<String> excludedTags) {
        return ResponseEntity.ok(tagService.getTagsWithExclusion(excludedTags).stream().map(Tag::getName).toList());
    }
}
