package com.example.demo.DTO;

import com.example.demo.Common.Enums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private UUID id; // ID của Order
    private UUID customerId; // ID của Customer
    private Enums.OrderStatus status; // Trạng thái Order
    private Date dateCreate; // Ngày tạo Order
}