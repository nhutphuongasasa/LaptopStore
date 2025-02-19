package com.example.demo.Service;

import com.example.demo.DTO.PaymentMethodDTO;

import java.util.List;
import java.util.UUID;

public interface PaymentMethodService {
    List<PaymentMethodDTO> getAllPaymentMethods();

    PaymentMethodDTO getPaymentMethodById(UUID id);

    PaymentMethodDTO createPaymentMethod(PaymentMethodDTO paymentMethodDTO);

    PaymentMethodDTO updatePaymentMethod(UUID id, PaymentMethodDTO paymentMethodDTO);

    void deletePaymentMethod(UUID id);
}
