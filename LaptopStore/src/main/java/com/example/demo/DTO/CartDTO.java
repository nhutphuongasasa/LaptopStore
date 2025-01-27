package com.example.demo.DTO;

import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartDTO {
    private UUID id; // ID của Cart
    private UUID customerId; // ID của Customer
    private List<UUID> laptopOnCartIds; // ID danh sách LaptopOnCart
}