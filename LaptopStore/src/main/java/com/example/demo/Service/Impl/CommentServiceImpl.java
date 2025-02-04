package com.example.demo.Service.Impl;

import com.example.demo.DTO.CommentDTO;
import com.example.demo.Models.Comment;
import com.example.demo.Models.Account;
import com.example.demo.Models.LaptopModel;
import com.example.demo.Repository.CommentRepository;
import com.example.demo.Repository.AccountRepository;
import com.example.demo.Repository.LaptopModelRepository;
import com.example.demo.Service.CommentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AccountRepository accountRepository;
    private final LaptopModelRepository laptopModelRepository;

    public CommentServiceImpl(CommentRepository commentRepository, AccountRepository accountRepository, LaptopModelRepository laptopModelRepository) {
        this.commentRepository = commentRepository;
        this.accountRepository = accountRepository;
        this.laptopModelRepository = laptopModelRepository;
    }

    // Lấy danh sách tất cả Comment
    @Transactional
    @Override
    public List<CommentDTO> getAllCommentsByAccountId(UUID accountId) {
        return commentRepository.findByAccountId(accountId).stream()
                .map(comment -> CommentDTO.builder()
                        .id(comment.getId())
                        .parentId(comment.getParent() == null ? null : comment.getParent().getId())
                        .body(comment.getBody())
                        .accountId(comment.getAccount().getId())
                        .laptopModelId(comment.getLaptopModel() == null ? null : comment.getLaptopModel().getId())
                        .replies(comment.getReplies() == null ? Collections.emptyList()
                                : comment.getReplies().stream().map(Comment :: getId).collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    // Lấy Comment theo ID
    @Transactional
    @Override
    public CommentDTO getCommentById(UUID id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        return CommentDTO.builder()
                .id(comment.getId())
                .parentId(comment.getParent() == null ? null : comment.getParent().getId())
                .body(comment.getBody())
                .accountId(comment.getAccount().getId())
                .laptopModelId(comment.getLaptopModel() == null ? null : comment.getLaptopModel().getId())
                .replies(comment.getReplies() == null ? Collections.emptyList()
                        : comment.getReplies().stream().map(Comment :: getId).collect(Collectors.toList()))
                .build();
    }

    // 3. Tạo một Comment mới
    @Override
    @Transactional
    public void createComment(CommentDTO commentDTO) {
        if(commentDTO.getBody() == null){
            throw  new IllegalArgumentException("body cannot be null");
        }

        Comment comment = Comment.builder()
                .replies(null)
                .id(null)
                .body(commentDTO.getBody())
                .build();

        Account account = accountRepository.findById(commentDTO.getAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        comment.setAccount(account);

        if(commentDTO.getLaptopModelId() == null){
            throw new IllegalArgumentException("LaptopModel cannot be null");
        } else{
            LaptopModel laptopModel = laptopModelRepository.findById(commentDTO.getLaptopModelId())
                    .orElseThrow(() -> new EntityNotFoundException("LaptopModel not found"));
            comment.setLaptopModel(laptopModel);

        }

        if (commentDTO.getParentId() != null) {
            Comment parentComment = commentRepository.findById(commentDTO.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent Comment not found"));
            comment.setParent(parentComment);
        }

        commentRepository.save(comment);
    }

    // 4. Cập nhật một Comment
    @Override
    @Transactional
    public void updateComment(UUID commentId, CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        comment.setBody(commentDTO.getBody());

        // Cập nhật Account
        if (commentDTO.getAccountId() != null) {
            Account account = accountRepository.findById(commentDTO.getAccountId())
                    .orElseThrow(() -> new EntityNotFoundException("Account not found"));
            comment.setAccount(account);
        }

        // Cập nhật LaptopModel nếu có
        if (commentDTO.getLaptopModelId() != null) {
            LaptopModel laptopModel = laptopModelRepository.findById(commentDTO.getLaptopModelId())
                    .orElseThrow(() -> new EntityNotFoundException("LaptopModel not found "));
            comment.setLaptopModel(laptopModel);
        }

        // Cập nhật Parent Comment nếu có
        if (commentDTO.getParentId() != null) {
            Comment parentComment = commentRepository.findById(commentDTO.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent Comment not found"));
            comment.setParent(parentComment);
        }

        if (commentDTO.getReplies() != null) {
            List<Comment> replies = commentDTO.getReplies().stream()
                    .map(replyId -> commentRepository.findById(replyId)
                            .orElseThrow(() -> new EntityNotFoundException("Reply Comment not found")))
                    .collect(Collectors.toList());

            comment.setReplies(replies);
        }

        commentRepository.save(comment);
    }

    // 5. Xóa Comment theo ID
    @Override
    @Transactional
    public void deleteComment(UUID id) {
        Comment commentExisting = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        commentRepository.delete(commentExisting);
    }
}