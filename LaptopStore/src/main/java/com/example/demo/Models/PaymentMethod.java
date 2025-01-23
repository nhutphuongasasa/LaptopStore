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
@Table(name="payment")
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(columnDefinition = "json")
    private String data;

    @Column(nullable = false)
    private Enums.PaymentType type;

    @JsonIgnore
    @OneToMany(mappedBy = "paymentMethod")
    private List<Payment> paymentList;
}
