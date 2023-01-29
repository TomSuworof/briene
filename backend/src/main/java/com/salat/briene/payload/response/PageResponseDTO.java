package com.salat.briene.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class PageResponseDTO<T extends ObjectDTO> {
    private final boolean hasBefore;
    private final boolean hasAfter;
    private final List<T> entities;
    private final long totalCount;
}