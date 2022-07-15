package com.salat.briene.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CommentUploadRequest {
    private UUID articleId;
    private String message;
}
