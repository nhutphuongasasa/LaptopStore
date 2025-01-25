package com.example.demo.DTO;

import java.util.UUID;

import com.example.demo.Common.Enums;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDTO {
    private UUID id;

    private String email;

    private String name;

    private String password;

    private Enums.role role;

}
