package com.example.demo.Controller;

import com.example.demo.DTO.AddressDTO;
import com.example.demo.Service.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    // Lấy tất cả địa chỉ của một khách hàng
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getAllAddress(@PathVariable UUID customerId) {
        try {
            List<AddressDTO> addresses = addressService.getAllAddress(customerId);
            return ResponseEntity.ok(addresses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }

    // Lấy địa chỉ theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getAddressById(@PathVariable UUID id) {
        try {
            AddressDTO addressDTO = addressService.getAddressById(id);
            return ResponseEntity.ok(addressDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }

    // Tạo mới địa chỉ
    @PostMapping
    public ResponseEntity<?> createAddress(@RequestBody AddressDTO addressDTO) {
        try {
            addressService.createAddress(addressDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body("Address created successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }

    // Cập nhật thông tin địa chỉ
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable UUID id, @RequestBody AddressDTO updatedAddress) {
        try {
            addressService.updateAddress(id, updatedAddress);
            return ResponseEntity.ok("Address updated successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }

    // Xóa địa chỉ
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable UUID id) {
        try {
            addressService.deleteAddress(id);
            return ResponseEntity.ok("Address deleted successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
}