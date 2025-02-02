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
    @GetMapping("/{accountId}")
    public ResponseEntity<?> getAllChatsByAccountId(@PathVariable UUID accountId) {
            List<ChatDTO> chatDTOList = chatService.getAllChatsByAccountId(accountId);
            return ResponseEntity.ok(chatDTOList);
    }


    // Lấy chat theo ID
//    @GetMapping("/{id}")
//    public ResponseEntity<?> getChatById(@PathVariable UUID id) {
//        try {
//            ChatDTO chatDTO = chatService.getChatById(id);
//            return ResponseEntity.ok(chatDTO);
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("Error: " + e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error: Unable to fetch chat. Server encountered an issue.");
//        }
//    }

    // Tạo một chat mới
    @PostMapping
    public ResponseEntity<String> createChat(@RequestBody ChatDTO chatDTO) {
            chatService.createChat(chatDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Chat created successfully!");
    }

    // Cập nhật chat
    @PutMapping("/{id}")
    public ResponseEntity<String> updateChat(@PathVariable UUID id, @RequestBody ChatDTO chatDTO) {

            chatService.updateChat(id, chatDTO);
            return ResponseEntity.ok("Chat updated successfully!");
    }

    // Xóa chat
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteChat(@PathVariable UUID id) {
            chatService.deleteChat(id);
            return ResponseEntity.ok("Chat deleted successfully!");
    }
}