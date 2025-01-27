package com.example.demo.Service.Impl;

import com.example.demo.DTO.CartDTO;
import com.example.demo.Models.Account;
import com.example.demo.Models.Cart;
import com.example.demo.Models.Customer;
import com.example.demo.Models.LaptopOnCart;
import com.example.demo.Repository.AccountRepository;
import com.example.demo.Repository.CartRepository;
import com.example.demo.Repository.CustomerRepository;
import com.example.demo.Service.CartService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    public CartServiceImpl(CartRepository cartRepository, CustomerRepository customerRepository, AccountRepository accountRepository) {
        this.cartRepository = cartRepository;
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
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
                        : null
                )
                .build();
    }

    // Tạo mới Cart
    @Override
    public void createCart(CartDTO cartDTO) {
        Account accountCustomer = accountRepository.findById(cartDTO.getCustomerId()).orElseThrow(() -> new RuntimeException("Customer with ID " + cartDTO.getCustomerId() + " not found!"));

        Customer customer = customerRepository.findById(accountCustomer)
                .orElseThrow(() -> new RuntimeException("Customer with ID " + cartDTO.getCustomerId() + " not found!"));

        Cart cart = Cart.builder()
                .customer(customer)
                .build();
        cartRepository.save(cart);
    }

    // Cập nhật Cart theo ID
    @Override
    public void updateCart(UUID id, CartDTO cartDTO) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart with ID " + id + " not found!"));

        Account accountCustomer = accountRepository.findById(cartDTO.getCustomerId()).orElseThrow(() -> new RuntimeException("Customer with ID " + cartDTO.getCustomerId() + " not found!"));

        Customer customer = customerRepository.findById(accountCustomer)
                .orElseThrow(() -> new RuntimeException("Customer with ID " + cartDTO.getCustomerId() + " not found!"));

        cart.setCustomer(customer);

        cartRepository.save(cart);
    }

    // Xóa Cart theo ID
    @Override
    public void deleteCart(UUID id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart with ID " + id + " not found!"));

        cart.setCustomer(null);
        cartRepository.save(cart); // Ngắt liên kết trước khi xóa

        cartRepository.delete(cart);
    }
}