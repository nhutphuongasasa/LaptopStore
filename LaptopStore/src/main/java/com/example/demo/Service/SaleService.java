package com.example.demo.Service;

import com.example.demo.DTO.SaleDTO;

import java.util.List;
import java.util.UUID;

public interface SaleService {

    List<SaleDTO> getAllSales();

    SaleDTO getSaleById(UUID id);

    void createSale(SaleDTO saleDTO);

    void attachLaptopModelsToSale(UUID saleId, List<UUID> laptopModelIds);

    void updateSale(UUID saleId, SaleDTO saleDTO);

    void deleteSale(UUID id);
}