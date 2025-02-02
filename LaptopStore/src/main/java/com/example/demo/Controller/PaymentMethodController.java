package com.example.demo.Controller;

import com.example.demo.DTO.PaymentMethodDTO;
import com.example.demo.Service.PaymentMethodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payment-methods")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    // Constructor Injection
    public PaymentMethodController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @GetMapping
    public ResponseEntity<?> getAllPaymentMethods() {
            List<PaymentMethodDTO> paymentMethods = paymentMethodService.getAllPaymentMethods();
            return ResponseEntity.ok(paymentMethods); // HTTP 200 (OK)
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentMethodById(@PathVariable UUID id) {
        try {
            PaymentMethodDTO paymentMethod = paymentMethodService.getPaymentMethodById(id);
            return ResponseEntity.ok(paymentMethod); // HTTP 200 (OK)
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage()); // HTTP 404
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: Server encountered an issue."); // HTTP 500
        }
    }


    @PostMapping
    public ResponseEntity<String> createPaymentMethod(@RequestBody PaymentMethodDTO paymentMethodDTO) {
        try {
            paymentMethodService.createPaymentMethod(paymentMethodDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Payment Method created successfully!"); // HTTP 201 (Created)
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage()); // HTTP 400
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: Server encountered an issue."); // HTTP 500
        }
    }

    /**
     * Cập nhật phương thức thanh toán
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updatePaymentMethod(@PathVariable UUID id, @RequestBody PaymentMethodDTO paymentMethodDTO) {
        try {
            paymentMethodService.updatePaymentMethod(id, paymentMethodDTO);
            return ResponseEntity.ok("Payment Method updated successfully!"); // HTTP 200 (OK)
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage()); // HTTP 404
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: Server encountered an issue."); // HTTP 500
        }
    }

    /**
     * Xóa phương thức thanh toán
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePaymentMethod(@PathVariable UUID id) {
        try {
            paymentMethodService.deletePaymentMethod(id);
            return ResponseEntity.ok("Payment Method deleted successfully!"); // HTTP 200 (OK)
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage()); // HTTP 404
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: Unable to delete Payment Method. Please try again later."); // HTTP 500
        }
    }
}