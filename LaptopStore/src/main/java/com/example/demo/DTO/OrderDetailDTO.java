package com.example.demo.DTO;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailDTO {
    private UUID id;                // ID của OrderDetail
    private UUID orderId;           // ID của Order (quan hệ ManyToOne)
    private UUID laptopModelId;     // ID của LaptopModel (quan hệ ManyToOne)
    private Integer quantity;       // Số lượng
    private BigDecimal price;       // Giá
}