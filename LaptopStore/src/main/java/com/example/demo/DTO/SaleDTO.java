package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleDTO {
    private UUID id;
    private String eventDescription;
    private Date startAt;
    private Date endAt;
    private Float discount;
    private List<UUID> laptopModelIds; // Danh sách ID của các LaptopModel tham gia vào Sale
}