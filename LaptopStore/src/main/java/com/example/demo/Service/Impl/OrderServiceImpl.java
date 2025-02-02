package com.example.demo.Service.Impl;

import com.example.demo.DTO.OrderDTO;
import com.example.demo.Models.Account;
import com.example.demo.Models.Customer;
import com.example.demo.Models.Order;
import com.example.demo.Repository.AccountRepository;
import com.example.demo.Repository.CustomerRepository;
import com.example.demo.Repository.OrderRepository;
import com.example.demo.Service.OrderService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository; // Repository để kiểm tra Customer tồn tại
    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;

    public OrderServiceImpl(OrderRepository orderRepository, CustomerRepository customerRepository, ModelMapper modelMapper,AccountRepository accountRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
        this.accountRepository = accountRepository;
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> {
                    OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);

                    if (order.getCustomer() != null) {
                        orderDTO.setCustomerId(order.getCustomer().getCustomerId().getId());
                    } else {
                        orderDTO.setCustomerId(null); // Nếu không có Customer, set null cho customerId
                    }

                    return orderDTO; // Trả về đối tượng DTO
                })
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO getOrderById(UUID id) {
        // Tìm Order theo ID
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order with ID " + id + " not found!"));

        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);

        if (order.getCustomer() != null) {
            orderDTO.setCustomerId(order.getCustomer().getCustomerId().getId());
        } else {
            orderDTO.setCustomerId(null);
        }

        return orderDTO;
    }

    @Override
    public void createOrder(OrderDTO orderDTO) {
        //tim account cua customer
        Account accountCustomer = accountRepository.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer with ID " + orderDTO.getCustomerId() + " not found!"));
        // Kiểm tra xem Customer có tồn tại không
        Customer customer = customerRepository.findById(accountCustomer.getId())
                .orElseThrow(() -> new RuntimeException("Customer with ID " + orderDTO.getCustomerId() + " not found!"));

        // Map từ DTO sang Entity
        Order order = modelMapper.map(orderDTO, Order.class);
        order.setCustomer(customer); // Gán cho Customer

        // Save Order
        orderRepository.save(order);
    }

    @Override
    public void updateOrder(UUID id, OrderDTO orderDTO) {
        // Lấy Order theo ID
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order with ID " + id + " not found!"));

        //tim account cua customer
        Account accountCustomer = accountRepository.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer with ID " + orderDTO.getCustomerId() + " not found!"));

        // Kiểm tra xem Customer mới có tồn tại không
        Customer customer = customerRepository.findById(accountCustomer.getId())
                .orElseThrow(() -> new RuntimeException("Customer with ID " + orderDTO.getCustomerId() + " not found!"));

        // Cập nhật thông tin Order
        existingOrder.setCustomer(customer);
        existingOrder.setStatus(orderDTO.getStatus());
        existingOrder.setDateCreate(orderDTO.getDateCreate());

        // Save cập nhật
        orderRepository.save(existingOrder);
    }

    @Override
    public void deleteOrder(UUID id) {
        // Lấy Order theo ID
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order with ID " + id + " not found!"));
        existingOrder.setCustomer(null);
        // Xóa Order
        orderRepository.delete(existingOrder);
    }
}