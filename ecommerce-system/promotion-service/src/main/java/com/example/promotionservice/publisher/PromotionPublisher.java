package com.example.promotionservice.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PromotionPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String TOPIC_NAME = "promotion-updates";

    public void publishPromotionUpdate(Long productId) {
        redisTemplate.convertAndSend(TOPIC_NAME, String.valueOf(productId));
        log.info("[PROMOTION-SERVICE] Đã bắn tín hiệu Pub/Sub đổi giá cho sản phẩm ID = {} vào channel '{}'", productId, TOPIC_NAME);
    }
}
