package com.example.demo.Models;


import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name="admin")
public class Admin {
    @Id
    @OneToOne
    @JoinColumn(name = "account_id")
    private Account adminId;
}