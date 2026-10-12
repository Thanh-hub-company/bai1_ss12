package com.example.orderservice.event;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreatedEvent implements Serializable {
    private Long orderId;
    private String customerEmail;
    private Long productId;
    private Integer quantity;
    private BigDecimal totalAmount;
}