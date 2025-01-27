package com.example.demo.Service;

import com.example.demo.DTO.ImageDTO;

import java.util.List;
import java.util.UUID;

public interface ImageService {
    List<ImageDTO> getAllImages();
    ImageDTO getImageById(UUID id);
    void createImage(ImageDTO imageDTO);
    void attachImageToLaptopModels(UUID imageId, List<UUID> laptopModelIds);
    void deleteImage(UUID id);
}
