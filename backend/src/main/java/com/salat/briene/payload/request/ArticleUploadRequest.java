package com.salat.briene.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleUploadRequest {
    private String title;
    private String content;
    private String summary;
}
