package com.example.demo.Service;

import com.example.demo.DTO.CommentDTO;

import java.util.List;
import java.util.UUID;

public interface CommentService {
    List<CommentDTO> getAllCommentsByAccountId(UUID accountId);
    CommentDTO getCommentById(UUID id);
    void createComment(CommentDTO commentDTO);
    void updateComment(UUID id, CommentDTO updatedComment);
    void deleteComment(UUID id);
}