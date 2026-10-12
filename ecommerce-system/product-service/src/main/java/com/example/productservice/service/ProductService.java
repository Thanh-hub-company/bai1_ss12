package com.example.productservice.service;

import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    // Cache-aside: Kiểm tra Redis trước, nếu Miss mới query DB và lưu vào Redis (TTL 30 min)
    @Cacheable(value = "products", key = "#id")
    public Product getProductById(Long id) {
        log.info("[PRODUCT-SERVICE DB] Query thông tin sản phẩm ID: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));
    }

    // Xóa Cache khi cập nhật thông tin sản phẩm
    @CacheEvict(value = "products", key = "#id")
    public Product updateProduct(Long id, Product details) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));

        existing.setName(details.getName());
        existing.setDescription(details.getDescription());
        existing.setPrice(details.getPrice());
        existing.setStock(details.getStock());

        return productRepository.save(existing);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
}