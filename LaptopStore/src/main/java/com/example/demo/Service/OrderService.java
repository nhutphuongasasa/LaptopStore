package com.example.demo.Service;

import com.example.demo.DTO.OrderDTO;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<OrderDTO> getAllOrders(); // Lấy danh sách tất cả Orders

    OrderDTO getOrderById(UUID id); // Lấy Order theo ID

    void createOrder(OrderDTO orderDTO); // Tạo Order mới

    void updateOrder(UUID id, OrderDTO orderDTO); // Cập nhật Order

    void deleteOrder(UUID id); // Xóa Order theo ID
}