package com.example.demo.Service.Impl;

import com.example.demo.DTO.OrderDetailDTO;
import com.example.demo.Models.LaptopModel;
import com.example.demo.Models.Order;
import com.example.demo.Models.OrderDetail;
import com.example.demo.Repository.LaptopModelRepository;
import com.example.demo.Repository.OrderDetailRepository;
import com.example.demo.Repository.OrderRepository;
import com.example.demo.Service.OrderDetailService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
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

    @Override
    public List<OrderDetailDTO> getAllOrderDetails() {
        return orderDetailRepository.findAll().stream()
                .map(orderDetail -> OrderDetailDTO.builder()
                        .id(orderDetail.getId())
                        .orderId(orderDetail.getOrder().getId())
                        .laptopModelId(orderDetail.getLaptopModel().getId())
                        .quantity(orderDetail.getQuantity())
                        .price(orderDetail.getPrice())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public OrderDetailDTO getOrderDetailById(UUID id) {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderDetail with ID " + id + " not found!"));

        // Sử dụng Builder để trả về OrderDetailDTO
        return OrderDetailDTO.builder()
                .id(orderDetail.getId())
                .orderId(orderDetail.getOrder().getId())
                .laptopModelId(orderDetail.getLaptopModel().getId())
                .quantity(orderDetail.getQuantity())
                .price(orderDetail.getPrice())
                .build();
    }

    @Override
    public void createOrderDetail(OrderDetailDTO orderDetailDTO) {
        // Lấy thông tin Order
        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order with ID " + orderDetailDTO.getOrderId() + " not found!"));

        // Lấy thông tin LaptopModel
        LaptopModel laptopModel = laptopModelRepository.findById(orderDetailDTO.getLaptopModelId())
                .orElseThrow(() -> new RuntimeException("LaptopModel with ID " + orderDetailDTO.getLaptopModelId() + " not found!"));

        // Sử dụng Builder để khởi tạo OrderDetail entity
        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .laptopModel(laptopModel)
                .quantity(orderDetailDTO.getQuantity())
                .price(orderDetailDTO.getPrice())
                .build();

        orderDetailRepository.save(orderDetail);
    }

    @Override
    public void updateOrderDetail(UUID id, OrderDetailDTO orderDetailDTO) {
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderDetail with ID " + id + " not found!"));

        // Lấy thông tin Order và LaptopModel từ DTO
        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order with ID " + orderDetailDTO.getOrderId() + " not found!"));

        LaptopModel laptopModel = laptopModelRepository.findById(orderDetailDTO.getLaptopModelId())
                .orElseThrow(() -> new RuntimeException("LaptopModel with ID " + orderDetailDTO.getLaptopModelId() + " not found!"));

        // Cập nhật OrderDetail (có thể dùng Builder tại đây nhưng vì cập nhật entity hiện có nên sử dụng setter)
        existingOrderDetail.setOrder(order);
        existingOrderDetail.setLaptopModel(laptopModel);
        existingOrderDetail.setQuantity(orderDetailDTO.getQuantity());
        existingOrderDetail.setPrice(orderDetailDTO.getPrice());

        // Lưu thay đổi
        orderDetailRepository.save(existingOrderDetail);
    }

    @Override
    public void deleteOrderDetail(UUID id) {
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderDetail with ID " + id + " not found!"));

        existingOrderDetail.setLaptopModel(null);
        existingOrderDetail.setOrder(null);
        orderDetailRepository.save(existingOrderDetail);
        orderDetailRepository.delete(existingOrderDetail);
    }
}