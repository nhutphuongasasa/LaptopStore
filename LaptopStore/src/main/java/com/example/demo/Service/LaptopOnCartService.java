package com.example.demo.Service;

import com.example.demo.DTO.LaptopOnCartDTO;

import java.util.List;
import java.util.UUID;

public interface LaptopOnCartService {
    List<LaptopOnCartDTO> getAllLaptopOnCarts();

    LaptopOnCartDTO getLaptopOnCartById(UUID id);

    void createLaptopOnCart(LaptopOnCartDTO laptopOnCartDTO);

    void updateLaptopOnCart(UUID id, LaptopOnCartDTO laptopOnCartDTO);

    void deleteLaptopOnCart(UUID id);
}