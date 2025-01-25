package com.example.demo.DTO;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressDTO {
    private UUID id;
    private CustomerDTO customer;
    private String city;
    private String district;
    private String ward;
    private String street;
    private String phone;
}
