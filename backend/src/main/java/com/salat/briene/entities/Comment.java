package com.salat.briene.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "t_comments")
public class Comment {

    @Id
    @NotNull
    private UUID id;

    @NotBlank
    private String message;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PastOrPresent
    private OffsetDateTime publicationDate;

    public Comment(String message, User user) {
        this.id = UUID.randomUUID();
        this.message = message;
        this.user = user;
        this.publicationDate = OffsetDateTime.now();
    }
}
