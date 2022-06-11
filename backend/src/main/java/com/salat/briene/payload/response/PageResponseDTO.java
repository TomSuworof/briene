package com.salat.briene.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageResponseDTO<T extends ObjectDTO> {
    private final boolean hasBefore;
    private final boolean hasAfter;
    private final List<T> entities;
    private final long totalCount;
}