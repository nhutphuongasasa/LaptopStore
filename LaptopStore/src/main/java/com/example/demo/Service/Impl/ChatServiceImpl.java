package com.example.demo.Service.Impl;

import com.example.demo.DTO.ChatDTO;
import com.example.demo.Models.Account;
import com.example.demo.Models.Chat;
import com.example.demo.Repository.AccountRepository;
import com.example.demo.Repository.ChatRepository;
import com.example.demo.Service.ChatService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    public ChatServiceImpl(ChatRepository chatRepository, AccountRepository accountRepository, ModelMapper modelMapper) {
        this.chatRepository = chatRepository;
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ChatDTO> getAllChats() {
        return chatRepository.findAll().stream()
                .map(chat -> modelMapper.map(chat, ChatDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ChatDTO getChatById(UUID id) {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chat not found with ID: " + id));
        return modelMapper.map(chat, ChatDTO.class);
    }

    @Override
    public void createChat(ChatDTO chatDTO) {
        Chat chat = new Chat();

        Account sender = accountRepository.findById(chatDTO.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender Account not found with ID: " + chatDTO.getSenderId()));
        Account receiver = accountRepository.findById(chatDTO.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver Account not found with ID: " + chatDTO.getReceiverId()));

        chat.setSenderId(sender);
        chat.setReceiverId(receiver);
        chat.setMessage(chatDTO.getMessage());
        chat.setCreateAt(chatDTO.getCreateAt());

        chatRepository.save(chat);
    }

    @Override
    public void updateChat(UUID chatId, ChatDTO chatDTO) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found with ID: " + chatId));

        Account sender = accountRepository.findById(chatDTO.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender Account not found with ID: " + chatDTO.getSenderId()));
        Account receiver = accountRepository.findById(chatDTO.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver Account not found with ID: " + chatDTO.getReceiverId()));

        chat.setSenderId(sender);
        chat.setReceiverId(receiver);
        chat.setMessage(chatDTO.getMessage());
        chat.setCreateAt(chatDTO.getCreateAt());

        chatRepository.save(chat);
    }

    @Override
    public void deleteChat(UUID chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found with ID: " + chatId));
        chat.setSenderId(null);
        chat.setReceiverId(null);
        chatRepository.save(chat);
        chatRepository.delete(chat);
    }
}