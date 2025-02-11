package com.example.demo.Service;

import com.example.demo.DTO.ChatDTO;

import java.util.List;
import java.util.UUID;

public interface ChatService {
    List<ChatDTO> getAllChatsByAccountId(UUID accountId);

    ChatDTO getChatById(UUID id);

    ChatDTO createChat(ChatDTO chatDTO);

    ChatDTO updateChat(UUID chatId, ChatDTO chatDTO);

    void deleteChat(UUID chatId);
}