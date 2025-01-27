package com.example.demo.DTO;

import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CustomerDTO {
    private UUID customerId; // Sử dụng UUID nếu Account ID là UUID.
    private List<UUID> addressIds; // Danh sách ID của Address
    private List<UUID> paymentIds; // Danh sách ID của Payment
    private List<UUID> orderIds; // Danh sách ID của Order
    private List<UUID> cartIds; // Danh sách ID của Cart
}