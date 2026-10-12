package com.example.productservice.service;

import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlashSaleService {

    private final ProductRepository productRepository;
    private final RedissonClient redissonClient;

    public String buyProductFlashSale(Long productId, Integer quantity) {
        String lockKey = "lock:product:" + productId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            // Chờ tối đa 3s để lấy Lock, giữ Lock tối đa 10s
            boolean isAcquired = lock.tryLock(3, 10, TimeUnit.SECONDS);

            if (!isAcquired) {
                return "Hệ thống đang quá tải, vui lòng thử lại!";
            }

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            // Critical Section: Kiểm tra tồn kho
            if (product.getStock() < quantity) {
                return "Sản phẩm đã hết hàng!";
            }

            // Trừ tồn kho & lưu DB
            product.setStock(product.getStock() - quantity);
            productRepository.save(product);

            return "Đặt hàng thành công! Tồn kho còn lại: " + product.getStock();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Lỗi hệ thống!";
        } finally {
            // Đảm bảo LUÔN giải phóng khóa trong khối finally
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
