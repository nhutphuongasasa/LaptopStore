package com.example.demo.Service.Impl;


import com.example.demo.DTO.ChatDTO;
import com.example.demo.Models.Account;
import com.example.demo.Models.Chat;
import com.example.demo.Repository.AccountRepository;
import com.example.demo.Repository.ChatRepository;
import com.example.demo.Service.ChatService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Transactional
    @Override
    public List<ChatDTO> getAllChatsByAccountId(UUID accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new EntityNotFoundException("Account not found"));

        List<ChatDTO> chatDTOList = chatRepository.findBySenderId(account).stream()
                .map(chat -> modelMapper.map(chat, ChatDTO.class))
                .collect(Collectors.toList());

        chatRepository.findByReceiverId(account).stream()
                .map(chat -> modelMapper.map(chat, ChatDTO.class))
                .forEach(chatDTOList::add);

        return chatDTOList;
    }

    @Transactional
    @Override
    public ChatDTO getChatById(UUID id) {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chat not found with ID: " + id));
        return modelMapper.map(chat, ChatDTO.class);
    }

    @Transactional
    @Override
    public void createChat(ChatDTO chatDTO) {
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

        sender.getChatSend().add(chat);
        receiver.getChatReceive().add(chat);

        accountRepository.save(sender);
        accountRepository.save(receiver);
    }

    @Transactional
    @Override
    public void updateChat(UUID chatId, ChatDTO chatDTO) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Chat not found"));

        Account sender = accountRepository.findById(chatDTO.getSenderId())
                .orElseThrow(() -> new EntityNotFoundException("Sender not found"));
        Account receiver = accountRepository.findById(chatDTO.getReceiverId())
                .orElseThrow(() -> new EntityNotFoundException("Receiver not found"));

        chat.setMessage(chatDTO.getMessage());

        chat.setCreateAt(LocalDateTime.now());

        chatRepository.save(chat);
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
}