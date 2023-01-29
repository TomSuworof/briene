package com.salat.briene.payload.response;

import com.salat.briene.entities.User;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AuthorDTO {
    private final String username;
    private final String bio;
    private final PageResponseDTO<ArticleDTO> articles;
    private final Integer followersCounter;

    public AuthorDTO(User user, PageResponseDTO<ArticleDTO> articles) {
        this.username = user.getUsername();
        this.bio = user.getBio();
        this.articles = articles;
        this.followersCounter = user.getFollowers().size();
    }
}