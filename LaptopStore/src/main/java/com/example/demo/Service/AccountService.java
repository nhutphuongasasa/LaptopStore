package com.example.demo.Service;

import java.util.List;
import java.util.UUID;

import com.example.demo.DTO.AccountDTO;

public interface AccountService {
    public List<AccountDTO> getAllAccounts();
    public AccountDTO getAccountById(UUID id);
    public void createAccount(AccountDTO accountDTO);
    public void updateAccount(UUID id, AccountDTO updatedAccount);
    public void deleteAccount(UUID id);
}
