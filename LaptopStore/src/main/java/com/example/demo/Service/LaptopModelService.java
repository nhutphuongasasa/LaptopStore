package com.example.demo.Service;

import com.example.demo.DTO.LaptopModelDTO;

import java.util.List;
import java.util.UUID;

public interface LaptopModelService {
    List<LaptopModelDTO> getAllLaptopModels();                  // Lấy tất cả LaptopModel
    LaptopModelDTO getLaptopModelById(UUID id);                 // Lấy LaptopModel theo ID
    void createLaptopModel(LaptopModelDTO laptopModelDTO);      // Tạo LaptopModel mới
    void updateLaptopModel(UUID id, LaptopModelDTO laptopModelDTO); // Cập nhật LaptopModel theo ID
    void deleteLaptopModel(UUID id);                            // Xóa LaptopModel theo ID
}