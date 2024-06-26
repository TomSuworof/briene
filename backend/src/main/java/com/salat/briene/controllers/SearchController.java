// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.salat.briene.controllers;

import com.salat.briene.payload.response.SearchResponseDTO;
import com.salat.briene.services.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Log4j2
@Controller
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public @ResponseBody ResponseEntity<SearchResponseDTO> search(
            @RequestParam String query,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer offset
    ) {
        log.debug("search() called. Query: {}, limit: {}, offset: {}", () -> query, () -> limit, () -> offset);
        SearchResponseDTO response = searchService.search(query, limit, offset);
        log.trace("search(). Response to send: {}", () -> response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
