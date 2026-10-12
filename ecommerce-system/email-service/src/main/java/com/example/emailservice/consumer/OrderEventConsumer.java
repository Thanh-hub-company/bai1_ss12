package com.example.emailservice.consumer;

import com.example.emailservice.event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderEventConsumer {

    @KafkaListener(topics = "order-events", groupId = "email-group")
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("[EMAIL-SERVICE CONSUMER] Nhận đơn hàng #{}", event.getOrderId());
        log.info("[EMAIL-SERVICE] Gửi email xác nhận đặt hàng tới địa chỉ: {}", event.getCustomerEmail());
    }
}
