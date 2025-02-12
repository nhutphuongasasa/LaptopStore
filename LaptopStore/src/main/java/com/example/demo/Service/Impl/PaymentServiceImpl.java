package com.example.demo.Service.Impl;

import com.example.demo.Common.Enums;
import com.example.demo.DTO.PaymentDTO;
import com.example.demo.DTO.SaleDTO;
import com.example.demo.Models.*;
import com.example.demo.Repository.*;
import com.example.demo.Service.PaymentService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;

    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            CustomerRepository customerRepository,
            OrderRepository orderRepository,
            PaymentMethodRepository paymentMethodRepository,
            ModelMapper modelMapper,
            AccountRepository accountRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.modelMapper = modelMapper;
        this.accountRepository = accountRepository;
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(payment -> PaymentDTO.builder()
                        .id(payment.getId())
                        .customerId(payment.getCustomer() != null ? payment.getCustomer().getCustomerId().getId() : null)
                        .orderId(payment.getOrder() != null ? payment.getOrder().getId() : null)
                        .paymentMethodId(payment.getPaymentMethod() != null ? payment.getPaymentMethod().getId() : null)
                        .type(payment.getType())
                        .status(payment.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public PaymentDTO getPaymentById(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment with ID " + id + " not found!"));

        return PaymentDTO.builder()
                .id(payment.getId())
                .customerId(payment.getCustomer() != null ? payment.getCustomer().getCustomerId().getId() : null)
                .orderId(payment.getOrder() != null ? payment.getOrder().getId() : null)
                .paymentMethodId(payment.getPaymentMethod() != null ? payment.getPaymentMethod().getId() : null)
                .type(payment.getType())
                .status(payment.getStatus())
                .build();
    }

    @Override
    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        Customer customer = customerRepository.findById(paymentDTO.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found!"));

        Order order = orderRepository.findById(paymentDTO.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found!"));

        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentDTO.getPaymentMethodId())
                .orElseThrow(() -> new EntityNotFoundException("Payment not found!"));

        Payment payment = Payment.builder()
                .id(null)
                .type(paymentDTO.getType())
                .status(paymentDTO.getStatus())
                .paymentMethod(paymentMethod)
                .customer(customer)
                .order(order)
                .build();

        Payment paymentExisting = paymentRepository.save(payment);

        return convertToDTO(paymentExisting);
    }

    @Override
    public PaymentDTO updatePayment(UUID id, PaymentDTO paymentDTO) {
        Payment existingPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found!"));

        Customer customer = customerRepository.findById(paymentDTO.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found!"));
        existingPayment.setCustomer(customer);

        Order order = orderRepository.findById(paymentDTO.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found!"));
        existingPayment.setOrder(order);

        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentDTO.getPaymentMethodId())
                .orElseThrow(() -> new EntityNotFoundException("Payment Method not found!"));

        existingPayment.setCustomer(customer);
        existingPayment.setOrder(order);
        existingPayment.setPaymentMethod(paymentMethod);
        existingPayment.setType(paymentDTO.getType());
        existingPayment.setStatus(paymentDTO.getStatus());

        Payment payment = paymentRepository.save(existingPayment);

        return convertToDTO(payment);
    }

    @Override
    public void deletePayment(UUID id) {
        Payment existingPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found!"));

        paymentRepository.delete(existingPayment);
    }

    private PaymentDTO convertToDTO(Payment payment) {
        return PaymentDTO.builder()
                .id(payment.getId())
                .customerId(payment.getCustomer() != null ? payment.getCustomer().getCustomerId().getId() : null)
                .orderId(payment.getOrder() != null ? payment.getOrder().getId() : null)
                .paymentMethodId(payment.getPaymentMethod() != null ? payment.getPaymentMethod().getId() : null)
                .type(payment.getType())
                .status(payment.getStatus())
                .build();
    }
}