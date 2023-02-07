package com.salat.briene.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@Table(name = "t_images")
@NoArgsConstructor
public class Image {
    @Id
    @NotNull
    private UUID id;

    @ToString.Exclude
    @Column(columnDefinition = "text")
    @Basic(fetch = FetchType.LAZY)
    private String content;

    public Image(String content) {
        this.id = UUID.randomUUID();
        this.content = content;
    }
}
