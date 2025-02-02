package com.example.demo.Service.Impl;

import com.example.demo.Common.SaleNotFoundException;
import com.example.demo.DTO.SaleDTO;
import com.example.demo.Models.LaptopModel;
import com.example.demo.Models.Sale;
import com.example.demo.Repository.LaptopModelRepository;
import com.example.demo.Repository.SaleRepository;
import com.example.demo.Service.SaleService;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final LaptopModelRepository laptopModelRepository;
    private final ModelMapper modelMapper;

    public SaleServiceImpl(SaleRepository saleRepository,
                           LaptopModelRepository laptopModelRepository,
                           ModelMapper modelMapper) {
        this.saleRepository = saleRepository;
        this.laptopModelRepository = laptopModelRepository;
        this.modelMapper = modelMapper;
    }

    // 1. Lấy danh sách tất cả Sale
    @Override
    public List<SaleDTO> getAllSales() {
        return saleRepository.findAll().stream()
                .map(sale -> {
                    SaleDTO saleDTO = modelMapper.map(sale, SaleDTO.class);
                    saleDTO.setLaptopModelIds(sale.getLaptopModelList().stream()
                            .map(LaptopModel::getId)
                            .collect(Collectors.toList()));
                    return saleDTO;
                })
                .collect(Collectors.toList());
    }

    // 2. Lấy Sale theo ID
    @Override
    public SaleDTO getSaleById(UUID id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found with ID: " + id));
        SaleDTO saleDTO = modelMapper.map(sale, SaleDTO.class);
        saleDTO.setLaptopModelIds(sale.getLaptopModelList().stream()
                .map(LaptopModel::getId)
                .collect(Collectors.toList()));
        return saleDTO;
    }

    // 3. Tạo mới một Sale
    @Override
    public void createSale(SaleDTO saleDTO) {
        Sale sale = Sale.builder()
                .event_description(saleDTO.getEventDescription())
                .startAt(saleDTO.getStartAt())
                .endAt(saleDTO.getEndAt())
                .discount(saleDTO.getDiscount())
                .build();


        // Danh sách LaptopModels có thể trống khi tạo Sale
        if (saleDTO.getLaptopModelIds() != null && !saleDTO.getLaptopModelIds().isEmpty()) {
            List<LaptopModel> laptopModels = saleDTO.getLaptopModelIds().stream()
                    .map(laptopModelId -> laptopModelRepository.findById(laptopModelId)
                            .orElseThrow(() -> new RuntimeException("LaptopModel not found with ID: " + laptopModelId)))
                    .collect(Collectors.toList());
            sale.setLaptopModelList(laptopModels);
        }
        saleRepository.save(sale);

    }
    //cap nhat sale
    @Override
    public void updateSale(UUID saleId, SaleDTO saleDTO) {
        // Tìm Sale theo ID từ database
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException("Sale not found"));

        sale.setEvent_description(saleDTO.getEventDescription());
        sale.setStartAt(saleDTO.getStartAt());
        sale.setEndAt(saleDTO.getEndAt());
        sale.setDiscount(saleDTO.getDiscount());

        if (saleDTO.getLaptopModelIds() != null) {
            List<LaptopModel> laptopModels = saleDTO.getLaptopModelIds().stream()
                    .map(laptopModelId -> laptopModelRepository.findById(laptopModelId)
                            .orElseThrow(() -> new RuntimeException("LaptopModel not found with ID: " + laptopModelId)))
                    .collect(Collectors.toList());
            sale.setLaptopModelList(laptopModels);
        }

        // Lưu các thay đổi vào database
        saleRepository.save(sale);

    }

    // 5. Xóa Sale theo ID
    @Override
    public void deleteSale(UUID id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found with ID: " + id));

        // Xóa mối quan hệ giữa Sale và các LaptopModel liên kết
        sale.getLaptopModelList().forEach(laptopModel -> laptopModel.getSaleList().remove(sale));

        // Xóa hoàn toàn Sale
        saleRepository.delete(sale);
    }
}