package com.salat.briene.payload.response;

import com.salat.briene.entities.Comment;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public class CommentDTO implements ObjectDTO {
    private final UUID id;
    private final String message;
    private final String authorUsername;
    private final OffsetDateTime publicationDate;

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.message = comment.getMessage();
        this.authorUsername = comment.getUser().getUsername();
        this.publicationDate = comment.getPublicationDate();
    }
}
