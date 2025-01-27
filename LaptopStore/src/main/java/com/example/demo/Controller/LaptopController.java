package com.example.demo.Controller;

import com.example.demo.DTO.LaptopDTO;
import com.example.demo.Service.LaptopService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/laptops")
public class LaptopController {

    private final LaptopService laptopService;

    public LaptopController(LaptopService laptopService) {
        this.laptopService = laptopService;
    }

    // 1. Lấy tất cả Laptop
    @GetMapping
    public ResponseEntity<List<LaptopDTO>> getAllLaptops() {
        List<LaptopDTO> laptops = laptopService.getAllLaptops();
        return ResponseEntity.ok(laptops); // HTTP 200
    }

    // 2. Lấy Laptop chi tiết theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getLaptopById(@PathVariable UUID id) {
        try {
            LaptopDTO laptop = laptopService.getLaptopById(id);
            return ResponseEntity.ok(laptop); // HTTP 200
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage()); // HTTP 404 + Message
        }
    }

    // 3. Tạo mới Laptop
    @PostMapping
    public ResponseEntity<String> createLaptop(@RequestBody LaptopDTO laptopDTO) {
        try {
            laptopService.createLaptop(laptopDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Laptop created successfully!"); // HTTP 201
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage()); // HTTP 400 + Message
        }
    }

    // 4. Cập nhật Laptop
    @PutMapping("/{id}")
    public ResponseEntity<String> updateLaptop(@PathVariable UUID id, @RequestBody LaptopDTO laptopDTO) {
        try {
            laptopService.updateLaptop(id, laptopDTO);
            return ResponseEntity.ok("Laptop updated successfully!"); // HTTP 200
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage()); // HTTP 404
        }
    }

    // 5. Xóa Laptop
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLaptop(@PathVariable UUID id) {
        try {
            laptopService.deleteLaptop(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("Laptop deleted successfully!"); // HTTP 204 (No Content with message)
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage()); // HTTP 404 + Message
        }
    }
}