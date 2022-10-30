package com.salat.briene.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ArticleUploadRequest {
    private UUID id;
    private String title;
    private String content;
    private String summary;
    private String url;
    private List<String> tags;
}
