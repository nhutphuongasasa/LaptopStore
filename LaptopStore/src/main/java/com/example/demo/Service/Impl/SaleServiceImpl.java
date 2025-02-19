package com.example.demo.Service.Impl;

import com.example.demo.DTO.CommentDTO;
import com.example.demo.DTO.SaleDTO;
import com.example.demo.Models.Comment;
import com.example.demo.Models.LaptopModel;
import com.example.demo.Models.Sale;
import com.example.demo.Repository.LaptopModelRepository;
import com.example.demo.Repository.SaleRepository;
import com.example.demo.Service.SaleService;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final LaptopModelRepository laptopModelRepository;
    private final ModelMapper modelMapper;

    public SaleServiceImpl(SaleRepository saleRepository, LaptopModelRepository laptopModelRepository, ModelMapper modelMapper) {
        this.saleRepository = saleRepository;
        this.laptopModelRepository = laptopModelRepository;
        this.modelMapper = modelMapper;
    }

    //Lấy danh sách tất cả Sale
    @Transactional
    @Override
    public List<SaleDTO> getAllSales() {
        return saleRepository.findAll().stream()
                .map(sale -> SaleDTO.builder()
                        .id(sale.getId())
                        .discount(sale.getDiscount())
                        .endAt(sale.getEndAt())
                        .startAt(sale.getStartAt())
                        .eventDescription(sale.getEvent_description() == null ? null : sale.getEvent_description())
                        .laptopModelIds(sale.getLaptopModelList() == null ? Collections.emptyList() :
                                sale.getLaptopModelList().stream()
                                        .map(LaptopModel::getId)
                                        .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

    }

    //Lấy Sale theo ID
    @Transactional
    @Override
    public SaleDTO getSaleById(UUID id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found with ID: " + id));
        return SaleDTO.builder()
                .id(sale.getId())
                .discount(sale.getDiscount())
                .endAt(sale.getEndAt())
                .startAt(sale.getStartAt())
                .eventDescription(sale.getEvent_description() == null ? null : sale.getEvent_description())
                .laptopModelIds(sale.getLaptopModelList() == null ? Collections.emptyList() :
                        sale.getLaptopModelList().stream()
                                .map(LaptopModel::getId)
                                .collect(Collectors.toList()))
                .build();
    }

    //Tạo mới một Sale
    @Transactional
    @Override
    public SaleDTO createSale(SaleDTO saleDTO) {
        Sale sale = Sale.builder()
                .id(null)
                .event_description(saleDTO.getEventDescription())
                .startAt(saleDTO.getStartAt())
                .endAt(saleDTO.getEndAt())
                .discount(saleDTO.getDiscount())
                .build();

        if (saleDTO.getLaptopModelIds() != null && !saleDTO.getLaptopModelIds().isEmpty()) {
            List<LaptopModel> laptopModels = saleDTO.getLaptopModelIds().stream()
                    .map(laptopModelId -> laptopModelRepository.findById(laptopModelId)
                            .orElseThrow(() -> new EntityNotFoundException("LaptopModel not found ")))
                    .collect(Collectors.toList());
            sale.setLaptopModelList(laptopModels);
        }
        Sale saleExisting = saleRepository.save(sale);

        return convertToDTO(sale);
    }

    //cap nhat sale
    @Transactional
    @Override
    public SaleDTO updateSale(UUID saleId, SaleDTO saleDTO) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new EntityNotFoundException("Sale not found"));

        modelMapper.map(saleDTO, sale);
        sale.setId(saleId);

        if (saleDTO.getLaptopModelIds() != null) {
            List<LaptopModel> laptopModels = saleDTO.getLaptopModelIds().stream()
                    .map(laptopModelId -> laptopModelRepository.findById(laptopModelId)
                            .orElseThrow(() -> new EntityNotFoundException("LaptopModel not found")))
                    .collect(Collectors.toList());
            sale.setLaptopModelList(laptopModels);
        }

        Sale saleExisting = saleRepository.save(sale);
        return convertToDTO(saleExisting);
    }

    //Xóa Sale theo ID
    @Transactional
    @Override
    public void deleteSale(UUID id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sale not found"));

        sale.getLaptopModelList().forEach(laptopModel -> laptopModel.getSaleList().remove(sale));

        saleRepository.delete(sale);
    }

    private SaleDTO convertToDTO(Sale sale) {
        return SaleDTO.builder()
                .id(sale.getId())
                .discount(sale.getDiscount())
                .endAt(sale.getEndAt())
                .startAt(sale.getStartAt())
                .eventDescription(sale.getEvent_description() == null ? null : sale.getEvent_description())
                .laptopModelIds(sale.getLaptopModelList() == null ? Collections.emptyList() :
                        sale.getLaptopModelList().stream()
                                .map(LaptopModel::getId)
                                .collect(Collectors.toList()))
                .build();
    }
}