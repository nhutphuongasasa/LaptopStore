package com.example.demo.Service.Impl;

import com.example.demo.DTO.CommentDTO;
import com.example.demo.Models.Comment;
import com.example.demo.Models.Account;
import com.example.demo.Models.LaptopModel;
import com.example.demo.Repository.CommentRepository;
import com.example.demo.Repository.AccountRepository;
import com.example.demo.Repository.LaptopModelRepository;
import com.example.demo.Service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AccountRepository accountRepository;
    private final LaptopModelRepository laptopModelRepository;
    private final ModelMapper modelMapper;

    public CommentServiceImpl(CommentRepository commentRepository, AccountRepository accountRepository,
                              LaptopModelRepository laptopModelRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.accountRepository = accountRepository;
        this.laptopModelRepository = laptopModelRepository;
        this.modelMapper = modelMapper;
    }

    // 1. Lấy danh sách tất cả Comment
    @Override
    public List<CommentDTO> getAllComments() {
        return commentRepository.findAll().stream()
                .map(comment -> {
                    CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
                    // Lấy danh sách reply IDs nếu có
                    if (comment.getReplies() != null) {
                        commentDTO.setReplies(
                                comment.getReplies().stream()
                                        .map(Comment::getId)
                                        .collect(Collectors.toList())
                        );
                    }
                    return commentDTO;
                })
                .collect(Collectors.toList());
    }

    // 2. Lấy Comment theo ID
    @Override
    public CommentDTO getCommentById(UUID id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + id));

        CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);

        // Lấy danh sách reply IDs nếu có
        if (comment.getReplies() != null) {
            commentDTO.setReplies(
                    comment.getReplies().stream()
                            .map(Comment::getId)
                            .collect(Collectors.toList())
            );
        }
        return commentDTO;
    }

    // 3. Tạo một Comment mới
    @Override
    public void createComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setBody(commentDTO.getBody());

        // Gắn Account
        Account account = accountRepository.findById(commentDTO.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + commentDTO.getAccountId()));
        comment.setAccount(account);

        // Gắn LaptopModel nếu có
        if (commentDTO.getLaptopModelId() != null) {
            LaptopModel laptopModel = laptopModelRepository.findById(commentDTO.getLaptopModelId())
                    .orElseThrow(() -> new RuntimeException("LaptopModel not found with ID: " + commentDTO.getLaptopModelId()));
            comment.setLaptopModel(laptopModel);
        }

        // Gắn Parent Comment nếu có
        if (commentDTO.getParentId() != null) {
            Comment parentComment = commentRepository.findById(commentDTO.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent Comment not found with ID: " + commentDTO.getParentId()));
            comment.setParent(parentComment);
        }

        commentRepository.save(comment);
    }

    // 4. Cập nhật một Comment
    @Override
    public void updateComment(UUID commentId, CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + commentId));

        comment.setBody(commentDTO.getBody());

        // Cập nhật Account
        if (commentDTO.getAccountId() != null) {
            Account account = accountRepository.findById(commentDTO.getAccountId())
                    .orElseThrow(() -> new RuntimeException("Account not found with ID: " + commentDTO.getAccountId()));
            comment.setAccount(account);
        }

        // Cập nhật LaptopModel nếu có
        if (commentDTO.getLaptopModelId() != null) {
            LaptopModel laptopModel = laptopModelRepository.findById(commentDTO.getLaptopModelId())
                    .orElseThrow(() -> new RuntimeException("LaptopModel not found with ID: " + commentDTO.getLaptopModelId()));
            comment.setLaptopModel(laptopModel);
        }

        // Cập nhật Parent Comment nếu có
        if (commentDTO.getParentId() != null) {
            Comment parentComment = commentRepository.findById(commentDTO.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent Comment not found with ID: " + commentDTO.getParentId()));
            comment.setParent(parentComment);
        }

        commentRepository.save(comment);
    }

    // 5. Xóa Comment theo ID
    @Override
    public void deleteComment(UUID id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + id));

        // Xóa liên kết giữa comment này và replies của nó
        if (comment.getReplies() != null) {
            comment.getReplies().forEach(reply -> reply.setParent(null));
        }

        commentRepository.delete(comment);
    }
}