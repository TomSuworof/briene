package com.salat.briene.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@ToString
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
    @ToString.Exclude
    private User user;

    @PastOrPresent
    private OffsetDateTime publicationDate;

    @ManyToOne
    @JoinTable(name = "t_articles_comments",
            joinColumns = {@JoinColumn(name = "comment_id")},
            inverseJoinColumns = {@JoinColumn(name = "article_id")})
    @org.springframework.data.annotation.Transient
    @ToString.Exclude
    private Article article;

    public Comment(String message, User user, Article article) {
        this.id = UUID.randomUUID();
        this.message = message;
        this.user = user;
        this.article = article;
        this.publicationDate = OffsetDateTime.now();
    }
}
