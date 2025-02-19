package com.example.demo.Service;

import com.example.demo.DTO.SaleDTO;

import java.util.List;
import java.util.UUID;

public interface SaleService {

    List<SaleDTO> getAllSales();

    SaleDTO getSaleById(UUID id);

    SaleDTO createSale(SaleDTO saleDTO);

    SaleDTO updateSale(UUID saleId, SaleDTO saleDTO);

    void deleteSale(UUID id);
}