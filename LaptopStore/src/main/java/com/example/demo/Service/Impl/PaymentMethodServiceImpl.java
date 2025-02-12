package com.example.demo.Service.Impl;

import com.example.demo.Common.Enums;
import com.example.demo.Common.JsonConverter;
import com.example.demo.DTO.PaymentMethodDTO;
import com.example.demo.DTO.SaleDTO;
import com.example.demo.Models.LaptopModel;
import com.example.demo.Models.PaymentMethod;
import com.example.demo.Models.Sale;
import com.example.demo.Repository.PaymentMethodRepository;
import com.example.demo.Service.PaymentMethodService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodServiceImpl(PaymentMethodRepository paymentMethodRepository, ModelMapper modelMapper) {
        this.paymentMethodRepository = paymentMethodRepository;
    }


    // Lấy danh sách tất cả PaymentMethod
    @Override
    public List<PaymentMethodDTO> getAllPaymentMethods() {
        return paymentMethodRepository.findAll().stream()
                .map(paymentMethod -> {

                    Map<String, Object> dataMap = paymentMethod.getData();

                    return PaymentMethodDTO.builder()
                            .id(paymentMethod.getId())
                            .data(dataMap)
                            .type(paymentMethod.getType())
                            .build();
                })
                .collect(Collectors.toList());
    }

    // Lấy chi tiết PaymentMethod theo ID
    @Override
    public PaymentMethodDTO getPaymentMethodById(UUID id) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PaymentMethod with ID " + id + " not found!"));

        Map<String, Object> dataMap = paymentMethod.getData();

        return PaymentMethodDTO.builder()
                .id(paymentMethod.getId())
                .data(dataMap)
                .type(paymentMethod.getType())
                .build();
    }


    // Tạo mới PaymentMethod
    @Override
    public PaymentMethodDTO createPaymentMethod(PaymentMethodDTO paymentMethodDTO) {

        PaymentMethod paymentMethod = PaymentMethod.builder()
                .id(null)
                .data(paymentMethodDTO.getData())
                .type(paymentMethodDTO.getType())
                .build();

        PaymentMethod paymentMethodExisting = paymentMethodRepository.save(paymentMethod);

        return convertToDTO(paymentMethodExisting);
    }

    // Cập nhật PaymentMethod
    @Override
    public PaymentMethodDTO updatePaymentMethod(UUID id, PaymentMethodDTO paymentMethodDTO) {
        PaymentMethod existingPaymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PaymentMethod with ID " + id + " not found!"));

        // Cập nhật các thuộc tính của PaymentMethod
        existingPaymentMethod.setData(paymentMethodDTO.getData());
        existingPaymentMethod.setId(id);
        existingPaymentMethod.setType(Enums.PaymentType.valueOf(paymentMethodDTO.getType().name()));

        PaymentMethod paymentMethodExisting = paymentMethodRepository.save(existingPaymentMethod);
        return convertToDTO(paymentMethodExisting);
    }

    // Xóa PaymentMethod theo ID
    @Override
    public void deletePaymentMethod(UUID id) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PaymentMethod with ID " + id + " not found!"));
        paymentMethod.getPaymentList().clear();
        paymentMethodRepository.delete(paymentMethod);
    }

    private PaymentMethodDTO convertToDTO(PaymentMethod paymentMethod) {
        return PaymentMethodDTO.builder()
                .id(paymentMethod.getId())
                .type(paymentMethod.getType())
                .data(paymentMethod.getData())
                .build();
    }
}