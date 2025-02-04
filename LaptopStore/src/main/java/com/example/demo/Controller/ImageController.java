package com.example.demo.Controller;

import com.example.demo.DTO.ImageDTO;
import com.example.demo.Service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
        return ResponseEntity.ok(images);
    }

    // 2. Lấy Image theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ImageDTO> getImageById(@PathVariable UUID id) {
        ImageDTO imageDTO = imageService.getImageById(id);
        return ResponseEntity.ok(imageDTO);
    }

    // 3. Tạo mới một Image
    @PostMapping
    public ResponseEntity<?> createImage(@RequestBody ImageDTO imageDTO) {
        imageService.createImage(imageDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Image created successfully!");
    }

    @PutMapping("/{id}")
    public  ResponseEntity<?> updateImage(@PathVariable UUID id,@RequestBody ImageDTO imageDTO){
        imageService.updateImage(id,imageDTO);
        return ResponseEntity.ok("Image updated successfully!");
    }

    // 5. Xóa Image theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable UUID id) {
        imageService.deleteImage(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}