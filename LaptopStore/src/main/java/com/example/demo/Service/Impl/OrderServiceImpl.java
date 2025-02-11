package com.example.demo.Service.Impl;

import com.example.demo.Common.Enums;
import com.example.demo.DTO.OrderDTO;
import com.example.demo.DTO.OrderDetailDTO;
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
                        .orderDetails(order.getOrderDetailList() == null
                                ? Collections.emptyList()
                                : order.getOrderDetailList().stream()
                                .map(OrderDetail::getId)
                                .collect(Collectors.toList()))
                        .payments(order.getPaymentList() == null
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
                .orderDetails(order.getOrderDetailList() == null
                        ? Collections.emptyList()
                        : order.getOrderDetailList().stream()
                        .map(OrderDetail::getId)
                        .collect(Collectors.toList()))
                .payments(order.getPaymentList() == null
                        ? Collections.emptyList()
                        : order.getPaymentList().stream()
                        .map(Payment::getId)
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Customer customer = customerRepository.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found!"));

        Order order = Order.builder()
                .customer(customer)
                .dateCreate(LocalDateTime.now())
                .orderDetailList(null)
                .paymentList(null)
                .status(Enums.OrderStatus.Pending)
                .build();

        Order orderExisting = orderRepository.save(order);

        return convertToDTO(orderExisting);
    }


    @Transactional
    @Override
    public OrderDTO updateOrder(UUID id, OrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found!"));

        existingOrder.setStatus(orderDTO.getStatus());
        existingOrder.setDateCreate(orderDTO.getDateCreate());
        //cap nhat danh sach orderdetail
        if (orderDTO.getOrderDetails() == null || orderDTO.getOrderDetails().isEmpty()) {
            existingOrder.getOrderDetailList().clear();
        } else {
            List<OrderDetail> newOrderDetails = orderDTO.getOrderDetails().stream()
                    .map(orderDetailId -> orderDetailRepository.findById(orderDetailId)
                            .orElseThrow(() -> new EntityNotFoundException("OrderDetail not found"))).toList();
            //loai bo nhung orderdetail khong co trong danh sach dto
            existingOrder.getOrderDetailList().removeIf(orderDetail -> !newOrderDetails.contains(orderDetail));
            //them nhung orderdetail moi
            newOrderDetails.forEach(orderDetail -> {
                if (!existingOrder.getOrderDetailList().contains(orderDetail)) {
                    orderDetail.setOrder(existingOrder);
                    existingOrder.getOrderDetailList().add(orderDetail);
                }
            });
        }

        if (orderDTO.getPayments() == null || orderDTO.getPayments().isEmpty()) {
            existingOrder.getPaymentList().clear();
        } else {
            List<Payment> newPayments = orderDTO.getPayments().stream()
                    .map(paymentId -> paymentRepository.findById(paymentId)
                            .orElseThrow(() -> new EntityNotFoundException("Payment not found"))).toList();

            existingOrder.getPaymentList().removeIf(payment -> !newPayments.contains(payment));

            newPayments.forEach(payment -> {
                if (!existingOrder.getPaymentList().contains(payment)) {
                    payment.setOrder(existingOrder);
                    existingOrder.getPaymentList().add(payment);
                }
            });
        }

        Order order = orderRepository.save(existingOrder);

        return convertToDTO(order);
    }

    @Override
    public void deleteOrder(UUID id) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found!"));

        existingOrder.getOrderDetailList().forEach(orderDetail -> orderDetail.setOrder(null));
        existingOrder.getPaymentList().forEach(payment -> payment.setOrder(null));
        orderRepository.delete(existingOrder);
    }

    private OrderDTO convertToDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .payments(order.getPaymentList() == null ? null :
                        order.getPaymentList().stream()
                                .map(Payment::getId)
                                .collect(Collectors.toList()))
                .orderDetails(order.getOrderDetailList() == null ? null :
                        order.getOrderDetailList().stream()
                                .map(OrderDetail::getId)
                                .collect(Collectors.toList()))
                .status(order.getStatus())
                .customerId(order.getCustomer().getId())
                .build();
    }
}