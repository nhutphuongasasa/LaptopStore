package com.example.demo.Controller;

import com.example.demo.DTO.CartDTO;
import com.example.demo.Service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/carts") // Base URL cho Cart
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // 1. API: Lấy tất cả Cart
    @GetMapping
    public ResponseEntity<?> getAllCarts() {
            List<CartDTO> carts = cartService.getAllCarts();
            return ResponseEntity.ok(carts);
    }

    // Lấy Cart theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCartById(@PathVariable("id") UUID id) {
            CartDTO cart = cartService.getCartById(id);
            return ResponseEntity.ok(cart);
    }

    //  API: Tạo mới Cart
    @PostMapping
    public ResponseEntity<?> createCart(@RequestBody CartDTO cartDTO) {
            cartService.createCart(cartDTO);
            return ResponseEntity
                    .status(HttpStatus.CREATED) // Trả về HTTP 201
                    .body("Cart created successfully!");
    }

    //  Cập nhật Cart theo ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCart(@PathVariable("id") UUID id, @RequestBody CartDTO cartDTO) {
            cartService.updateCart(id, cartDTO);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Cart updated successfully!");
    }

    // 5. API: Xóa Cart theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable("id") UUID id) {
            cartService.deleteCart(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Cart deleted successfully!");
    }
}