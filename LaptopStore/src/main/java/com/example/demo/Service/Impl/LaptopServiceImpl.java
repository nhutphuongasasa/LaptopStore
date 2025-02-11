package com.example.demo.Service.Impl;

import com.example.demo.DTO.CommentDTO;
import com.example.demo.DTO.LaptopDTO;
import com.example.demo.Models.Comment;
import com.example.demo.Models.Laptop;
import com.example.demo.Models.LaptopModel;
import com.example.demo.Repository.LaptopRepository;
import com.example.demo.Repository.LaptopModelRepository;
import com.example.demo.Service.LaptopService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class LaptopServiceImpl implements LaptopService {

    private final LaptopRepository laptopRepository;
    private final LaptopModelRepository laptopModelRepository;

    public LaptopServiceImpl(LaptopRepository laptopRepository, LaptopModelRepository laptopModelRepository) {
        this.laptopRepository = laptopRepository;
        this.laptopModelRepository = laptopModelRepository;
    }

    // **1. Lấy danh sách tất cả Laptop**
    @Transactional
    @Override
    public List<LaptopDTO> getAllLaptops() {
        return laptopRepository.findAll().stream()
                .map(laptop -> LaptopDTO.builder()
                        .macId(laptop.getMacId())
                        .mfg(laptop.getMFG())
                        .status(laptop.getStatus())
                        .laptopModelId(laptop.getLaptopModel() != null ? laptop.getLaptopModel().getId() : null)
                        .build())
                .collect(Collectors.toList());
    }

    // **2. Lấy Laptop chi tiết theo ID**
    @Transactional
    @Override
    public LaptopDTO getLaptopById(UUID id) {
        Laptop laptop = laptopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Laptop not found!"));
        return LaptopDTO.builder()
                .macId(laptop.getMacId())
                .mfg(laptop.getMFG())
                .status(laptop.getStatus())
                .laptopModelId(laptop.getLaptopModel().getId() != null ? laptop.getLaptopModel().getId() : null)
                .build();
    }

    // **3. Tạo mới Laptop**
    @Transactional
    @Override
    public LaptopDTO createLaptop(LaptopDTO laptopDTO) {
        Laptop laptop = Laptop.builder()
                .MFG(laptopDTO.getMfg())
                .status(laptopDTO.getStatus())
                .build();

        if(laptopDTO.getLaptopModelId() == null){
            throw  new IllegalArgumentException("LaptopModel cannot be null");
        }

        LaptopModel laptopModel = laptopModelRepository.findById(laptopDTO.getLaptopModelId())
                .orElseThrow(() -> new EntityNotFoundException("LaptopModel not found"));

        laptop.setLaptopModel(laptopModel);
//        laptopModel.addLaptop(laptop);

        Laptop laptopExisting = laptopRepository.save(laptop);

        return convertToDTO(laptopExisting);
    }

    // **4. Cập nhật Laptop**
    @Override
    @Transactional
    public LaptopDTO updateLaptop(UUID laptopId, LaptopDTO updatedLaptopDTO) {

        Laptop existingLaptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new EntityNotFoundException("Laptop not found"));

        LaptopModel newModel = laptopModelRepository.findById(updatedLaptopDTO.getLaptopModelId())
                .orElseThrow(() -> new EntityNotFoundException("LaptopModel not found"));

        if(existingLaptop.getLaptopModel() == null){
            throw  new IllegalArgumentException("LaptopModel cannot be null");
        }
        else if (!existingLaptop.getLaptopModel().getId().equals(updatedLaptopDTO.getLaptopModelId())) {
            existingLaptop.setLaptopModel(newModel);
        }

        existingLaptop.setMFG(updatedLaptopDTO.getMfg());
        existingLaptop.setStatus(updatedLaptopDTO.getStatus());

        Laptop laptopExisting = laptopRepository.save(existingLaptop);
        return convertToDTO(laptopExisting);
    }

    // **5. Xóa Laptop theo ID**
    @Transactional
    @Override
    public void deleteLaptop(UUID id) {
        Laptop laptop = laptopRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Laptop not found"));

//        LaptopModel laptopModel = laptop.getLaptopModel();
//        laptopModel.removeLaptop(laptop);

        laptopRepository.deleteById(id);
    }

    private LaptopDTO convertToDTO(Laptop laptop) {
        return LaptopDTO.builder()
                .macId(laptop.getMacId())
                .status(laptop.getStatus())
                .laptopModelId(laptop.getLaptopModel() == null ? null
                        : laptop.getLaptopModel().getId())
                .mfg(laptop.getMFG())
                .build();
    }
}