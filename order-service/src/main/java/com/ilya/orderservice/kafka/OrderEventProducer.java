package com.ilya.orderservice.kafka;

import com.ilya.common.OrderCreatedEvent;
import com.ilya.orderservice.dtos.OrderViewDto;
import com.ilya.orderservice.services.OrderViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public void createOrder(OrderCreatedEvent orderCreatedEvent){
        kafkaTemplate.send("order-created-events-topic",
                String.valueOf(orderCreatedEvent.getUserId()),
                orderCreatedEvent);
    }
}
