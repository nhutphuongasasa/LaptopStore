package com.example.demo.Controller;

import com.example.demo.DTO.OrderDetailDTO;
import com.example.demo.Service.OrderDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order-details") // Base URL
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    public OrderDetailController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    // Get all order details
    @GetMapping
    public ResponseEntity<List<OrderDetailDTO>> getAllOrderDetails() {
        List<OrderDetailDTO> orderDetails = orderDetailService.getAllOrderDetails();
        return ResponseEntity.ok(orderDetails); // HTTP 200
    }

    // Get order detail by ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailDTO> getOrderDetailById(@PathVariable UUID id) {
        OrderDetailDTO orderDetail = orderDetailService.getOrderDetailById(id);
        return ResponseEntity.ok(orderDetail); // HTTP 200
    }

    // Create new order detail
    @PostMapping
    public ResponseEntity<Void> createOrderDetail(@RequestBody OrderDetailDTO orderDetailDTO) {
        orderDetailService.createOrderDetail(orderDetailDTO);
        return ResponseEntity.status(201).build(); // HTTP 201 - Created
    }

    // Update order detail by ID
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateOrderDetail(@PathVariable UUID id, @RequestBody OrderDetailDTO orderDetailDTO) {
        orderDetailService.updateOrderDetail(id, orderDetailDTO);
        return ResponseEntity.ok().build(); // HTTP 200
    }

    // Delete order detail by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderDetail(@PathVariable UUID id) {
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.noContent().build(); // HTTP 204 - No Content
    }
}