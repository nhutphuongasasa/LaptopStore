package com.example.demo.Service.Impl;

import com.example.demo.Common.AccountNotFoundException;
import com.example.demo.Common.ChatNotFoundException;
import com.example.demo.DTO.ChatDTO;
import com.example.demo.Models.Account;
import com.example.demo.Models.Chat;
import com.example.demo.Repository.AccountRepository;
import com.example.demo.Repository.ChatRepository;
import com.example.demo.Service.ChatService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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

    @Override
    public List<ChatDTO> getAllChatsByAccountId(UUID accountId) {

        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException("Account not found"));


        List<ChatDTO> chatDTOList = chatRepository.findBySenderId(account).stream()
                .map(chat -> modelMapper.map(chat, ChatDTO.class))
                .collect(Collectors.toList());


        chatRepository.findByReceiverId(account).stream()
                .map(chat -> modelMapper.map(chat, ChatDTO.class))
                .forEach(chatDTOList::add);

        return chatDTOList;
    }


    @Override
    public ChatDTO getChatById(UUID id) {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chat not found with ID: " + id));
        return modelMapper.map(chat, ChatDTO.class);
    }

    @Override
    public void createChat(ChatDTO chatDTO) {
        Account sender = accountRepository.findById(chatDTO.getSenderId())
                .orElseThrow(() -> new AccountNotFoundException("Sender Account not found"));
        Account receiver = accountRepository.findById(chatDTO.getReceiverId())
                .orElseThrow(() -> new AccountNotFoundException("Receiver Account not found"));

        // Sử dụng builder để tạo đối tượng Chat
        Chat chat = Chat.builder()
                .receiverId(receiver)
                .senderId(sender)
                .message(chatDTO.getMessage())
                .createAt(LocalDateTime.now())
                .build(); // Tạo đối tượng Chat và thiết lập tất cả các thuộc tính

        sender.getChatSend().add(chat);
        receiver.getChatReceive().add(chat);

        accountRepository.save(sender);
        accountRepository.save(receiver);
    }


    @Override
    public void updateChat(UUID chatId, ChatDTO chatDTO) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatNotFoundException("Chat not found"));

        Account sender = accountRepository.findById(chatDTO.getSenderId())
                .orElseThrow(() -> new AccountNotFoundException("Sender not found"));
        Account receiver = accountRepository.findById(chatDTO.getReceiverId())
                .orElseThrow(() -> new AccountNotFoundException("Receiver not found"));

        chat.setMessage(chatDTO.getMessage());

        chat.setCreateAt(LocalDateTime.now());

        chatRepository.save(chat);
    }

    @Override
    public void deleteChat(UUID chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatNotFoundException("Chat not found"));

        Account sender = chat.getSenderId();
        Account receiver = chat.getReceiverId();

        sender.getChatSend().remove(chat);
        receiver.getChatReceive().remove(chat);

        chatRepository.delete(chat);

        sender.getChatSend();
        receiver.getChatReceive();

    }
}