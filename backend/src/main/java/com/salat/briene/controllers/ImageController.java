// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.salat.briene.controllers;

import com.salat.briene.payload.response.ImageDTO;
import com.salat.briene.services.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.beans.Transient;
import java.util.Base64;
import java.util.UUID;

@Log4j2
@Controller
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping(value = "/get/{imageId}", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Transient
    public ResponseEntity<byte[]> getImage(@PathVariable UUID imageId) {
        log.debug("getImage() called. ID: {}", imageId);
        ImageDTO image = imageService.getImage(imageId);
        log.trace("getImage(). Response to send: {}", () -> image);
        String response = image.getContent().substring("data:image/png;base64,".length());
        return ResponseEntity.ok().body(Base64.getDecoder().decode(response));
    }

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    @Transient
    public @ResponseBody ResponseEntity<ImageDTO> uploadImage(@RequestBody ImageDTO image) {
        log.debug("uploadImage() called. Image: {}", () -> image);
        ImageDTO imageResult = imageService.uploadImage(image);
        log.trace("uploadImage(). Response to send: {}", () -> image);
        return ResponseEntity.status(HttpStatus.CREATED).body(imageResult);
    }
}
