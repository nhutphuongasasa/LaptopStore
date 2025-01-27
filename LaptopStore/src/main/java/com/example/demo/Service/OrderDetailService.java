package com.example.demo.Service;

import com.example.demo.DTO.OrderDetailDTO;

import java.util.List;
import java.util.UUID;

public interface OrderDetailService {
    List<OrderDetailDTO> getAllOrderDetails();                     // Lấy danh sách tất cả OrderDetail
    OrderDetailDTO getOrderDetailById(UUID id);                    // Lấy OrderDetail theo ID
    void createOrderDetail(OrderDetailDTO orderDetailDTO);         // Tạo OrderDetail mới
    void updateOrderDetail(UUID id, OrderDetailDTO orderDetailDTO);// Cập nhật OrderDetail
    void deleteOrderDetail(UUID id);                               // Xóa OrderDetail
}