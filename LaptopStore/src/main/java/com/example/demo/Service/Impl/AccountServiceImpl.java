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


import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
@Transactional
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    
    private final ModelMapper modelMapper;

//    private AdminRepository adminRepository;
//
//    private CustomerRepository customerRepository;

    public AccountServiceImpl(AccountRepository accountRepository, ModelMapper modelMapper,AdminRepository adminRepository,CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
//        this.adminRepository = adminRepository;
//        this.customerRepository = customerRepository;
    }

    // Lấy danh sách tài khoản
    @Override
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
            .map(account -> modelMapper.map(account, AccountDTO.class))
            .collect(Collectors.toList());
    }

    // Lấy chi tiết một tài khoản
    @Override
    public AccountDTO getAccountById(UUID id) {
        Account account = accountRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Account Not Found"));
        return modelMapper.map(account, AccountDTO.class);
    }
    // Tạo mới tài khoản
    @Override
    public void createAccount(AccountDTO accountDTO) {
        if (accountRepository.findByEmail(accountDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists!");
        }
        Account account = modelMapper.map(accountDTO,Account.class);
        //set id = null de tu dong tao id
        account.setId(null);

        if(account.getRole().equals(Enums.role.ADMIN)){
            Admin admin = new Admin();
            account.setAdminId(admin);
        }else if(account.getRole().equals(Enums.role.CUSTOMER)){
            Customer customer = new Customer();
            account.setCustomerId(customer);
        }else{
            throw new RuntimeException("Role Is Wrong");
        }
        accountRepository.save(account);
    }


    // Cập nhật thông tin tài khoản
    @Override
    public void updateAccount(UUID id, AccountDTO updatedAccount) {
        Account existingAccount = accountRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Account not found"));
        Optional<Account> existingAccount1 = accountRepository.findByEmail(updatedAccount.getEmail());
        if(existingAccount1.isPresent()){
            throw new RuntimeException("Email already existed");
        }
        existingAccount = Account.builder()
                                    .name(updatedAccount.getName())
                                    .role(updatedAccount.getRole())
                                    .password(updatedAccount.getPassword())
                                    .email(updatedAccount.getEmail())
                                    .build();
        accountRepository.save(existingAccount);
    }

    // Xóa tài khoản
    @Override
    public void deleteAccount(UUID id) {
        if (!accountRepository.existsById(id)) {
            throw new RuntimeException("Account does not exist!");
        }
        accountRepository.deleteById(id);
    }

}
