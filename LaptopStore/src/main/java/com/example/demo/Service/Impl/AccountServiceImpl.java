package com.example.demo.Service.Impl;


import com.example.demo.Common.Enums;
import com.example.demo.DTO.AccountDTO;
import com.example.demo.Models.Account;
import com.example.demo.Models.Admin;
import com.example.demo.Models.Customer;
import com.example.demo.Repository.AccountRepository;
import com.example.demo.Repository.AdminRepository;
import com.example.demo.Repository.CustomerRepository;
import com.example.demo.Service.AccountService;


import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final ModelMapper modelMapper;

    private final AdminRepository adminRepository;

    private final CustomerRepository customerRepository;

    public AccountServiceImpl(AccountRepository accountRepository, ModelMapper modelMapper,AdminRepository adminRepository,CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
        this.adminRepository = adminRepository;
        this.customerRepository = customerRepository;
    }

    // Lấy danh sách tài khoản
    @Transactional
    @Override
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
            .map(account -> AccountDTO.builder()
                    .id(account.getId())
                    .name(account.getName())
                    .email(account.getEmail())
                    .password(account.getPassword())
                    .role(account.getRole())
                    .build())
            .collect(Collectors.toList());
    }

    // Lấy chi tiết một tài khoản
    @Transactional
    @Override
    public AccountDTO getAccountById(UUID id) {
        Account account = accountRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        return AccountDTO.builder()
                .id(account.getId())
                .name(account.getName())
                .email(account.getEmail())
                .password(account.getPassword())
                .role(account.getRole())
                .build();
    }
    // Tạo mới tài khoản
    @Transactional
    @Override
    public AccountDTO createAccount(AccountDTO accountDTO) {
        if (accountRepository.findByEmail(accountDTO.getEmail()).isPresent()) {
            throw new EntityExistsException("Email already exists!");
        }
        Account account = Account.builder()
                .id(null)
                .email(accountDTO.getEmail())
                .name(accountDTO.getName())
                .password(accountDTO.getPassword())
                .role(accountDTO.getRole())
                .build();

        if(account.getRole().equals(Enums.role.ADMIN)){
            Admin admin = new Admin();
            admin.setAdminId(account);
            account.setAdminId(admin);
        }else if(account.getRole().equals(Enums.role.CUSTOMER)){
            Customer customer = new Customer();
            customer.setCustomerId(account);
            account.setCustomerId(customer);
        }else{
            throw new IllegalArgumentException("Role Is Wrong");
        }

        Account accountExisting = accountRepository.save(account);

        return convertToDTO(accountExisting);
    }


    // Cập nhật thông tin tài khoản
    @Transactional
    @Override
    public AccountDTO updateAccount(UUID id, AccountDTO updatedAccount) {
        Account existingAccount = accountRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        Optional<Account> existingAccount1 = accountRepository.findByEmail(updatedAccount.getEmail());

        if(existingAccount1.isPresent()){
            if(!existingAccount1.get().getId().equals(existingAccount.getId())){
                throw new EntityExistsException("Email already existed");
            }
        }

        existingAccount.setName(updatedAccount.getName());
        existingAccount.setEmail(updatedAccount.getEmail());
        existingAccount.setRole(updatedAccount.getRole());
        existingAccount.setPassword(updatedAccount.getPassword());

        Account account = accountRepository.save(existingAccount);

        return convertToDTO(account);
    }

    // Xóa tài khoản
    @Transactional
    @Override
    public void deleteAccount(UUID id) {
        Account account =accountRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        accountRepository.deleteById(id);
    }


    private AccountDTO convertToDTO(Account account) {
        return AccountDTO.builder()
                .id(account.getId())
                .email(account.getEmail())
                .password(account.getPassword())
                .name(account.getName())
                .role(account.getRole())
                .build();
    }
}
