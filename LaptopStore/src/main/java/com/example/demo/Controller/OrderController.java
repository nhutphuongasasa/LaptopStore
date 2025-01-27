package com.example.demo.Controller;

import com.example.demo.DTO.OrderDTO;
import com.example.demo.Service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders") // Định tuyến cơ bản cho Order API
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        try {
            List<OrderDTO> orders = orderService.getAllOrders();
            return ResponseEntity.ok(orders); // Trả về 200 OK + danh sách Orders
        } catch (Exception e) {
            // Ghi log lỗi hoặc xử lý lỗi bổ sung tại đây nếu cần
            String errorMessage = "Failed to fetch orders: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage); // Trả về 500 INTERNAL_SERVER_ERROR
        }
    }

    // Fetch Order theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable UUID id) {
        try {
            OrderDTO orderDTO = orderService.getOrderById(id);
            return ResponseEntity.ok(orderDTO); // Trả về 200 OK + Order
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage()); // Trả về 404 + thông báo lỗi
        }
    }

    // Tạo mới một Order
    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderDTO orderDTO) {
        try {
            orderService.createOrder(orderDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Order created successfully!"); // Trả về 201 Created + thông báo
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage()); // Trả về 400 Bad Request + thông báo lỗi
        }
    }

    // Cập nhật Order theo ID
    @PutMapping("/{id}")
    public ResponseEntity<String> updateOrder(@PathVariable UUID id, @RequestBody OrderDTO orderDTO) {
        try {
            orderService.updateOrder(id, orderDTO);
            return ResponseEntity.ok("Order updated successfully!"); // Trả về 200 OK + thông báo
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage()); // Trả về 404 + thông báo lỗi
        }
    }

    // Xóa Order theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable UUID id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.ok("Order deleted successfully!"); // Trả về 200 OK + thông báo
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage()); // Trả về 404 + thông báo lỗi
        }
    }
}