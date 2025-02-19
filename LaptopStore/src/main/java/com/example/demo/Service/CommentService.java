package com.example.demo.Service;

import com.example.demo.DTO.CommentDTO;

import java.util.List;
import java.util.UUID;

public interface CommentService {
    List<CommentDTO> getAllCommentsByAccountId(UUID accountId);
    CommentDTO getCommentById(UUID id);
    CommentDTO createComment(CommentDTO commentDTO);
    CommentDTO updateComment(UUID id, CommentDTO updatedComment);
    void deleteComment(UUID id);
}