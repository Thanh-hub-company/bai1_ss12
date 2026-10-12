package com.example.orderservice.service;

import com.example.orderservice.entity.Order;
import com.example.orderservice.event.OrderCreatedEvent;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    private static final String TOPIC = "order-events";

    @Transactional
    public Order createOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Đơn hàng không được để trống");
        }
        // 1. Ghi đơn hàng vào Database
        Order savedOrder = orderRepository.save(order);
        log.info("[ORDER-SERVICE] Đã lưu thành công Đơn hàng ID: {}", savedOrder.getId());

        // 2. Tạo Event
        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(savedOrder.getId())
                .customerEmail(savedOrder.getCustomerEmail())
                .productId(savedOrder.getProductId())
                .quantity(savedOrder.getQuantity())
                .totalAmount(savedOrder.getTotalAmount())
                .build();

        // 3. Gửi Event bất đồng bộ qua Kafka
        kafkaTemplate.send(TOPIC, String.valueOf(savedOrder.getId()), event);
        log.info("[ORDER-SERVICE] Đã gửi sự kiện OrderCreatedEvent lên Kafka Topic '{}'", TOPIC);

        return savedOrder;
    }
}