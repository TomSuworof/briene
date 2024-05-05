// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.salat.briene.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@Table(name = "t_tags")
@NoArgsConstructor
public class Tag {
    @Id
    @NotNull
    private UUID id;

    @NotEmpty(message = ConstraintViolationMessage.ROLE_NAME_EMPTY)
    private String name;

    public Tag(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }
}
