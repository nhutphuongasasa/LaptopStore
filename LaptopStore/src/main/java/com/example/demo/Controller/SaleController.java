package com.example.demo.Controller;

import com.example.demo.DTO.SaleDTO;
import com.example.demo.Service.SaleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sales") // Base URL cho Sale
public class SaleController {
    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    // 1. Lấy danh sách tất cả Sale
    @GetMapping
    public ResponseEntity<?> getAllSales() {
        try {
            List<SaleDTO> sales = saleService.getAllSales();
            return ResponseEntity.ok(sales);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching sales: " + e.getMessage());
        }
    }

    // 2. Lấy Sale theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getSaleById(@PathVariable UUID id) {
        try {
            SaleDTO saleDTO = saleService.getSaleById(id);
            return ResponseEntity.ok(saleDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Sale not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching sale: " + e.getMessage());
        }
    }

    // 3. Tạo mới một Sale
    @PostMapping
    public ResponseEntity<?> createSale(@RequestBody SaleDTO saleDTO) {
        try {
            saleService.createSale(saleDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Sale created successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid Sale data: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating sale: " + e.getMessage());
        }
    }

    // 4. Gắn LaptopModels vào Sale
    @PostMapping("/{saleId}/attach")
    public ResponseEntity<?> attachLaptopModelsToSale(
            @PathVariable UUID saleId,
            @RequestBody List<UUID> laptopModelIds) {
        try {
            saleService.attachLaptopModelsToSale(saleId, laptopModelIds);
            return ResponseEntity.ok("Laptop models attached to Sale successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Sale or LaptopModel not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error attaching laptop models to sale: " + e.getMessage());
        }
    }

    // 5. Xóa Sale theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSale(@PathVariable UUID id) {
        try {
            saleService.deleteSale(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Sale not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting sale: " + e.getMessage());
        }
    }

    // 6. Cập nhật Sale theo ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSale(@PathVariable UUID id, @RequestBody SaleDTO saleDTO) {
        try {
            saleService.updateSale(id, saleDTO);
            return ResponseEntity.ok("Sale updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid Sale data: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Sale not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating sale: " + e.getMessage());
        }
    }
}
