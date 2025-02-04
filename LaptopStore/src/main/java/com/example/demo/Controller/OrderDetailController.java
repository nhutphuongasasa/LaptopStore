package com.example.demo.Controller;

import com.example.demo.DTO.OrderDetailDTO;
import com.example.demo.Service.OrderDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order-details")
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    public OrderDetailController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    // Get all order details
    @GetMapping
    public ResponseEntity<List<OrderDetailDTO>> getAllOrderDetails() {
        List<OrderDetailDTO> orderDetails = orderDetailService.getAllOrderDetails();
        return ResponseEntity.ok(orderDetails);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailDTO> getOrderDetailById(@PathVariable UUID id) {
        OrderDetailDTO orderDetail = orderDetailService.getOrderDetailById(id);
        return ResponseEntity.ok(orderDetail);
    }

    // Create new order detail
    @PostMapping
    public ResponseEntity<?> createOrderDetail(@RequestBody OrderDetailDTO orderDetailDTO) {
        orderDetailService.createOrderDetail(orderDetailDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Order created successfully!");
    }

    // Update order detail by ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@PathVariable UUID id, @RequestBody OrderDetailDTO orderDetailDTO) {
        orderDetailService.updateOrderDetail(id, orderDetailDTO);
        return ResponseEntity.ok("Order updated successfully");
    }

    // Delete order detail by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderDetail(@PathVariable UUID id) {
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}