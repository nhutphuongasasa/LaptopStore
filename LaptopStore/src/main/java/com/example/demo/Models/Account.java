package com.example.demo.Models;

import com.example.demo.Common.Enums;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name="account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(unique = true,nullable = false)
    private String email;

    private String name;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,columnDefinition = "VARCHAR(255) DEFAULT 'CUSTOMER'")
    private Enums.role role;

    @OneToOne(mappedBy = "customerId",cascade = CascadeType.ALL)
    private Customer customerId;

    @OneToOne(mappedBy = "adminId",cascade = CascadeType.ALL)
    private Admin adminId;

    @JsonIgnore
    @OneToMany(mappedBy = "account")
    private List<Comment> commentList;

    @JsonIgnore
    @OneToMany(mappedBy = "senderId")
    private List<Chat> chatSender;

    @JsonIgnore
    @OneToMany(mappedBy = "receiverId")
    private List<Chat> chatReceiver;

}
