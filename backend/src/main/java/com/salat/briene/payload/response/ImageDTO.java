// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.salat.briene.payload.response;

import com.salat.briene.entities.Image;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ImageDTO implements ObjectDTO {
    private UUID id;
    private String content;

    public ImageDTO(Image image) {
        this.id = image.getId();
        this.content = image.getContent();
    }
}
