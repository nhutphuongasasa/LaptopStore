package com.example.demo.DTO;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ImageDTO {
    private UUID id;
    private String imageUrl;
    private List<UUID> laptopModelIds; // Danh sách LaptopModel gắn với Image
}