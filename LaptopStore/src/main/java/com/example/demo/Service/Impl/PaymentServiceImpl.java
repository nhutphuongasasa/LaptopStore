package com.example.demo.Service.Impl;

import com.example.demo.Common.Enums;
import com.example.demo.DTO.PaymentDTO;
import com.example.demo.Models.*;
import com.example.demo.Repository.*;
import com.example.demo.Service.PaymentService;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
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
    public void createPayment(PaymentDTO paymentDTO) {
        Account accountCustomer = accountRepository.findById(paymentDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer with ID " + paymentDTO.getCustomerId() + " not found!"));
        // Validate Customer
        Customer customer = customerRepository.findById(accountCustomer.getId())
                .orElseThrow(() -> new RuntimeException("Customer with ID " + paymentDTO.getCustomerId() + " not found!"));

        // Validate Order
        Order order = orderRepository.findById(paymentDTO.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order with ID " + paymentDTO.getOrderId() + " not found!"));

        // Validate PaymentMethod
        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentDTO.getPaymentMethodId())
                .orElseThrow(() -> new RuntimeException("Payment Method with ID " + paymentDTO.getPaymentMethodId() + " not found!"));

        // Map DTO to Entity and Set Relations
        Payment payment = modelMapper.map(paymentDTO, Payment.class);
        payment.setCustomer(customer);
        payment.setOrder(order);
        payment.setPaymentMethod(paymentMethod);

        // Save Payment
        paymentRepository.save(payment);
    }

    @Override
    public void updatePayment(UUID id, PaymentDTO paymentDTO) {
        // Find Existing Payment
        Payment existingPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment with ID " + id + " not found!"));

        Account accountCustomer = accountRepository.findById(paymentDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer with ID " + paymentDTO.getCustomerId() + " not found!"));

        // Validate and Update Customer
        Customer customer = customerRepository.findById(accountCustomer.getId())
                .orElseThrow(() -> new RuntimeException("Customer with ID " + paymentDTO.getCustomerId() + " not found!"));
        existingPayment.setCustomer(customer);

        // Validate and Update Order
        Order order = orderRepository.findById(paymentDTO.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order with ID " + paymentDTO.getOrderId() + " not found!"));
        existingPayment.setOrder(order);

        // Validate and Update PaymentMethod
        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentDTO.getPaymentMethodId())
                .orElseThrow(() -> new RuntimeException("Payment Method with ID " + paymentDTO.getPaymentMethodId() + " not found!"));
        existingPayment.setPaymentMethod(paymentMethod);

        // Update Type and Status
        existingPayment.setType(paymentDTO.getType());
        existingPayment.setStatus(paymentDTO.getStatus());

        // Save Updated Payment
        paymentRepository.save(existingPayment);
    }

    @Override
    public void deletePayment(UUID id) {
        Payment existingPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment with ID " + id + " not found!"));
        existingPayment.setCustomer(null);
        existingPayment.setOrder(null);
        existingPayment.setPaymentMethod(null);
        paymentRepository.save(existingPayment);
        paymentRepository.delete(existingPayment);
    }
}