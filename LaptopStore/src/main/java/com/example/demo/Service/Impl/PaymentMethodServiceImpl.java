package com.example.demo.Service.Impl;

import com.example.demo.Common.Enums;
import com.example.demo.DTO.PaymentMethodDTO;
import com.example.demo.Models.PaymentMethod;
import com.example.demo.Repository.PaymentMethodRepository;
import com.example.demo.Service.PaymentMethodService;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final ModelMapper modelMapper;

    public PaymentMethodServiceImpl(PaymentMethodRepository paymentMethodRepository, ModelMapper modelMapper) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.modelMapper = modelMapper;
    }

    // Lấy danh sách tất cả PaymentMethod
    @Override
    public List<PaymentMethodDTO> getAllPaymentMethods() {
        return paymentMethodRepository.findAll().stream()
                .map(paymentMethod -> modelMapper.map(paymentMethod, PaymentMethodDTO.class))
                .collect(Collectors.toList());
    }

    // Lấy chi tiết PaymentMethod theo ID
    @Override
    public PaymentMethodDTO getPaymentMethodById(UUID id) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PaymentMethod with ID " + id + " not found!"));
        return modelMapper.map(paymentMethod, PaymentMethodDTO.class);
    }

    // Tạo mới PaymentMethod
    @Override
    public void createPaymentMethod(PaymentMethodDTO paymentMethodDTO) {
        PaymentMethod paymentMethod = modelMapper.map(paymentMethodDTO, PaymentMethod.class);
        paymentMethodRepository.save(paymentMethod);
    }

    // Cập nhật PaymentMethod
    @Override
    public void updatePaymentMethod(UUID id, PaymentMethodDTO paymentMethodDTO) {
        PaymentMethod existingPaymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PaymentMethod with ID " + id + " not found!"));

        // Cập nhật các thuộc tính của PaymentMethod
        existingPaymentMethod.setData(paymentMethodDTO.getData());
        existingPaymentMethod.setType(Enums.PaymentType.valueOf(paymentMethodDTO.getType()));

        paymentMethodRepository.save(existingPaymentMethod);
    }

    // Xóa PaymentMethod theo ID
    @Override
    public void deletePaymentMethod(UUID id) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PaymentMethod with ID " + id + " not found!"));
        paymentMethod.getPaymentList().clear();
        paymentMethodRepository.delete(paymentMethod);
    }
}