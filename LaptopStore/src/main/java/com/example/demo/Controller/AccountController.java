package com.example.demo.Controller;

import com.example.demo.DTO.AccountDTO;
import com.example.demo.Service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // Lấy tất cả tài khoản
    @GetMapping
    public ResponseEntity<?> getAllAccounts() {
        try {
            List<AccountDTO> accountDTOList = accountService.getAllAccounts();
            return ResponseEntity.ok(accountDTOList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: Server encountered an issue.");
        }
    }

    // Lấy tài khoản theo id
    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable UUID id) {
        try {
            AccountDTO accountDTO = accountService.getAccountById(id);
            return ResponseEntity.ok(accountDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: Server encountered an issue.");
        }
    }

    // Tạo tài khoản mới
    @PostMapping
    public ResponseEntity<String> createAccount(@RequestBody AccountDTO accountDTO) {
        try {
            accountService.createAccount(accountDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body("Account created successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: Server encountered an issue.");
        }
    }

    // Cập nhật tài khoản
    @PutMapping("/{id}")
    public ResponseEntity<String> updateAccount(@PathVariable UUID id, @RequestBody AccountDTO updatedAccount) {
        try {
            accountService.updateAccount(id, updatedAccount);
            return ResponseEntity.ok("Account updated successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: Server encountered an issue.");
        }
    }

    // Xóa tài khoản
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable UUID id) {
        try {
            accountService.deleteAccount(id);
            return ResponseEntity.ok("Account deleted successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: Unable to delete account. Please try again later.");
        }
    }
}

