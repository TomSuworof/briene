package com.salat.briene.entities;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "t_users")
public class User implements UserDetails {

    @Id
    @NotNull
    @GeneratedValue
    private UUID id;

    @NotBlank(message = ConstraintViolationMessage.USER_USERNAME_EMPTY)
    @Field(type = FieldType.Text)
    private String username;

    @NotBlank(message = ConstraintViolationMessage.USER_EMAIL_EMPTY)
    @Email(message = ConstraintViolationMessage.USER_EMAIL_INVALID)
    private String email;

    @Size(max = 255, message = ConstraintViolationMessage.USER_BIO_LIMIT)
    private String bio;

    @NotBlank(message = ConstraintViolationMessage.USER_PASSWORD_EMPTY)
    private String password;

    @NotEmpty
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @org.springframework.data.annotation.Transient
    private String avatar;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "t_user_bookmarks",
            joinColumns = @JoinColumn(name = "bookmarked_by_user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "bookmarked_article_id", referencedColumnName = "id"))
    @org.springframework.data.annotation.Transient
    private Set<Article> bookmarkedArticles;

    @ManyToMany
    @JoinTable(name="t_followers",
            joinColumns=@JoinColumn(name="author_id"),
            inverseJoinColumns=@JoinColumn(name="follower_id")
    )
    @org.springframework.data.annotation.Transient
    private Set<User> followers;

    @ManyToMany(mappedBy = "followers")
    @org.springframework.data.annotation.Transient
    private Set<User> followings;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !roles.contains(RoleEnum.BLOCKED.getAsObject());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email);
    }

    public boolean is(RoleEnum role) {
        return this.getRoles().contains(role.getAsObject());
    }
}
