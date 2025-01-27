package com.example.demo.DTO;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethodDTO {
    private UUID id;
    private String data;
    private String type; // Tên type trong Enum

}