package com.example.demo.Controller;

import com.example.demo.DTO.CommentDTO;
import com.example.demo.Service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 1. Lấy danh sách tất cả Comments
    @GetMapping
    public ResponseEntity<?> getAllComments() {
        try {
            List<CommentDTO> comments = commentService.getAllComments();
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching comments: " + e.getMessage());
        }
    }

    // 2. Lấy Comment theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCommentById(@PathVariable UUID id) {
        try {
            CommentDTO comment = commentService.getCommentById(id);
            return ResponseEntity.ok(comment);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment with ID " + id + " not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching comment by ID: " + e.getMessage());
        }
    }

    // 3. Tạo mới một Comment
    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CommentDTO commentDTO) {
        try {
            commentService.createComment(commentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Comment created successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating comment: " + e.getMessage());
        }
    }

    // 4. Cập nhật một Comment
    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable UUID id, @RequestBody CommentDTO commentDTO) {
        try {
            commentService.updateComment(id, commentDTO);
            return ResponseEntity.ok("Comment updated successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment with ID " + id + " not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating comment: " + e.getMessage());
        }
    }

    // 5. Xóa một Comment
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable UUID id) {
        try {
            commentService.deleteComment(id);
            return ResponseEntity.ok("Comment deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment with ID " + id + " not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting comment: " + e.getMessage());
        }
    }
}