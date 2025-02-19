package com.example.demo.Service.Impl;

import com.example.demo.DTO.ImageDTO;
import com.example.demo.Models.Image;
import com.example.demo.Models.LaptopModel;
import com.example.demo.Repository.ImageRepository;
import com.example.demo.Repository.LaptopModelRepository;
import com.example.demo.Service.ImageService;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Transactional
@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final LaptopModelRepository laptopModelRepository;
    private final ModelMapper modelMapper;

    public ImageServiceImpl(ImageRepository imageRepository,
                            LaptopModelRepository laptopModelRepository,
                            ModelMapper modelMapper) {
        this.imageRepository = imageRepository;
        this.laptopModelRepository = laptopModelRepository;
        this.modelMapper = modelMapper;
    }

    // 1. Lấy danh sách tất cả Images
    @Override
    public List<ImageDTO> getAllImages() {
        return imageRepository.findAll().stream()
                .map(image -> {
                    ImageDTO imageDTO = modelMapper.map(image, ImageDTO.class);
                    imageDTO.setLaptopModelIds(image.getLaptopModelList() != null ?
                            image.getLaptopModelList().stream()
                            .map(LaptopModel::getId)
                            .collect(Collectors.toList()) : null);
                    return imageDTO;
                })
                .collect(Collectors.toList());
    }

    // 2. Lấy Image theo ID
    @Override
    public ImageDTO getImageById(UUID id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found with ID: " + id));
        ImageDTO imageDTO = modelMapper.map(image, ImageDTO.class);
        imageDTO.setLaptopModelIds(image.getLaptopModelList() != null ?
                image.getLaptopModelList().stream()
                .map(LaptopModel::getId)
                .collect(Collectors.toList()) : null);
        return imageDTO;
    }

    // 3. Tạo mới một Image
    @Override
    public ImageDTO createImage(ImageDTO imageDTO) {
        Image image = Image.builder()
                .id(null)
                .imageUrl(imageDTO.getImageUrl())
                .build();

        if (imageDTO.getLaptopModelIds() != null && !imageDTO.getLaptopModelIds().isEmpty()) {
            List<LaptopModel> laptopModels = imageDTO.getLaptopModelIds().stream()
                    .map(laptopModelId -> laptopModelRepository.findById(laptopModelId)
                            .orElseThrow(() -> new EntityNotFoundException("LaptopModel not found")))
                    .collect(Collectors.toList());
            image.setLaptopModelList(laptopModels);
        }else{
            throw  new IllegalArgumentException("LaptopModel cannot be null");
        }
        Image imageExisting = imageRepository.save(image);

        return convertToDTO(imageExisting);
    }

    @Override
    public ImageDTO updateImage(UUID imageId, ImageDTO imageDTO){
        Image imageExisting = imageRepository.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("Image not found"));
        imageExisting.setImageUrl(imageDTO.getImageUrl());

        if(imageDTO.getLaptopModelIds() == null){
            throw new IllegalArgumentException("LaptopModel cannot be null");
        }
        else{
            List<LaptopModel> laptopModels = imageDTO.getLaptopModelIds().stream()
                    .map(laptopModelId -> laptopModelRepository.findById(laptopModelId)
                            .orElseThrow(() -> new EntityNotFoundException("LaptopModel not found")))
                    .collect(Collectors.toList());
            imageExisting.setLaptopModelList(laptopModels);
        }

        Image image = imageRepository.save(imageExisting);
        return convertToDTO(image);
    }

    // 5. Xóa Image theo ID
    @Override
    public void deleteImage(UUID id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Image not found"));

        image.setLaptopModelList(null);

        imageRepository.delete(image);
    }

    private ImageDTO convertToDTO(Image image) {
        return ImageDTO.builder()
                .id(image.getId())
                .laptopModelIds(image.getLaptopModelList() == null ? null
                        : image.getLaptopModelList().stream()
                        .map(LaptopModel::getId)
                        .collect(Collectors.toList()))
                .imageUrl(image.getImageUrl())
                .build();
    }
}