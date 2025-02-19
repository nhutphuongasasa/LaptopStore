package com.example.demo.Service.Impl;


import com.example.demo.DTO.CartDTO;
import com.example.demo.DTO.ChatDTO;
import com.example.demo.Models.Account;
import com.example.demo.Models.Cart;
import com.example.demo.Models.Chat;
import com.example.demo.Models.LaptopOnCart;
import com.example.demo.Repository.AccountRepository;
import com.example.demo.Repository.ChatRepository;
import com.example.demo.Service.ChatService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

    @Transactional
    @Override
    public List<ChatDTO> getAllChatsByAccountId(UUID accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new EntityNotFoundException("Account not found"));

        List<ChatDTO> chatDTOList = chatRepository.findBySenderId(account).stream()
                .map(chat -> ChatDTO.builder()
                        .id(chat.getId())
                        .senderId(chat.getSenderId().getId())
                        .receiverId(chat.getReceiverId().getId())
                        .message(chat.getMessage())
                        .createAt(chat.getCreateAt())
                        .build())
                .collect(Collectors.toList());

        chatRepository.findByReceiverId(account).stream()
                .map(chat -> ChatDTO.builder()
                        .id(chat.getId())
                        .senderId(chat.getSenderId().getId())
                        .receiverId(chat.getReceiverId().getId())
                        .message(chat.getMessage())
                        .createAt(chat.getCreateAt())
                        .build())
                .forEach(chatDTOList::add);

        return chatDTOList;
    }

    @Transactional
    @Override
    public ChatDTO getChatById(UUID id) {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chat not found with ID: " + id));
        return ChatDTO.builder()
                .id(chat.getId())
                .senderId(chat.getSenderId().getId())
                .receiverId(chat.getReceiverId().getId())
                .message(chat.getMessage())
                .createAt(chat.getCreateAt())
                .build();
    }

    @Transactional
    @Override
    public ChatDTO createChat(ChatDTO chatDTO) {
        Account sender = accountRepository.findById(chatDTO.getSenderId())
                .orElseThrow(() -> new EntityNotFoundException("Sender Account not found"));
        Account receiver = accountRepository.findById(chatDTO.getReceiverId())
                .orElseThrow(() -> new EntityNotFoundException("Receiver Account not found"));

        Chat chat = Chat.builder()
                .receiverId(receiver)
                .senderId(sender)
                .message(chatDTO.getMessage())
                .createAt(LocalDateTime.now())
                .build();

        Chat chatExisting = chatRepository.save(chat);

        sender.getChatSend().add(chat);
        receiver.getChatReceive().add(chat);

        accountRepository.save(sender);
        accountRepository.save(receiver);

        return convertToDTO(chatExisting);
    }

    @Transactional
    @Override
    public ChatDTO updateChat(UUID chatId, ChatDTO chatDTO) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Chat not found"));

        Account sender = accountRepository.findById(chatDTO.getSenderId())
                .orElseThrow(() -> new EntityNotFoundException("Sender not found"));
        Account receiver = accountRepository.findById(chatDTO.getReceiverId())
                .orElseThrow(() -> new EntityNotFoundException("Receiver not found"));

        chat.setMessage(chatDTO.getMessage());

        chat.setCreateAt(LocalDateTime.now());

        chatRepository.save(chat);

        return convertToDTO(chat);
    }

    @Transactional
    @Override
    public void deleteChat(UUID chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Chat not found"));

        Account sender = chat.getSenderId();
        Account receiver = chat.getReceiverId();

        sender.getChatSend().remove(chat);
        receiver.getChatReceive().remove(chat);

        chatRepository.delete(chat);
    }

    private ChatDTO convertToDTO(Chat chat) {
        return ChatDTO.builder()
                .id(chat.getId())
                .message(chat.getMessage())
                .createAt(chat.getCreateAt())
                .receiverId(chat.getReceiverId().getId())
                .senderId(chat.getSenderId().getId())
                .build();

    }
}