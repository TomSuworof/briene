package com.salat.briene.entities;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name = "t_users")
public class User implements UserDetails {

    @Id
    private Long id;

    @Column
    private String username;

    @Column
    private String email;

    @Column
    private String secretQuestion;

    @Column
    private String secretAnswer;

    @Column
    private String password;

    @Transient
    private String passwordConfirm;

    @Transient
    private String passwordNew;

    @Transient
    private String passwordNewConfirm;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @OneToMany(mappedBy = "author")
    private Set<Article> articles;

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
}
