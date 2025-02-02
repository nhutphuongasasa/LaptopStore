package com.example.demo.Controller;

import com.example.demo.DTO.LaptopModelDTO;
import com.example.demo.Service.LaptopModelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/laptop-models")
public class LaptopModelController {

    private final LaptopModelService laptopModelService;

    public LaptopModelController(LaptopModelService laptopModelService) {
        this.laptopModelService = laptopModelService;
    }

    // 1. Lấy danh sách tất cả LaptopModels
    @GetMapping
    public ResponseEntity<?> getAllLaptopModels() {
        try {
            List<LaptopModelDTO> laptopModels = laptopModelService.getAllLaptopModels();
            return ResponseEntity.ok(laptopModels);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while fetching laptop models: " + e.getMessage());
        }
    }

    // 2. Lấy LaptopModel theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getLaptopModelById(@PathVariable UUID id) {
        try {
            LaptopModelDTO laptopModel = laptopModelService.getLaptopModelById(id);
            return ResponseEntity.ok(laptopModel);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("LaptopModel with ID " + id + " not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching LaptopModel by ID: " + e.getMessage());
        }
    }

    // 3. Tạo mới một LaptopModel
    @PostMapping
    public ResponseEntity<?> createLaptopModel(@RequestBody LaptopModelDTO laptopModelDTO) {
        try {
            laptopModelService.createLaptopModel(laptopModelDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("LaptopModel created successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error creating LaptopModel: " + e.getMessage());
        }
    }

    // 4. Cập nhật một LaptopModel
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLaptopModel(@PathVariable UUID id, @RequestBody LaptopModelDTO laptopModelDTO) {
        try {
            laptopModelService.updateLaptopModel(id, laptopModelDTO);
            return ResponseEntity.ok("LaptopModel updated successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("LaptopModel with ID " + id + " not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error updating LaptopModel: " + e.getMessage());
        }
    }

    // 5. Xóa một LaptopModel
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLaptopModel(@PathVariable UUID id) {
        try {
            laptopModelService.deleteLaptopModel(id);
            return ResponseEntity.ok("LaptopModel deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("LaptopModel with ID " + id + " not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting LaptopModel: " + e.getMessage());
        }
    }
}