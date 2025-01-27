package com.example.demo.Controller;

import com.example.demo.DTO.CartDTO;
import com.example.demo.Service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/carts") // Base URL cho Cart
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // 1. API: Lấy tất cả Cart
    @GetMapping
    public ResponseEntity<?> getAllCarts() {
        try {
            List<CartDTO> carts = cartService.getAllCarts();
            return ResponseEntity.ok(carts); // Trả về HTTP 200 cùng dữ liệu
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR) // Trả về HTTP 500
                    .body("Error fetching carts: " + e.getMessage());
        }
    }

    // 2. API: Lấy Cart theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCartById(@PathVariable("id") UUID id) {
        try {
            CartDTO cart = cartService.getCartById(id);
            return ResponseEntity.ok(cart); // Trả về HTTP 200
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND) // Trả về HTTP 404 nếu không tìm thấy
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR) // Trả về HTTP 500
                    .body("Error fetching cart: " + e.getMessage());
        }
    }

    // 3. API: Tạo mới Cart
    @PostMapping
    public ResponseEntity<?> createCart(@RequestBody CartDTO cartDTO) {
        try {
            cartService.createCart(cartDTO);
            return ResponseEntity
                    .status(HttpStatus.CREATED) // Trả về HTTP 201
                    .body("Cart created successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST) // Trả về HTTP 400 nếu dữ liệu không hợp lệ
                    .body("Error creating cart: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR) // Trả về HTTP 500
                    .body("Error creating cart: " + e.getMessage());
        }
    }

    // 4. API: Cập nhật Cart theo ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCart(@PathVariable("id") UUID id, @RequestBody CartDTO cartDTO) {
        try {
            cartService.updateCart(id, cartDTO);
            return ResponseEntity
                    .status(HttpStatus.OK) // Trả về HTTP 200
                    .body("Cart updated successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND) // Trả về HTTP 404 nếu không tìm thấy
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR) // Trả về HTTP 500
                    .body("Error updating cart: " + e.getMessage());
        }
    }

    // 5. API: Xóa Cart theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable("id") UUID id) {
        try {
            cartService.deleteCart(id);
            return ResponseEntity
                    .status(HttpStatus.OK) // Trả về HTTP 200
                    .body("Cart deleted successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND) // Trả về HTTP 404 nếu không tìm thấy
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR) // Trả về HTTP 500
                    .body("Error deleting cart: " + e.getMessage());
        }
    }
}