package com.example.demo.Service.Impl;

import com.example.demo.Common.Enums;
import com.example.demo.DTO.OrderDTO;
import com.example.demo.Models.*;
import com.example.demo.Repository.*;
import com.example.demo.Service.OrderService;
import jakarta.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service

public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository; // Repository để kiểm tra Customer tồn tại
    private final OrderDetailRepository orderDetailRepository;
    private final PaymentRepository paymentRepository;

    public OrderServiceImpl(PaymentRepository paymentRepository,OrderRepository orderRepository, CustomerRepository customerRepository,OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.orderDetailRepository=orderDetailRepository;
        this.paymentRepository=paymentRepository;
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> OrderDTO.builder()
                        .id(order.getId())
                        .customerId(order.getCustomer().getId())
                        .dateCreate(order.getDateCreate())
                        .status(order.getStatus())
                        .OrderDetails(order.getOrderDetailList() == null
                                ? Collections.emptyList()
                                : order.getOrderDetailList().stream()
                                .map(OrderDetail::getId)
                                .collect(Collectors.toList()))
                        .Payments(order.getPaymentList() == null
                                ? Collections.emptyList()
                                : order.getPaymentList().stream()
                                .map(Payment::getId)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }


    @Override
    public OrderDTO getOrderById(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found!"));

        return OrderDTO.builder()
                .id(order.getId())
                .customerId(order.getCustomer().getId())
                .dateCreate(order.getDateCreate())
                .status(order.getStatus())
                .OrderDetails(order.getOrderDetailList() == null
                        ? Collections.emptyList()
                        : order.getOrderDetailList().stream()
                        .map(OrderDetail::getId)
                        .collect(Collectors.toList()))
                .Payments(order.getPaymentList() == null
                        ? Collections.emptyList()
                        : order.getPaymentList().stream()
                        .map(Payment::getId)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public void createOrder(OrderDTO orderDTO) {
        Customer customer = customerRepository.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found!"));

        List<OrderDetail> orderDetails = Optional.ofNullable(orderDTO.getOrderDetails())
                .orElse(Collections.emptyList()) // Nếu null thì gán list rỗng
                .stream()
                .map(orderDetailId -> orderDetailRepository.findById(orderDetailId)
                        .orElseThrow(() -> new EntityNotFoundException("OrderDetail not found!")))
                .toList();

        Order order = Order.builder()
                .customer(customer)
                .dateCreate(LocalDateTime.now())
                .id(null)
                .orderDetailList(orderDetails)
                .paymentList(null)
                .status(Enums.OrderStatus.Pending)
                .build();

        orderRepository.save(order);
    }

    @Transactional
    @Override
    public void updateOrder(UUID id, OrderDTO orderDTO) {
            Order existingOrder = orderRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Order not found!"));

            Customer customer = customerRepository.findById(orderDTO.getCustomerId())
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found!"));

            existingOrder.setCustomer(customer);
            existingOrder.setStatus(orderDTO.getStatus());
            existingOrder.setDateCreate(orderDTO.getDateCreate());

            if (orderDTO.getOrderDetails() == null || orderDTO.getOrderDetails().isEmpty()) {
                existingOrder.getOrderDetailList().removeIf(orderDetail -> true);
            } else {
                existingOrder.getOrderDetailList().removeIf(orderDetail -> true);
                existingOrder.setOrderDetailList(orderDTO.getOrderDetails().stream()
                        .map(orderDetailId -> orderDetailRepository.findById(orderDetailId)
                                .orElseThrow(() -> new EntityNotFoundException("OrderDetail not found"))).collect(Collectors.toList()));
            }

            orderRepository.save(existingOrder);
    }


    @Override
    public void deleteOrder(UUID id) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found!"));

        existingOrder.getOrderDetailList().forEach(orderDetail -> orderDetail.setOrder(null));
        existingOrder.getPaymentList().forEach(payment -> payment.setOrder(null));
        orderRepository.delete(existingOrder);
    }
}