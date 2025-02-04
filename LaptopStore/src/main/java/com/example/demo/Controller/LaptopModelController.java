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

            List<LaptopModelDTO> laptopModels = laptopModelService.getAllLaptopModels();
            return ResponseEntity.ok(laptopModels);

    }

    // 2. Lấy LaptopModel theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getLaptopModelById(@PathVariable UUID id) {

            LaptopModelDTO laptopModel = laptopModelService.getLaptopModelById(id);
            return ResponseEntity.ok(laptopModel);

    }

    // 3. Tạo mới một LaptopModel
    @PostMapping
    public ResponseEntity<?> createLaptopModel(@RequestBody LaptopModelDTO laptopModelDTO) {

            laptopModelService.createLaptopModel(laptopModelDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("LaptopModel created successfully.");

    }

    // 4. Cập nhật một LaptopModel
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLaptopModel(@PathVariable UUID id, @RequestBody LaptopModelDTO laptopModelDTO) {

            laptopModelService.updateLaptopModel(id, laptopModelDTO);
            return ResponseEntity.ok("LaptopModel updated successfully.");

    }

    // 5. Xóa một LaptopModel
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLaptopModel(@PathVariable UUID id) {

            laptopModelService.deleteLaptopModel(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}