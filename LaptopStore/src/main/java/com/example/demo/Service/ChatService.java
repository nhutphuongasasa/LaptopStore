package com.example.demo.Service;

import com.example.demo.DTO.ChatDTO;

import java.util.List;
import java.util.UUID;

public interface ChatService {
    List<ChatDTO> getAllChats();

    ChatDTO getChatById(UUID id);

    void createChat(ChatDTO chatDTO);

    void updateChat(UUID chatId, ChatDTO chatDTO);

    void deleteChat(UUID chatId);
}