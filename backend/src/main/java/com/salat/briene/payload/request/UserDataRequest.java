// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.salat.briene.payload.request;

import com.salat.briene.entities.Article;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@ToString
public class UserDataRequest {
    @ToString.Exclude private Optional<String> avatar;
    private Optional<String> email;
    private Optional<String> bio;
    private Optional<Set<Article>> bookmarks;
    @ToString.Exclude private Optional<String> password;

    public UserDataRequest() {
        this.avatar = Optional.empty();
        this.email = Optional.empty();
        this.bio = Optional.empty();
        this.bookmarks = Optional.empty();
        this.password = Optional.empty();
    }
}
