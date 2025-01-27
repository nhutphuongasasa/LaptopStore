package com.example.demo.Controller;

import com.example.demo.DTO.LaptopOnCartDTO;
import com.example.demo.Service.LaptopOnCartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/laptop-on-carts")
public class LaptopOnCartController {

    private final LaptopOnCartService laptopOnCartService;

    public LaptopOnCartController(LaptopOnCartService laptopOnCartService) {
        this.laptopOnCartService = laptopOnCartService;
    }

    // 1. Lấy tất cả LaptopOnCart
    @GetMapping
    public ResponseEntity<List<LaptopOnCartDTO>> getAllLaptopOnCarts() {
        try {
            List<LaptopOnCartDTO> laptopOnCarts = laptopOnCartService.getAllLaptopOnCarts();
            return ResponseEntity.ok(laptopOnCarts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // 2. Lấy LaptopOnCart theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getLaptopOnCartById(@PathVariable UUID id) {
        try {
            LaptopOnCartDTO laptopOnCart = laptopOnCartService.getLaptopOnCartById(id);
            return ResponseEntity.ok(laptopOnCart);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    // 3. Tạo mới LaptopOnCart
    @PostMapping
    public ResponseEntity<String> createLaptopOnCart(@RequestBody LaptopOnCartDTO laptopOnCartDTO) {
        try {
            laptopOnCartService.createLaptopOnCart(laptopOnCartDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("LaptopOnCart created successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    // 4. Cập nhật LaptopOnCart
    @PutMapping("/{id}")
    public ResponseEntity<String> updateLaptopOnCart(@PathVariable UUID id, @RequestBody LaptopOnCartDTO laptopOnCartDTO) {
        try {
            laptopOnCartService.updateLaptopOnCart(id, laptopOnCartDTO);
            return ResponseEntity.ok("LaptopOnCart updated successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    // 5. Xóa LaptopOnCart
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLaptopOnCart(@PathVariable UUID id) {
        try {
            laptopOnCartService.deleteLaptopOnCart(id);
            return ResponseEntity.ok("LaptopOnCart deleted successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
}