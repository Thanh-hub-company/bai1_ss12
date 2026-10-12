package com.example.promotionservice.service;

import com.example.promotionservice.publisher.PromotionPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionPublisher promotionPublisher;

    public void updatePromotionPrice(Long productId, BigDecimal newDiscountPrice) {
        // 1. Logic cập nhật giá trong DB bảng Promotions...

        // 2. Bắn sự kiện thời gian thực qua Redis Pub/Sub
        promotionPublisher.publishPromotionUpdate(productId);
    }
}
