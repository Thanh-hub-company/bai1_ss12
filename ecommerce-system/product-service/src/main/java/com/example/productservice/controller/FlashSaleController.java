package com.example.productservice.controller;

import com.example.productservice.service.FlashSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/flash-sale")
@RequiredArgsConstructor
public class FlashSaleController {

    private final FlashSaleService flashSaleService;

    @PostMapping("/buy")
    public ResponseEntity<String> buyProduct(@RequestParam Long productId,
                                             @RequestParam(defaultValue = "1") Integer quantity) {
        if (quantity == null || quantity < 1) {
            return ResponseEntity.badRequest().body("Số lượng phải lớn hơn 0");
        }
        String result = flashSaleService.buyProductFlashSale(productId, quantity);
        return ResponseEntity.ok(result);
    }
}
