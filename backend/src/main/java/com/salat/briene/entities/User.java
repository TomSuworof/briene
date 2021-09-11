package com.salat.briene.entities;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "t_users")
public class User implements UserDetails {

    @Id
    @NotNull
    private Long id;

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message="Email cannot be empty")
    @Email(message = "Invalid email")
    private String email;

    @Size(max = 255)
    private String bio;

    @NotBlank(message = "Secret question cannot be empty: Is is used in password reset procedure")
    private String secretQuestion;

    @NotBlank(message = "Secret answer cannot be empty: Is is used in password reset procedure")
    private String secretAnswer;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    @NotEmpty
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "t_user_bookmarks",
            joinColumns = @JoinColumn(name = "bookmarked_by_user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "bookmarked_article_id", referencedColumnName = "id"))
    private Set<Article> bookmarkedArticles;

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
        return !roles.contains(new Role(0L, "ROLE_BLOCKED"));
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

    public boolean is(String role) {
        if (role.equals("blocked") && this.getRoles().contains(new Role((long) 0, "ROLE_BLOCKED"))) {
            return true;
        } else if (role.equals("admin") && this.getRoles().contains(new Role((long) 1, "ROLE_ADMIN"))) {
            return true;
        } else if (role.equals("user") && this.getRoles().contains(new Role((long) 2, "ROLE_USER"))) {
            return true;
        } else {
            return false;
        }
    }
}
