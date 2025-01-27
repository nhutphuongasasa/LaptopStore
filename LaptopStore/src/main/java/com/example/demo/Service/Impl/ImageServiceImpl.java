package com.example.demo.Service.Impl;

import com.example.demo.DTO.ImageDTO;
import com.example.demo.Models.Image;
import com.example.demo.Models.LaptopModel;
import com.example.demo.Repository.ImageRepository;
import com.example.demo.Repository.LaptopModelRepository;
import com.example.demo.Service.ImageService;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
                    imageDTO.setLaptopModelIds(image.getLaptopModelList().stream()
                            .map(LaptopModel::getId)
                            .collect(Collectors.toList()));
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
        imageDTO.setLaptopModelIds(image.getLaptopModelList().stream()
                .map(LaptopModel::getId)
                .collect(Collectors.toList()));
        return imageDTO;
    }

    // 3. Tạo mới một Image
    @Override
    public void createImage(ImageDTO imageDTO) {
        Image image = new Image();
        image.setImage_url(imageDTO.getImageUrl()); // Gán URL từ DTO

        // Danh sách LaptopModels có thể trống khi tạo Image
        if (imageDTO.getLaptopModelIds() != null && !imageDTO.getLaptopModelIds().isEmpty()) {
            List<LaptopModel> laptopModels = imageDTO.getLaptopModelIds().stream()
                    .map(laptopModelId -> laptopModelRepository.findById(laptopModelId)
                            .orElseThrow(() -> new RuntimeException("LaptopModel not found with ID: " + laptopModelId)))
                    .collect(Collectors.toList());
            image.setLaptopModelList(laptopModels);
        }
        imageRepository.save(image); // Lưu Image vào cơ sở dữ liệu
    }

    // 4. Gắn một Image hiện có vào danh sách LaptopModels
    @Override
    public void attachImageToLaptopModels(UUID imageId, List<UUID> laptopModelIds) {
        // Tìm Image trong database
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found with ID: " + imageId));

        // Tìm các LaptopModels trong database
        List<LaptopModel> laptopModels = laptopModelIds.stream()
                .map(laptopModelId -> laptopModelRepository.findById(laptopModelId)
                        .orElseThrow(() -> new RuntimeException("LaptopModel not found with ID: " + laptopModelId)))
                .collect(Collectors.toList());

        // Gán danh sách LaptopModels vào Image
        image.getLaptopModelList().addAll(laptopModels);
        imageRepository.save(image); // Lưu thay đổi vào cơ sở dữ liệu
    }

    // 5. Xóa Image theo ID
    @Override
    public void deleteImage(UUID id) {
        // Kiểm tra xem Image có tồn tại không
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found with ID: " + id));

        // Xóa mối quan hệ giữa Image và các LaptopModel liên kết
        List<LaptopModel> laptopModels = image.getLaptopModelList();

        for (LaptopModel laptopModel : laptopModels) {
            laptopModel.getImageList().remove(image); // Loại bỏ Image khỏi ImageList trong LaptopModel
        }

        // Loại bỏ tất cả các LaptopModel khỏi Image (giờ Image không liên kết với bất kỳ LaptopModel nào)
        image.getLaptopModelList().clear();
        imageRepository.save(image); // Lưu trạng thái mới của Image

        // Xóa Image sau khi mối quan hệ đã được làm sạch
        imageRepository.delete(image);
    }
}