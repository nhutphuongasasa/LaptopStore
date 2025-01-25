package com.example.demo.DTO;

import java.util.List;
import java.util.UUID;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDTO {
    private UUID accountId;
    // private List<OrderDTO> orders;
    // private List<AddressDTO> addresses;
    // private List<PaymentDTO> payments;
    // private CartDTO cart;
}
