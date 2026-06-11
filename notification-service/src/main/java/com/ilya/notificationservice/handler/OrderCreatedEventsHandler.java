package com.ilya.notificationservice.handler;

import com.ilya.common.OrderCreatedEvent;
import com.ilya.notificationservice.dtos.OrderViewDto;
import com.ilya.notificationservice.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "order-created-events-topic")
@RequiredArgsConstructor
public class OrderCreatedEventsHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final OrderService orderService;

    @KafkaHandler
    public void handle(OrderCreatedEvent orderCreatedEvent){
        LOGGER.info("Received event: {} ", orderCreatedEvent.getTotalPrice());
        orderService.save(orderCreatedEvent);
    }
}
