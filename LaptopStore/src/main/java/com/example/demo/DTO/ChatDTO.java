package com.example.demo.DTO;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDTO {
    private UUID id;
    private UUID senderId;
    private UUID receiverId;
    private String message;
    private Date createAt;
}