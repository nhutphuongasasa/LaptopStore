package com.example.demo.Service.Impl;

import com.example.demo.DTO.ImageDTO;
import com.example.demo.DTO.OrderDTO;
import com.example.demo.DTO.OrderDetailDTO;
import com.example.demo.Models.Image;
import com.example.demo.Models.LaptopModel;
import com.example.demo.Models.Order;
import com.example.demo.Models.OrderDetail;
import com.example.demo.Repository.LaptopModelRepository;
import com.example.demo.Repository.OrderDetailRepository;
import com.example.demo.Repository.OrderRepository;
import com.example.demo.Service.OrderDetailService;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final LaptopModelRepository laptopModelRepository;

    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository,
                                  OrderRepository orderRepository,
                                  LaptopModelRepository laptopModelRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.orderRepository = orderRepository;
        this.laptopModelRepository = laptopModelRepository;
    }

    @Transactional
    @Override
    public List<OrderDetailDTO> getAllOrderDetails() {
        return orderDetailRepository.findAll().stream()
                .map(orderDetail -> OrderDetailDTO.builder()
                        .id(orderDetail.getId())
                        .orderId(orderDetail.getOrder() == null ? null : orderDetail.getOrder().getId())
                        .laptopModelId(orderDetail.getLaptopModel().getId())
                        .quantity(orderDetail.getQuantity())
                        .price(orderDetail.getPrice())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public OrderDetailDTO getOrderDetailById(UUID id) {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrderDetail not found"));

        return OrderDetailDTO.builder()
                .id(orderDetail.getId())
                .orderId(orderDetail.getOrder() == null ? null : orderDetail.getOrder().getId())
                .laptopModelId(orderDetail.getLaptopModel().getId())
                .quantity(orderDetail.getQuantity())
                .price(orderDetail.getPrice())
            .build();
    }

    @Transactional
    @Override
    public OrderDetailDTO createOrderDetail(OrderDetailDTO orderDetailDTO) {
        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order  not found!"));

        LaptopModel laptopModel = laptopModelRepository.findById(orderDetailDTO.getLaptopModelId())
                .orElseThrow(() -> new EntityNotFoundException("LaptopModel not found!"));

        OrderDetail orderDetail = OrderDetail.builder()
                .id(null)
                .order(order)
                .laptopModel(laptopModel)
                .quantity(orderDetailDTO.getQuantity())
                .price(orderDetailDTO.getPrice())
                .build();

        OrderDetail orderDetailExisting = orderDetailRepository.save(orderDetail);

        return convertToDTO(orderDetailExisting);
    }

    @Transactional
    @Override
    public OrderDetailDTO updateOrderDetail(UUID id, OrderDetailDTO orderDetailDTO) {
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrderDetail not found!"));

        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found!"));

        LaptopModel laptopModel = laptopModelRepository.findById(orderDetailDTO.getLaptopModelId())
                .orElseThrow(() -> new EntityNotFoundException("LaptopModel not found!"));

        existingOrderDetail.setOrder(order);
        existingOrderDetail.setLaptopModel(laptopModel);
        existingOrderDetail.setQuantity(orderDetailDTO.getQuantity());
        existingOrderDetail.setPrice(orderDetailDTO.getPrice());

        OrderDetail orderDetailExisting = orderDetailRepository.save(existingOrderDetail);

        return  convertToDTO(orderDetailExisting);
    }

    @Transactional
    @Override
    public void deleteOrderDetail(UUID id) {
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrderDetail not found!"));

        orderDetailRepository.delete(existingOrderDetail);
    }

    private OrderDetailDTO convertToDTO(OrderDetail orderDetail) {
        return OrderDetailDTO.builder()
                .id(orderDetail.getId())
                .orderId(orderDetail.getId())
                .laptopModelId(orderDetail.getLaptopModel().getId())
                .quantity(orderDetail.getQuantity())
                .price(orderDetail.getPrice())
                .build();
    }
}