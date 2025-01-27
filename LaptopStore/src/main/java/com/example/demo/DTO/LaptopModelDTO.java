package com.example.demo.DTO;

import com.example.demo.Common.Enums;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LaptopModelDTO {
    private UUID id;                     // ID của LaptopModel
    private String name;
    private String branch;
    private String cpu;
    private String ram;
    private String storage;
    private String display;
    private Enums.Color color;
    private BigDecimal price;
    private String description;
    private List<UUID> commentIds;      // Danh sách ID của Comment liên quan
    private List<UUID> imageIds;        // Danh sách ID của Image
    private List<UUID> saleIds;         // Danh sách ID của Sale
    private List<UUID> laptopIds;       // Danh sách ID của Laptop
    private List<UUID> laptopOnCartIds;
    private List<UUID> orderDetailIds;
}