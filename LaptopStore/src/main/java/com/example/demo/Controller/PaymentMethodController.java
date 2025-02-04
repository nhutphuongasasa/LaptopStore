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
            return ResponseEntity.ok(paymentMethods);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentMethodById(@PathVariable UUID id) {
            PaymentMethodDTO paymentMethod = paymentMethodService.getPaymentMethodById(id);
            return ResponseEntity.ok(paymentMethod);
    }


    @PostMapping
    public ResponseEntity<String> createPaymentMethod(@RequestBody PaymentMethodDTO paymentMethodDTO) {
            paymentMethodService.createPaymentMethod(paymentMethodDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Payment Method created successfully!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePaymentMethod(@PathVariable UUID id, @RequestBody PaymentMethodDTO paymentMethodDTO) {
            paymentMethodService.updatePaymentMethod(id, paymentMethodDTO);
            return ResponseEntity.ok("Payment Method updated successfully!"); // HTTP 200 (OK)
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePaymentMethod(@PathVariable UUID id) {
            paymentMethodService.deletePaymentMethod(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}