package com.example.demo.Service;

import com.example.demo.DTO.PaymentDTO;

import java.util.List;
import java.util.UUID;

public interface PaymentService {
    List<PaymentDTO> getAllPayments();
    PaymentDTO getPaymentById(UUID id);
    void createPayment(PaymentDTO paymentDTO);
    void updatePayment(UUID id, PaymentDTO paymentDTO);
    void deletePayment(UUID id);
}