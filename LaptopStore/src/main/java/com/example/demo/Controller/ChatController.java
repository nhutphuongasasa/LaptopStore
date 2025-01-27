package com.example.demo.Controller;

import com.example.demo.DTO.ChatDTO;
import com.example.demo.Service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // Lấy tất cả chat
    @GetMapping
    public ResponseEntity<?> getAllChats() {
        try {
            List<ChatDTO> chatDTOList = chatService.getAllChats();
            return ResponseEntity.ok(chatDTOList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: Unable to fetch chats. Server encountered an issue.");
        }
    }

    // Lấy chat theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getChatById(@PathVariable UUID id) {
        try {
            ChatDTO chatDTO = chatService.getChatById(id);
            return ResponseEntity.ok(chatDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: Unable to fetch chat. Server encountered an issue.");
        }
    }

    // Tạo một chat mới
    @PostMapping
    public ResponseEntity<String> createChat(@RequestBody ChatDTO chatDTO) {
        try {
            chatService.createChat(chatDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Chat created successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: Unable to create chat. Server encountered an issue.");
        }
    }

    // Cập nhật chat
    @PutMapping("/{id}")
    public ResponseEntity<String> updateChat(@PathVariable UUID id, @RequestBody ChatDTO chatDTO) {
        try {
            chatService.updateChat(id, chatDTO);
            return ResponseEntity.ok("Chat updated successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: Unable to update chat. Server encountered an issue.");
        }
    }

    // Xóa chat
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteChat(@PathVariable UUID id) {
        try {
            chatService.deleteChat(id);
            return ResponseEntity.ok("Chat deleted successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: Unable to delete chat. Server encountered an issue.");
        }
    }
}