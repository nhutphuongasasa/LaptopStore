package com.example.demo.Service;

import com.example.demo.DTO.LaptopOnCartDTO;

import java.util.List;
import java.util.UUID;

public interface LaptopOnCartService {
    List<LaptopOnCartDTO> getAllLaptopOnCarts();

    LaptopOnCartDTO getLaptopOnCartById(UUID id);

    LaptopOnCartDTO createLaptopOnCart(LaptopOnCartDTO laptopOnCartDTO);

    LaptopOnCartDTO updateLaptopOnCart(UUID id, LaptopOnCartDTO laptopOnCartDTO);

    void deleteLaptopOnCart(UUID id);
}