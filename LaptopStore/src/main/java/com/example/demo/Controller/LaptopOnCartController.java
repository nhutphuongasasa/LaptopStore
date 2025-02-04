package com.example.demo.Controller;

import com.example.demo.DTO.LaptopOnCartDTO;
import com.example.demo.Service.LaptopOnCartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/laptop-on-carts")
public class LaptopOnCartController {

    private final LaptopOnCartService laptopOnCartService;

    public LaptopOnCartController(LaptopOnCartService laptopOnCartService) {
        this.laptopOnCartService = laptopOnCartService;
    }

    // 1. Lấy tất cả LaptopOnCart
    @GetMapping
    public ResponseEntity<List<LaptopOnCartDTO>> getAllLaptopOnCarts() {

            List<LaptopOnCartDTO> laptopOnCarts = laptopOnCartService.getAllLaptopOnCarts();
            return ResponseEntity.ok(laptopOnCarts);

    }

    // 2. Lấy LaptopOnCart theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getLaptopOnCartById(@PathVariable UUID id) {

            LaptopOnCartDTO laptopOnCart = laptopOnCartService.getLaptopOnCartById(id);
            return ResponseEntity.ok(laptopOnCart);

    }

    // 3. Tạo mới LaptopOnCart
    @PostMapping
    public ResponseEntity<String> createLaptopOnCart(@RequestBody LaptopOnCartDTO laptopOnCartDTO) {

            laptopOnCartService.createLaptopOnCart(laptopOnCartDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("LaptopOnCart created successfully!");

    }

    // 4. Cập nhật LaptopOnCart
    @PutMapping("/{id}")
    public ResponseEntity<String> updateLaptopOnCart(@PathVariable UUID id, @RequestBody LaptopOnCartDTO laptopOnCartDTO) {

            laptopOnCartService.updateLaptopOnCart(id, laptopOnCartDTO);
            return ResponseEntity.ok("LaptopOnCart updated successfully!");

    }

    // 5. Xóa LaptopOnCart
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLaptopOnCart(@PathVariable UUID id) {

            laptopOnCartService.deleteLaptopOnCart(id);
            return ResponseEntity.ok("LaptopOnCart deleted successfully!");

    }
}