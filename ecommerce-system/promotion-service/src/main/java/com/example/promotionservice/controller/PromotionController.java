package com.example.promotionservice.controller;

import com.example.promotionservice.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @PutMapping("/products/{productId}")
    public ResponseEntity<String> updatePromotion(@PathVariable Long productId,
                                                  @RequestParam BigDecimal discountPrice) {
        promotionService.updatePromotionPrice(productId, discountPrice);
        return ResponseEntity.ok("Cập nhật khuyến mãi thành công & đã gửi tín hiệu xóa Cache toàn hệ thống!");
    }
}
