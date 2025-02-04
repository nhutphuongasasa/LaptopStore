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
        return ResponseEntity.ok(laptops);
    }

    // 2. Lấy Laptop chi tiết theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getLaptopById(@PathVariable UUID id) {
            LaptopDTO laptop = laptopService.getLaptopById(id);
            return ResponseEntity.ok(laptop);
    }

    // 3. Tạo mới Laptop
    @PostMapping
    public ResponseEntity<String> createLaptop(@RequestBody LaptopDTO laptopDTO) {
            laptopService.createLaptop(laptopDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Laptop created successfully!");
    }

    // 4. Cập nhật Laptop
    @PutMapping("/{id}")
    public ResponseEntity<String> updateLaptop(@PathVariable UUID id, @RequestBody LaptopDTO laptopDTO) {
            laptopService.updateLaptop(id, laptopDTO);
            return ResponseEntity.ok("Laptop updated successfully!");
    }

    // 5. Xóa Laptop
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLaptop(@PathVariable UUID id) {
            laptopService.deleteLaptop(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}