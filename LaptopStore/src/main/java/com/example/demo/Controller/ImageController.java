package com.example.demo.Controller;

import com.example.demo.DTO.ImageDTO;
import com.example.demo.Service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/images") // Định nghĩa URL cơ bản cho các API liên quan đến Image
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    // 1. Lấy danh sách tất cả Images
    @GetMapping
    public ResponseEntity<List<ImageDTO>> getAllImages() {
        List<ImageDTO> images = imageService.getAllImages();
        return ResponseEntity.ok(images); // Trả về danh sách Image ở dạng ResponseEntity
    }

    // 2. Lấy Image theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ImageDTO> getImageById(@PathVariable UUID id) {
        ImageDTO imageDTO = imageService.getImageById(id);
        return ResponseEntity.ok(imageDTO); // Trả về chi tiết Image theo ID được yêu cầu
    }

    // 3. Tạo mới một Image
    @PostMapping
    public ResponseEntity<Void> createImage(@RequestBody ImageDTO imageDTO) {
        imageService.createImage(imageDTO); // Gọi Service để tạo Image mới
        return ResponseEntity.ok().build(); // Trả về trạng thái thành công
    }

    // 4. Gắn một Image hiện có vào danh sách LaptopModels
    @PostMapping("/{imageId}/attach")
    public ResponseEntity<Void> attachImageToLaptopModels(
            @PathVariable UUID imageId,
            @RequestBody List<UUID> laptopModelIds) {
        imageService.attachImageToLaptopModels(imageId, laptopModelIds); // Gọi Service thực hiện gắn Image
        return ResponseEntity.ok().build(); // Trả về trạng thái OK (200)
    }

    // 5. Xóa Image theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable UUID id) {
        imageService.deleteImage(id); // Gọi Service để xóa Image theo ID
        return ResponseEntity.noContent().build(); // Trả về trạng thái NO CONTENT (204)
    }
}