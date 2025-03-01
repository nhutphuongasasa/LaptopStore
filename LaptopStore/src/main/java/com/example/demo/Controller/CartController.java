package com.example.demo.Controller;

import com.example.demo.Common.DataResponse;
import com.example.demo.DTO.AddressDTO;
import com.example.demo.DTO.CartDTO;
import com.example.demo.Service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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
    public ResponseEntity<DataResponse<List<CartDTO>>> getAllCarts() {
            List<CartDTO> carts = cartService.getAllCarts();
            return ResponseEntity.ok(DataResponse.<List<CartDTO>>builder()
                    .success(true)
                    .message("Cart retrieved successfully")
                    .data(cartService.getAllCarts())
                    .build());
    }

    // Lấy Cart theo ID
    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<CartDTO>> getCartById(@PathVariable("id") UUID id) {
            return ResponseEntity.ok(DataResponse.<CartDTO>builder()
                    .success(true)
                    .message("Addresses retrieved successfully")
                    .data(cartService.getCartById(id))
                    .build());
    }

    //  API: Tạo mới Cart
    @PostMapping
    public ResponseEntity<?> createCart(@RequestBody CartDTO cartDTO) {

        return ResponseEntity.ok(DataResponse.<CartDTO>builder()
                .success(true)
                .message("Addresses created successfully")
                .data(cartService.createCart(cartDTO))
                .build());
    }

    //  Cập nhật Cart theo ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCart(@PathVariable("id") UUID id, @RequestBody CartDTO cartDTO) {
        return ResponseEntity.ok(DataResponse.<CartDTO>builder()
                .success(true)
                .message("Addresses updated successfully")
                .data(cartService.updateCart(id, cartDTO))
                .build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DataResponse<CartDTO>> partialUpdateCart(
            @PathVariable UUID id,
            @RequestBody Map<String, Object> fieldsToUpdate) {

        CartDTO updatedCart = cartService.partialUpdateCart(id, fieldsToUpdate);

        return ResponseEntity.ok(DataResponse.<CartDTO>builder()
                .success(true)
                .message("Cart updated successfully!")
                .data(updatedCart)
                .build());
    }


    // 5. API: Xóa Cart theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable("id") UUID id) {
            cartService.deleteCart(id);
            return ResponseEntity.ok(DataResponse.builder()
                    .success(true)
                    .message("Addresses deleted successfully")
                    .build());
    }
}