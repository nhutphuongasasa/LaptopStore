package com.example.demo.Controller;

import com.example.demo.DTO.OrderDTO;
import com.example.demo.Service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders() {
            List<OrderDTO> orders = orderService.getAllOrders();
            return ResponseEntity.ok(orders);
    }

    // Fetch Order theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable UUID id) {
            OrderDTO orderDTO = orderService.getOrderById(id);
            return ResponseEntity.ok(orderDTO);
    }

    // Tạo mới một Order
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderDTO) {
            orderService.createOrder(orderDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Order created successfully!");
    }

    // Cập nhật Order theo ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable UUID id, @RequestBody OrderDTO orderDTO) {
            orderService.updateOrder(id, orderDTO);
            return ResponseEntity.ok("Order updated successfully!");
    }

    // Xóa Order theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable UUID id) {
            orderService.deleteOrder(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}