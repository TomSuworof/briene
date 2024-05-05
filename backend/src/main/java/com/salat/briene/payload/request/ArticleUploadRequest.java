// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.salat.briene.payload.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
public class ArticleUploadRequest {
    private UUID id;
    private String title;
    @ToString.Exclude private String content;
    private String summary;
    private String url;
    private List<String> tags;
}
