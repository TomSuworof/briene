package com.salat.briene.services;

import com.salat.briene.entities.Image;
import com.salat.briene.payload.response.ImageDTO;
import com.salat.briene.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageDTO getImage(UUID id) {
        Image image = imageRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        return new ImageDTO(image);
    }

    public ImageDTO uploadImage(ImageDTO imageDTO) {
        Image image = new Image(imageDTO.getContent());
        Image uploaded = imageRepository.save(image);
        return new ImageDTO(uploaded);
    }
}
