package com.ilya.notificationservice.services;

import com.ilya.common.OrderCreatedEvent;
import com.ilya.notificationservice.dtos.OrderViewDto;
import com.ilya.notificationservice.models.Order;
import com.ilya.notificationservice.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void save(OrderCreatedEvent orderCreatedEvent){
        orderRepository.save(modelMapper.map(orderCreatedEvent, Order.class));
    }
}
