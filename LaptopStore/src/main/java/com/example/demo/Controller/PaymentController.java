package com.example.demo.Controller;

import com.example.demo.DTO.PaymentDTO;
import com.example.demo.Service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // 1. Get all payments
    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        List<PaymentDTO> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments); // HTTP 200: OK
    }

    // 2. Get payment by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable UUID id) {

            PaymentDTO payment = paymentService.getPaymentById(id);
            return ResponseEntity.ok(payment);

    }

    // Create a new payment
    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody PaymentDTO paymentDTO) {
            paymentService.createPayment(paymentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Payment created successfully.");
    }

    // 4. Update payment by ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePayment(@PathVariable UUID id, @RequestBody PaymentDTO paymentDTO) {
            paymentService.updatePayment(id, paymentDTO);
            return ResponseEntity.ok("Payment updated successfully.");

    }

    // 5. Delete payment by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable UUID id) {

            paymentService.deletePayment(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}