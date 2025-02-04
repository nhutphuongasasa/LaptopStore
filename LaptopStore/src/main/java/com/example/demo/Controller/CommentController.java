package com.example.demo.Controller;

import com.example.demo.DTO.CommentDTO;
import com.example.demo.Service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // Lấy danh sách tất cả Comments
    @GetMapping("by-account/{accountId}")
    public ResponseEntity<?> getAllComments(@PathVariable UUID accountId) {
            List<CommentDTO> comments = commentService.getAllCommentsByAccountId(accountId);
            return ResponseEntity.ok(comments);
    }

    // Lấy Comment theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCommentById(@PathVariable UUID id) {
            CommentDTO comment = commentService.getCommentById(id);
            return ResponseEntity.ok(comment);
    }

    // 3. Tạo mới một Comment
    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CommentDTO commentDTO) {
            commentService.createComment(commentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Comment created successfully.");
    }

    // Cập nhật một Comment
    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable UUID id, @RequestBody CommentDTO commentDTO) {

            commentService.updateComment(id, commentDTO);
            return ResponseEntity.ok("Comment updated successfully.");
    }

    // 5. Xóa một Comment
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable UUID id) {

            commentService.deleteComment(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}