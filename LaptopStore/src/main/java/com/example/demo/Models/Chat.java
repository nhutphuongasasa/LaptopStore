package com.example.demo.Models;

import jakarta.persistence.*;
import lombok.*;


import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name="chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Account senderId;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Account receiverId;

    @Column(nullable = false)
    private String message;

    @Column(name = "create_at",nullable = false)
    private Date createAt;

}
