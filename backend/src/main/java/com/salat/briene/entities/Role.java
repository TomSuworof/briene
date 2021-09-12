package com.salat.briene.entities;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_roles")
public class Role implements GrantedAuthority {

    @Id
    @NotNull
    private Long id;

    @NotEmpty(message = ConstraintViolationMessage.ROLE_NAME_EMPTY)
    private String name;

    @Override
    public String getAuthority() {
        return getName();
    }
}