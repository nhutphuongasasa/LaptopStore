package com.example.demo.Models;

import com.example.demo.Common.Enums;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name="laptop_model")
public class LaptopModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String branch;

    @Column(nullable = false)
    private String cpu;

    @Column(nullable = false)
    private String ram;

    @Column(nullable = false)
    private String storage;

    @Column(nullable = false)
    private String display;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Enums.Color color;

    @Column(nullable = false)
    private BigDecimal price;

    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "laptopModel")
    private List<Comment> commentList;

    @Column(nullable = false)
    @OneToMany(mappedBy = "laptopModel")
    private List<Laptop> laptopList;

    @ManyToMany(mappedBy = "laptopModelList")
    private List<Image> imageList;

    @ManyToMany(mappedBy = "laptopModelList")
    private List<Sale> saleList;

    @JsonIgnore
    @OneToMany(mappedBy = "laptopModel")
    private List<OrderDetail> orderDetailList;

    @JsonIgnore
    @OneToMany(mappedBy = "laptopModel")
    private List<LaptopOnCart> laptopOnCartList;

}
