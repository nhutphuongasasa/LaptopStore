package com.example.demo.Service;

import com.example.demo.DTO.CartDTO;

import java.util.List;
import java.util.UUID;

public interface CartService {

    List<CartDTO> getAllCarts(); // Lấy tất cả Cart

    CartDTO getCartById(UUID id); // Lấy Cart theo ID

    void createCart(CartDTO cartDTO); // Tạo mới Cart

    void updateCart(UUID id, CartDTO cartDTO); // Cập nhật Cart theo ID

    void deleteCart(UUID id); // Xóa Cart theo ID
}