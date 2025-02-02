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

            List<AddressDTO> addresses = addressService.getAllAddress(customerId);
            return ResponseEntity.ok(addresses);

    }

    // Lấy địa chỉ theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getAddressById(@PathVariable UUID id) {

            AddressDTO addressDTO = addressService.getAddressById(id);
            return ResponseEntity.ok(addressDTO);

    }

    // Tạo mới địa chỉ
    @PostMapping
    public ResponseEntity<?> createAddress(@RequestBody AddressDTO addressDTO) {

            addressService.createAddress(addressDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body("Address created successfully!");

    }

    // Cập nhật thông tin địa chỉ
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable UUID id, @RequestBody AddressDTO updatedAddress) {

            addressService.updateAddress(id, updatedAddress);
            return ResponseEntity.ok("Address updated successfully!");

    }

    // Xóa địa chỉ
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable UUID id) {

            addressService.deleteAddress(id);
            return ResponseEntity.ok("Address deleted successfully!");

    }
}