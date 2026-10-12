package com.example.inventoryservice.consumer;

import com.example.inventoryservice.event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderEventConsumer {

    @KafkaListener(topics = "order-events", groupId = "inventory-group")
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("[INVENTORY-SERVICE CONSUMER] Nhận đơn hàng #{}", event.getOrderId());
        log.info("[INVENTORY-SERVICE] Xử lý trừ kho -> Sản phẩm ID: {}, Số lượng trừ: {}",
                event.getProductId(), event.getQuantity());
    }
}
