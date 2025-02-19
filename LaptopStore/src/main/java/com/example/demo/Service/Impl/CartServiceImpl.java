package com.example.demo.Service.Impl;

import com.example.demo.DTO.AddressDTO;
import com.example.demo.DTO.CartDTO;
import com.example.demo.DTO.LaptopOnCartDTO;
import com.example.demo.Models.*;
import com.example.demo.Repository.AccountRepository;
import com.example.demo.Repository.CartRepository;
import com.example.demo.Repository.CustomerRepository;
import com.example.demo.Repository.LaptopOnCartRepository;
import com.example.demo.Service.CartService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final LaptopOnCartRepository laptopOnCartRepository;

    public CartServiceImpl(CartRepository cartRepository, CustomerRepository customerRepository,LaptopOnCartRepository laptopOnCartRepository) {
        this.cartRepository = cartRepository;
        this.customerRepository = customerRepository;
        this.laptopOnCartRepository=laptopOnCartRepository;
    }

    // Lấy tất cả Cart
    @Override
    public List<CartDTO> getAllCarts() {

        return cartRepository.findAll().stream()
                .map(cart -> CartDTO.builder()
                        .id(cart.getId())
                        .customerId(cart.getCustomer().getCustomerId().getId())
                        .laptopOnCartIds(cart.getLaptopOnCarts() != null
                                ? cart.getLaptopOnCarts().stream().map(LaptopOnCart::getId).collect(Collectors.toList())
                                : null
                        )
                        .build())
                .collect(Collectors.toList());
    }

    // Lấy Cart theo ID
    @Override
    public CartDTO getCartById(UUID id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart with ID " + id + " not found!"));

        return CartDTO.builder()
                .id(cart.getId())
                .customerId(cart.getCustomer().getCustomerId().getId())
                .laptopOnCartIds(cart.getLaptopOnCarts() != null
                        ? cart.getLaptopOnCarts().stream().map(LaptopOnCart::getId).collect(Collectors.toList())
                        : null)
                .build();
    }

    // Tạo mới Cart
    @Override
    public CartDTO createCart(CartDTO cartDTO) {
        Customer customer = customerRepository.findById(cartDTO.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found!"));

        Cart cart = Cart.builder()
                .customer(customer)
                .build();
        Cart cartExisting = cartRepository.save(cart);

        return convertToDTO(cartExisting);
    }

    // Cập nhật Cart theo ID
    @Override
    public CartDTO updateCart(UUID id, CartDTO cartDTO) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found!"));

        Customer customer = customerRepository.findById(cartDTO.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found!"));

        cart.setCustomer(customer);
        //loai bo toan bo laptopOnCart
        cart.getLaptopOnCarts().removeIf(laptop -> true);
        //lay laptopOnCart moi
        List<LaptopOnCart> laptopOnCarts = Optional.ofNullable(cartDTO.getLaptopOnCartIds())
                .orElse(Collections.emptyList())
                .stream()
                .map(laptopOnCartId -> laptopOnCartRepository.findById(laptopOnCartId)
                        .orElseThrow(() -> new EntityNotFoundException("LaptopOnCart not found ")))
                .toList();

        cart.getLaptopOnCarts().addAll(laptopOnCarts);

        Cart cartExisting = cartRepository.save(cart);
        return convertToDTO(cartExisting);
    }


    // Xóa Cart theo ID xoa luon cac LaptopOnCart con
    @Override
    public void deleteCart(UUID id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart with ID " + id + " not found!"));

        cart.getLaptopOnCarts().removeIf(laptop -> true);

        cartRepository.delete(cart);
    }

    private CartDTO convertToDTO(Cart cart) {
        return CartDTO.builder()
                .id(cart.getId())
                .customerId(cart.getCustomer().getId())
                .laptopOnCartIds(Optional.ofNullable(cart.getLaptopOnCarts())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(LaptopOnCart::getId)
                        .collect(Collectors.toList()))
                .build();

    }
}