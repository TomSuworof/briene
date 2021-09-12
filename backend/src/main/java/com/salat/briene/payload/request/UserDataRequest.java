package com.salat.briene.payload.request;

import com.salat.briene.entities.Article;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;
import java.util.Set;

@Getter
@Setter
public class UserDataRequest {
    private Optional<String> email;
    private Optional<String> bio;
    private Optional<Set<Article>> bookmarks;
    private Optional<String> password;

    public UserDataRequest() {
        this.email = Optional.empty();
        this.bio = Optional.empty();
        this.bookmarks = Optional.empty();
        this.password = Optional.empty();
    }
}
