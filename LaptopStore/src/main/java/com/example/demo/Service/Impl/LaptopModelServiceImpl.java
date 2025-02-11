package com.example.demo.Service.Impl;

import com.example.demo.Common.Enums;
import com.example.demo.DTO.ImageDTO;
import com.example.demo.DTO.LaptopModelDTO;
import com.example.demo.Models.*;
import com.example.demo.Repository.*;
import com.example.demo.Service.LaptopModelService;
import jakarta.persistence.Column;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service

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
    @Transactional
    @Override
    public List<LaptopModelDTO> getAllLaptopModels() {
        return laptopModelRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 2. Lấy LaptopModel theo ID
    @Transactional
    @Override
    public LaptopModelDTO getLaptopModelById(UUID id) {
        // Tìm LaptopModel theo ID, nếu không tìm thấy thì ném ngoại lệ
        LaptopModel laptopModel = laptopModelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Laptop Model with ID " + id + " not found"));

        return convertToDTO(laptopModel);
    }
    @Transactional
    @Override
    public LaptopModelDTO createLaptopModel(LaptopModelDTO laptopModelDTO) {
        LaptopModel laptopModel = LaptopModel.builder()
                .id(null)
                .name(laptopModelDTO.getName())
                .branch(laptopModelDTO.getBranch())
                .cpu(laptopModelDTO.getCpu())
                .ram(laptopModelDTO.getRam())
                .storage(laptopModelDTO.getStorage())
                .display(laptopModelDTO.getDisplay())
                .color(laptopModelDTO.getColor())
                .price(laptopModelDTO.getPrice())
                .description(laptopModelDTO.getDescription())
                .build();

        // 1. Tìm và ánh xạ danh sách Laptop
        if (laptopModelDTO.getLaptopIds() != null) {
            List<Laptop> laptops = laptopModelDTO.getLaptopIds().stream()
                    .map(id -> {
                        Laptop laptop = laptopRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException("Laptop not found"));
                        laptop.setLaptopModel(laptopModel); // Gán quan hệ một chiều
                        return laptop;
                    })
                    .collect(Collectors.toList());
            laptopModel.setLaptopList(laptops); // Gán danh sách Laptop vào LaptopModel
        }

        // 2. Tìm và ánh xạ danh sách Image
        if (laptopModelDTO.getImageIds() != null) {
            List<Image> images = laptopModelDTO.getImageIds().stream()
                    .map(id -> {
                        Image image = imageRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException("Image not found"));
                        return image;
                    })
                    .collect(Collectors.toList());
            laptopModel.setImageList(images); // Gán danh sách Image vào LaptopModel
        }

        // 3. Tìm và ánh xạ danh sách Comment
        if (laptopModelDTO.getCommentIds() != null) {
            List<Comment> comments = laptopModelDTO.getCommentIds().stream()
                    .map(id -> {
                        Comment comment = commentRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
                        comment.setLaptopModel(laptopModel); // Gán quan hệ một chiều
                        return comment;
                    })
                    .collect(Collectors.toList());
            laptopModel.setCommentList(comments); // Gán danh sách Comment vào LaptopModel
        }

        // 4. Tìm và ánh xạ danh sách LaptopOnCart
        if (laptopModelDTO.getLaptopOnCartIds() != null) {
            List<LaptopOnCart> laptopsOnCart = laptopModelDTO.getLaptopOnCartIds().stream()
                    .map(id -> {
                        LaptopOnCart laptopOnCart = laptopOnCartRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException("LaptopOnCart not found"));
                        laptopOnCart.setLaptopModel(laptopModel); // Gán quan hệ một chiều
                        return laptopOnCart;
                    })
                    .collect(Collectors.toList());
            laptopModel.setLaptopOnCartList(laptopsOnCart); // Gán danh sách LaptopOnCart vào LaptopModel
        }

        // 5. Tìm và ánh xạ danh sách OrderDetail
        if (laptopModelDTO.getOrderDetailIds() != null) {
            List<OrderDetail> orderDetails = laptopModelDTO.getOrderDetailIds().stream()
                    .map(id -> {
                        OrderDetail orderDetail = orderDetailRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException("OrderDetail not found"));
                        orderDetail.setLaptopModel(laptopModel); // Gán quan hệ một chiều
                        return orderDetail;
                    })
                    .collect(Collectors.toList());
            laptopModel.setOrderDetailList(orderDetails); // Gán danh sách OrderDetail vào LaptopModel
        }

        if (laptopModelDTO.getSaleIds() != null) {
            List<Sale> sales = laptopModelDTO.getSaleIds().stream()
                    .map(id -> {
                        Sale sale = saleRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException("Sale not found"));
                        return sale;
                    })
                    .collect(Collectors.toList());
            laptopModel.setSaleList(sales); // Gán danh sách OrderDetail vào LaptopModel
        }

        // Lưu vào database
        LaptopModel laptopModelExisting = laptopModelRepository.save(laptopModel);

        return convertToDTO(laptopModelExisting);
    }

    // 4. Cập nhật LaptopModel
    @Transactional
    @Override
    public LaptopModelDTO updateLaptopModel(UUID id, LaptopModelDTO laptopModelDTO) {
        LaptopModel existingLaptopModel = laptopModelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Laptop Model with ID " + id + " not found"));

        BeanUtils.copyProperties(laptopModelDTO, existingLaptopModel, "id");

        LaptopModel laptopModel = laptopModelRepository.save(existingLaptopModel);
        return convertToDTO(laptopModel);
    }

    // 5. Xóa LaptopModel

    @Override
    public void deleteLaptopModel(UUID id) {
        LaptopModel laptopModel = laptopModelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Laptop Model not found"));

        laptopModel.getSaleList().forEach(sale -> sale.setLaptopModelList(null));
        laptopModel.getLaptopList().forEach(laptop -> laptop.setLaptopModel(null));
        laptopModel.getImageList().forEach(image -> image.setLaptopModelList(null));
        laptopModel.getCommentList().forEach(comment -> comment.setLaptopModel(null));
        laptopModel.getLaptopOnCartList().forEach(laptopOnCart -> laptopOnCart.setLaptopModel(null));
        laptopModel.getOrderDetailList().forEach(orderDetail -> orderDetail.setLaptopModel(null));

        laptopModelRepository.delete(laptopModel);
    }

    private LaptopModelDTO convertToDTO(LaptopModel laptopModel) {
        LaptopModelDTO laptopModelDTO = LaptopModelDTO.builder()
                .id(laptopModel.getId())
                .name(laptopModel.getName())
                .branch(laptopModel.getBranch())
                .cpu(laptopModel.getCpu())
                .ram(laptopModel.getRam())
                .storage(laptopModel.getStorage())
                .display(laptopModel.getDisplay())
                .color(laptopModel.getColor())
                .price(laptopModel.getPrice())
                .description(laptopModel.getDescription())
                .build();

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

        return laptopModelDTO;
    }
}