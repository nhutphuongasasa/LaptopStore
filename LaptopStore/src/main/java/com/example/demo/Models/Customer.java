package com.example.demo.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


import java.util.List;

// @AllArgsConstructor
// @NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name="customer")
public class Customer {
    @Id
    @OneToOne
    @JoinColumn(name = "account_id")
    private Account customerId;

    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private List<Address> addressList;

    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private List<Payment> paymentList;

    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private List<Order> oderList;

    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private List<Cart> cartList;
}
