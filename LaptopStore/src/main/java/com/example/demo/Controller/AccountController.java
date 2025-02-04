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

            List<AccountDTO> accountDTOList = accountService.getAllAccounts();
            return ResponseEntity.ok(accountDTOList);

    }

    // Lấy tài khoản theo id
    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable UUID id) {

            AccountDTO accountDTO = accountService.getAccountById(id);
            return ResponseEntity.ok(accountDTO);

    }

    // Tạo tài khoản mới
    @PostMapping
    public ResponseEntity<String> createAccount(@RequestBody AccountDTO accountDTO) {
            accountService.createAccount(accountDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body("Account created successfully!");
    }

    // Cập nhật tài khoản
    @PutMapping("/{id}")
    public ResponseEntity<String> updateAccount(@PathVariable UUID id, @RequestBody AccountDTO updatedAccount) {
            accountService.updateAccount(id, updatedAccount);
            return ResponseEntity.ok("Account updated successfully!");
    }

    // Xóa tài khoản
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable UUID id) {
            accountService.deleteAccount(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}

