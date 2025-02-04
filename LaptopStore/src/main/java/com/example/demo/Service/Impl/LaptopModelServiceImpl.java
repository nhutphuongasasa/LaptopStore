package com.example.demo.Service.Impl;

import com.example.demo.DTO.LaptopModelDTO;
import com.example.demo.Models.*;
import com.example.demo.Repository.*;
import com.example.demo.Service.LaptopModelService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class LaptopModelServiceImpl implements LaptopModelService {

    private final LaptopModelRepository laptopModelRepository;
    private final ModelMapper modelMapper;
    private final LaptopRepository laptopRepository;
    private final ImageRepository imageRepository;
    private final CommentRepository commentRepository;
    private final LaptopOnCartRepository laptopOnCartRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final SaleRepository saleRepository;

    public LaptopModelServiceImpl(LaptopModelRepository laptopModelRepository,SaleRepository saleRepository, ModelMapper modelMapper,LaptopRepository laptopRepository, ImageRepository imageRepository, CommentRepository commentRepository, LaptopOnCartRepository laptopOnCartRepository, OrderDetailRepository orderDetailRepository) {
        this.laptopModelRepository = laptopModelRepository;
        this.modelMapper = modelMapper;
        this.laptopRepository = laptopRepository;
        this.imageRepository = imageRepository;
        this.commentRepository = commentRepository;
        this.laptopOnCartRepository = laptopOnCartRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.saleRepository =saleRepository;
    }

    // 1. Lấy tất cả LaptopModel
    @Override
    public List<LaptopModelDTO> getAllLaptopModels() {
        return laptopModelRepository.findAll().stream()
                .map(laptopModel -> getLaptopModelById(laptopModel.getId()))
                .collect(Collectors.toList());
    }


    // 2. Lấy LaptopModel theo ID
    @Override
    public LaptopModelDTO getLaptopModelById(UUID id) {
        // Tìm LaptopModel theo ID, nếu không tìm thấy thì ném ngoại lệ
        LaptopModel laptopModel = laptopModelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Laptop Model with ID " + id + " not found"));

        // Ánh xạ các thuộc tính cơ bản từ LaptopModel sang LaptopModelDTO
        LaptopModelDTO laptopModelDTO = modelMapper.map(laptopModel, LaptopModelDTO.class);

        // Ánh xạ thủ công các danh sách liên quan (list ID), đảm bảo không bị null
        laptopModelDTO.setLaptopIds(
                Optional.ofNullable(laptopModel.getLaptopList())
                        .orElseGet(Collections::emptyList)
                        .stream()
                        .map(Laptop::getMacId)
                        .collect(Collectors.toList())
        );

        laptopModelDTO.setImageIds(
                Optional.ofNullable(laptopModel.getImageList())
                        .orElseGet(Collections::emptyList)
                        .stream()
                        .map(Image::getId)
                        .collect(Collectors.toList())
        );

        laptopModelDTO.setCommentIds(
                Optional.ofNullable(laptopModel.getCommentList())
                        .orElseGet(Collections::emptyList)
                        .stream()
                        .map(Comment::getId)
                        .collect(Collectors.toList())
        );

        laptopModelDTO.setLaptopOnCartIds(
                Optional.ofNullable(laptopModel.getLaptopOnCartList())
                        .orElseGet(Collections::emptyList)
                        .stream()
                        .map(LaptopOnCart::getId)
                        .collect(Collectors.toList())
        );

        laptopModelDTO.setOrderDetailIds(
                Optional.ofNullable(laptopModel.getOrderDetailList())
                        .orElseGet(Collections::emptyList)
                        .stream()
                        .map(OrderDetail::getId)
                        .collect(Collectors.toList())
        );

        laptopModelDTO.setSaleIds(
                Optional.ofNullable(laptopModel.getSaleList())
                        .orElseGet(Collections::emptyList)
                        .stream()
                        .map(Sale::getId)
                        .collect(Collectors.toList())
        );

        // Trả về DTO được hoàn thiện với tất cả các thông tin cần thiết
        return laptopModelDTO;
    }


    // 3. Tạo LaptopModel mới
    @Override
    public void createLaptopModel(LaptopModelDTO laptopModelDTO) {
        LaptopModel laptopModel = modelMapper.map(laptopModelDTO, LaptopModel.class);
        laptopModel.setId(null);
        // 1. Tìm và ánh xạ danh sách Laptop
        if (laptopModelDTO.getLaptopIds() != null) {
            List<Laptop> laptops = laptopModelDTO.getLaptopIds().stream()
                    .map(id -> laptopRepository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("Laptop not found")))
                    .collect(Collectors.toList());
            laptopModel.setLaptopList(laptops); // Gán danh sách Laptop vào LaptopModel
        }

        // 2. Tìm và ánh xạ danh sách Image
        if (laptopModelDTO.getImageIds() != null) {
            List<Image> images = laptopModelDTO.getImageIds().stream()
                    .map(id -> imageRepository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("Image not found")))
                    .collect(Collectors.toList());
            laptopModel.setImageList(images); // Gán danh sách Image vào LaptopModel
        }

        // 3. Tìm và ánh xạ danh sách Comment
        if (laptopModelDTO.getCommentIds() != null) {
            List<Comment> comments = laptopModelDTO.getCommentIds().stream()
                    .map(id -> commentRepository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("Comment not found")))
                    .collect(Collectors.toList());
            laptopModel.setCommentList(comments); // Gán danh sách Comment vào LaptopModel
        }

        // 4. Tìm và ánh xạ danh sách LaptopOnCart
        if (laptopModelDTO.getLaptopOnCartIds() != null) {
            List<LaptopOnCart> laptopsOnCart = laptopModelDTO.getLaptopOnCartIds().stream()
                    .map(id -> laptopOnCartRepository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("LaptopOnCart not found")))
                    .collect(Collectors.toList());
            laptopModel.setLaptopOnCartList(laptopsOnCart); // Gán danh sách LaptopOnCart vào LaptopModel
        }

        // 5. Tìm và ánh xạ danh sách OrderDetail
        if (laptopModelDTO.getOrderDetailIds() != null) {
            List<OrderDetail> orderDetails = laptopModelDTO.getOrderDetailIds().stream()
                    .map(id -> orderDetailRepository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("OrderDetail not found")))
                    .collect(Collectors.toList());
            laptopModel.setOrderDetailList(orderDetails); // Gán danh sách OrderDetail vào LaptopModel
        }

        if (laptopModelDTO.getSaleIds() != null) {
            List<Sale> sales = laptopModelDTO.getSaleIds().stream()
                    .map(id -> saleRepository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("OrderDetail not found")))
                    .collect(Collectors.toList());
            laptopModel.setSaleList(sales); // Gán danh sách OrderDetail vào LaptopModel
        }

        // Lưu vào database
        laptopModelRepository.save(laptopModel);
    }

    // 4. Cập nhật LaptopModel
    @Override
    public void updateLaptopModel(UUID id, LaptopModelDTO laptopModelDTO) {
        LaptopModel existingLaptopModel = laptopModelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Laptop Model with ID " + id + " not found"));

        modelMapper.map(laptopModelDTO, existingLaptopModel);

        laptopModelRepository.save(existingLaptopModel);
    }

    // 5. Xóa LaptopModel
    @Override
    public void deleteLaptopModel(UUID id) {
        LaptopModel laptopModel = laptopModelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Laptop Model not found"));

        laptopModel.getLaptopList().removeIf(laptop -> true);
        laptopModel.getImageList().removeIf(image -> true);
        laptopModel.getCommentList().removeIf(comment -> true);
        laptopModel.getLaptopOnCartList().removeIf(laptopOnCart -> true);
        laptopModel.getOrderDetailList().removeIf(orderDetail -> true);
        laptopModel.getSaleList().removeIf(sale -> true);

        laptopModelRepository.delete(laptopModel);
    }
}