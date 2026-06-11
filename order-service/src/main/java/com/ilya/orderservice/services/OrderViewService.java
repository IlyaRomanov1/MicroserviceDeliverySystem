package com.ilya.orderservice.services;

import com.ilya.common.OrderCreatedEvent;
import com.ilya.orderservice.dtos.OrderViewDto;
import com.ilya.orderservice.kafka.OrderEventProducer;
import com.ilya.orderservice.models.User;
import com.ilya.orderservice.repositories.OrderViewRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderViewService {

    private final BagsService bagsService;
    private final OrderEventProducer orderEventProducer;
    private final OrderViewRepository orderViewRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void createOrder(User user){
        bagsService.delete(user);
        OrderCreatedEvent orderCreatedEvent = modelMapper.map(orderViewRepository.findByUserId(user.getId()),
                OrderCreatedEvent.class);
        orderEventProducer.createOrder(orderCreatedEvent);
    }
}
