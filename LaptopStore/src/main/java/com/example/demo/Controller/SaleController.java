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

    // Lấy danh sách tất cả Sale
    @GetMapping
    public ResponseEntity<?> getAllSales() {
            List<SaleDTO> sales = saleService.getAllSales();
            return ResponseEntity.ok(sales);
    }

    // Lấy Sale theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getSaleById(@PathVariable UUID id) {
            SaleDTO saleDTO = saleService.getSaleById(id);
            return ResponseEntity.ok(saleDTO);
    }

    // Tạo mới một Sale
    @PostMapping
    public ResponseEntity<?> createSale(@RequestBody SaleDTO saleDTO) {
            saleService.createSale(saleDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Sale created successfully.");
    }


    // Xóa Sale theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSale(@PathVariable UUID id) {
            saleService.deleteSale(id);
            return ResponseEntity.ok("Sale deleted successfully!");
    }

    // Cập nhật Sale theo ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSale(@PathVariable UUID id, @RequestBody SaleDTO saleDTO) {
            saleService.updateSale(id, saleDTO);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
