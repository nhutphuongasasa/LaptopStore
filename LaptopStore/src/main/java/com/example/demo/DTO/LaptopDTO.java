package com.example.demo.DTO;

import lombok.*;

import com.example.demo.Common.Enums;

import java.sql.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LaptopDTO {

    private UUID id;

    private Date mfg;

    private UUID laptopModelId;

    private Enums.laptopStatus status;
}