package com.example.demo.DTO;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {
    private UUID id;              // Id của comment
    private UUID accountId;       // ID của Account người viết comment
    private UUID parentId;        // ID của comment cha (nếu có)
    private String body;          // Nội dung của comment
    private UUID laptopModelId;   // ID của Laptop Model (nếu có)
    private List<UUID> replies;   // Danh sách các reply (trả lời)
}