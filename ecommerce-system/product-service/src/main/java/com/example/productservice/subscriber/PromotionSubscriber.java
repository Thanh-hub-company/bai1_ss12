package com.example.productservice.subscriber;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class PromotionSubscriber implements MessageListener {

    private final CacheManager cacheManager;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String productIdValue = new String(message.getBody(), StandardCharsets.UTF_8)
                .replace("\"", "")
                .trim();
        Long productId = Long.valueOf(productIdValue);

        Cache productsCache = cacheManager.getCache("products");
        if (productsCache != null) {
            productsCache.evict(productId);
            log.info("[PRODUCT-SERVICE] Đã xóa cache products::{}", productId);
        }
    }
}
