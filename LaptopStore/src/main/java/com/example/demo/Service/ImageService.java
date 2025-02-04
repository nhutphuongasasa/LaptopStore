package com.example.demo.Service;

import com.example.demo.DTO.ImageDTO;

import java.util.List;
import java.util.UUID;

public interface ImageService {
    List<ImageDTO> getAllImages();
    ImageDTO getImageById(UUID id);
    void createImage(ImageDTO imageDTO);
    void updateImage(UUID imageId, ImageDTO imageDTO);
    void deleteImage(UUID id);
}
