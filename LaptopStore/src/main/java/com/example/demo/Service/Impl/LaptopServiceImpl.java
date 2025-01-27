package com.example.demo.Service.Impl;

import com.example.demo.DTO.LaptopDTO;
import com.example.demo.Models.Laptop;
import com.example.demo.Models.LaptopModel;
import com.example.demo.Repository.LaptopRepository;
import com.example.demo.Repository.LaptopModelRepository;
import com.example.demo.Service.LaptopService;

import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
public class LaptopServiceImpl implements LaptopService {

    private final LaptopRepository laptopRepository;
    private final LaptopModelRepository laptopModelRepository;
    private final ModelMapper modelMapper;

    public LaptopServiceImpl(LaptopRepository laptopRepository, LaptopModelRepository laptopModelRepository, ModelMapper modelMapper) {
        this.laptopRepository = laptopRepository;
        this.laptopModelRepository = laptopModelRepository;
        this.modelMapper = modelMapper;
    }

    // **1. Lấy danh sách tất cả Laptop**
    @Override
    public List<LaptopDTO> getAllLaptops() {
        return laptopRepository.findAll().stream()
                .map(laptop -> LaptopDTO.builder()
                        .id(laptop.getId())
                        .mfg(laptop.getMFG())
                        .status(laptop.getStatus())
                        .laptopModelId(laptop.getLaptopModel().getId()) // Ánh xạ LaptopModelId
                        .build())
                .collect(Collectors.toList());
    }

    // **2. Lấy Laptop chi tiết theo ID**
    @Override
    public LaptopDTO getLaptopById(UUID id) {
        Laptop laptop = laptopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Laptop not found!"));
        return LaptopDTO.builder()
                .id(laptop.getId())
                .mfg(laptop.getMFG())
                .status(laptop.getStatus())
                .laptopModelId(laptop.getLaptopModel().getId()) // Ánh xạ LaptopModelId
                .build();
    }

    // **3. Tạo mới Laptop**
    @Override
    public void createLaptop(LaptopDTO laptopDTO) {
        // 1. Map từ DTO -> Entity
        Laptop laptop = Laptop.builder()
                .MFG(laptopDTO.getMfg())
                .status(laptopDTO.getStatus())
                .build();

        // 2. Xác định LaptopModel dựa vào laptopModelId
        LaptopModel laptopModel = laptopModelRepository.findById(laptopDTO.getLaptopModelId())
                .orElseThrow(() -> new RuntimeException("Laptop Model with ID " + laptopDTO.getLaptopModelId() + " not found!"));

        // 3. Gán quan hệ Laptop và LaptopModel
        laptop.setLaptopModel(laptopModel);
        laptopModel.addLaptop(laptop);

        // 4. Lưu vào database
        laptopRepository.save(laptop);
    }

    // **4. Cập nhật Laptop**
    @Override
    @Transactional
    public void updateLaptop(UUID laptopId, LaptopDTO updatedLaptopDTO) {
        // 1. Tìm Laptop hiện tại bằng ID
        Laptop existingLaptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new RuntimeException("Laptop with ID " + laptopId + " not found!"));

        // 2. Tìm LaptopModel mới nếu laptopModelId thay đổi
        if (!existingLaptop.getLaptopModel().getId().equals(updatedLaptopDTO.getLaptopModelId())) {
            LaptopModel newModel = laptopModelRepository.findById(updatedLaptopDTO.getLaptopModelId())
                    .orElseThrow(() -> new RuntimeException("LaptopModel with ID " + updatedLaptopDTO.getLaptopModelId() + " not found!"));

            // Cập nhật quan hệ
            existingLaptop.getLaptopModel().removeLaptop(existingLaptop); // Xóa khỏi model cũ
            newModel.addLaptop(existingLaptop); // Thêm vào model mới
            existingLaptop.setLaptopModel(newModel);
        }

        // 3. Cập nhật các thuộc tính khác
        existingLaptop.setMFG(updatedLaptopDTO.getMfg());
        existingLaptop.setStatus(updatedLaptopDTO.getStatus());

        // 4. Lưu các thay đổi vào database
        laptopRepository.save(existingLaptop);
    }

    // **5. Xóa Laptop theo ID**
    @Override
    public void deleteLaptop(UUID id) {
        // 1. Kiểm tra laptop tồn tại
        Laptop laptop = laptopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Laptop with ID " + id + " does not exist!"));

        // 2. Xóa laptop khỏi LaptopModel trước khi xóa
        LaptopModel laptopModel = laptop.getLaptopModel();
        laptopModel.removeLaptop(laptop);

        laptopModelRepository.save(laptopModel);

        // 3. Xóa laptop
        laptopRepository.deleteById(id);
    }
}