package com.example.demo.DTO;

import com.example.demo.Common.Enums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private UUID id;
    private UUID customerId;
    private Enums.OrderStatus status;
    private LocalDateTime dateCreate;
    private List<UUID> OrderDetails;
    private List<UUID> Payments;
}