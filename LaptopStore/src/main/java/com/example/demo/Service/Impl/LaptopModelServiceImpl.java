package com.example.demo.Service.Impl;

import com.example.demo.DTO.LaptopModelDTO;
import com.example.demo.Models.*;
import com.example.demo.Repository.*;
import com.example.demo.Service.LaptopModelService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
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

    public LaptopModelServiceImpl(LaptopModelRepository laptopModelRepository, ModelMapper modelMapper,LaptopRepository laptopRepository, ImageRepository imageRepository, CommentRepository commentRepository, LaptopOnCartRepository laptopOnCartRepository, OrderDetailRepository orderDetailRepository) {
        this.laptopModelRepository = laptopModelRepository;
        this.modelMapper = modelMapper;
        this.laptopRepository = laptopRepository;
        this.imageRepository = imageRepository;
        this.commentRepository = commentRepository;
        this.laptopOnCartRepository = laptopOnCartRepository;
        this.orderDetailRepository = orderDetailRepository;
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

        // Ánh xạ thủ công các danh sách liên quan (list ID)
        laptopModelDTO.setLaptopIds(
                laptopModel.getLaptopList().stream()
                        .map(Laptop::getId) // Chỉ lấy ID từ Laptop
                        .collect(Collectors.toList())
        );

        laptopModelDTO.setImageIds(
                laptopModel.getImageList().stream()
                        .map(Image::getId) // Chỉ lấy ID từ Image
                        .collect(Collectors.toList())
        );

        laptopModelDTO.setCommentIds(
                laptopModel.getCommentList().stream()
                        .map(Comment::getId) // Chỉ lấy ID từ Comment
                        .collect(Collectors.toList())
        );

        laptopModelDTO.setLaptopOnCartIds(
                laptopModel.getLaptopOnCartList().stream()
                        .map(LaptopOnCart::getId) // Chỉ lấy ID từ LaptopOnCart
                        .collect(Collectors.toList())
        );

        laptopModelDTO.setOrderDetailIds(
                laptopModel.getOrderDetailList().stream()
                        .map(OrderDetail::getId) // Chỉ lấy ID từ OrderDetail
                        .collect(Collectors.toList())
        );

        // Trả về DTO được hoàn thiện với tất cả các thông tin cần thiết
        return laptopModelDTO;
    }

    // 3. Tạo LaptopModel mới
    @Override
    public void createLaptopModel(LaptopModelDTO laptopModelDTO) {
        // Ánh xạ các thuộc tính cơ bản từ DTO sang Entity
        LaptopModel laptopModel = modelMapper.map(laptopModelDTO, LaptopModel.class);

        // 1. Tìm và ánh xạ danh sách Laptop
        if (laptopModelDTO.getLaptopIds() != null) {
            List<Laptop> laptops = laptopModelDTO.getLaptopIds().stream()
                    .map(id -> laptopRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Laptop with ID " + id + " not found")))
                    .collect(Collectors.toList());
            laptopModel.setLaptopList(laptops); // Gán danh sách Laptop vào LaptopModel
        }

        // 2. Tìm và ánh xạ danh sách Image
        if (laptopModelDTO.getImageIds() != null) {
            List<Image> images = laptopModelDTO.getImageIds().stream()
                    .map(id -> imageRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Image with ID " + id + " not found")))
                    .collect(Collectors.toList());
            laptopModel.setImageList(images); // Gán danh sách Image vào LaptopModel
        }

        // 3. Tìm và ánh xạ danh sách Comment
        if (laptopModelDTO.getCommentIds() != null) {
            List<Comment> comments = laptopModelDTO.getCommentIds().stream()
                    .map(id -> commentRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Comment with ID " + id + " not found")))
                    .collect(Collectors.toList());
            laptopModel.setCommentList(comments); // Gán danh sách Comment vào LaptopModel
        }

        // 4. Tìm và ánh xạ danh sách LaptopOnCart
        if (laptopModelDTO.getLaptopOnCartIds() != null) {
            List<LaptopOnCart> laptopsOnCart = laptopModelDTO.getLaptopOnCartIds().stream()
                    .map(id -> laptopOnCartRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("LaptopOnCart with ID " + id + " not found")))
                    .collect(Collectors.toList());
            laptopModel.setLaptopOnCartList(laptopsOnCart); // Gán danh sách LaptopOnCart vào LaptopModel
        }

        // 5. Tìm và ánh xạ danh sách OrderDetail
        if (laptopModelDTO.getOrderDetailIds() != null) {
            List<OrderDetail> orderDetails = laptopModelDTO.getOrderDetailIds().stream()
                    .map(id -> orderDetailRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("OrderDetail with ID " + id + " not found")))
                    .collect(Collectors.toList());
            laptopModel.setOrderDetailList(orderDetails); // Gán danh sách OrderDetail vào LaptopModel
        }

        // Lưu vào database
        laptopModelRepository.save(laptopModel);
    }

    // 4. Cập nhật LaptopModel
    @Override
    public void updateLaptopModel(UUID id, LaptopModelDTO laptopModelDTO) {
        // 1. Tìm LaptopModel hiện tại (nếu không tồn tại thì ném ngoại lệ)
        LaptopModel existingLaptopModel = laptopModelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Laptop Model with ID " + id + " not found"));

        // 2. Cập nhật các thuộc tính cơ bản từ DTO vào thực thể đã tồn tại
        modelMapper.map(laptopModelDTO, existingLaptopModel);

        // 3. Xử lý danh sách Laptop
        if (laptopModelDTO.getLaptopIds() != null) {
            List<Laptop> updatedLaptops = laptopModelDTO.getLaptopIds().stream()
                    .map(laptopId -> laptopRepository.findById(laptopId)
                            .orElseThrow(() -> new RuntimeException("Laptop with ID " + laptopId + " not found"))
                    )
                    .collect(Collectors.toList());
            existingLaptopModel.setLaptopList(updatedLaptops);
        }

        // 4. Xử lý danh sách Image
        if (laptopModelDTO.getImageIds() != null) {
            List<Image> updatedImages = laptopModelDTO.getImageIds().stream()
                    .map(imageId -> imageRepository.findById(imageId)
                            .orElseThrow(() -> new RuntimeException("Image with ID " + imageId + " not found"))
                    )
                    .collect(Collectors.toList());
            existingLaptopModel.setImageList(updatedImages);
        }

        // 5. Xử lý danh sách Comment
        if (laptopModelDTO.getCommentIds() != null) {
            List<Comment> updatedComments = laptopModelDTO.getCommentIds().stream()
                    .map(commentId -> commentRepository.findById(commentId)
                            .orElseThrow(() -> new RuntimeException("Comment with ID " + commentId + " not found"))
                    )
                    .collect(Collectors.toList());
            existingLaptopModel.setCommentList(updatedComments);
        }

        // 6. Xử lý danh sách LaptopOnCart
        if (laptopModelDTO.getLaptopOnCartIds() != null) {
            List<LaptopOnCart> updatedLaptopsOnCart = laptopModelDTO.getLaptopOnCartIds().stream()
                    .map(laptopOnCartId -> laptopOnCartRepository.findById(laptopOnCartId)
                            .orElseThrow(() -> new RuntimeException("LaptopOnCart with ID " + laptopOnCartId + " not found"))
                    )
                    .collect(Collectors.toList());
            existingLaptopModel.setLaptopOnCartList(updatedLaptopsOnCart);
        }

        // 7. Xử lý danh sách OrderDetail
        if (laptopModelDTO.getOrderDetailIds() != null) {
            List<OrderDetail> updatedOrderDetails = laptopModelDTO.getOrderDetailIds().stream()
                    .map(orderDetailId -> orderDetailRepository.findById(orderDetailId)
                            .orElseThrow(() -> new RuntimeException("OrderDetail with ID " + orderDetailId + " not found"))
                    )
                    .collect(Collectors.toList());
            existingLaptopModel.setOrderDetailList(updatedOrderDetails);
        }

        // 8. Lưu lại thực thể đã cập nhật vào cơ sở dữ liệu
        laptopModelRepository.save(existingLaptopModel);
    }

    // 5. Xóa LaptopModel
    @Override
    public void deleteLaptopModel(UUID id) {
        LaptopModel laptopModel = laptopModelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Laptop Model with ID " + id + " not found"));
        laptopModel.setLaptopList(null);
        laptopModel.setImageList(null);
        laptopModel.setCommentList(null);
        laptopModel.setLaptopOnCartList(null);
        laptopModel.setOrderDetailList(null);
        laptopModelRepository.save(laptopModel);
        laptopModelRepository.delete(laptopModel);
    }
}