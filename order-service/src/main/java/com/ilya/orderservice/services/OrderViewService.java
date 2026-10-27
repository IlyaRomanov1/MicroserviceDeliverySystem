package com.ilya.orderservice.services;

import com.ilya.common.OrderCreatedEvent;
import com.ilya.orderservice.dtos.OrderResultDto;
import com.ilya.orderservice.kafka.OrderEventProducer;
import com.ilya.orderservice.models.OrderView;
import com.ilya.orderservice.repositories.OrderViewRepository;
import com.ilya.orderservice.util.exceptions.EmptyCartException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderViewService {

    private static final String STATUS_CONFIRMED = "CONFIRMED";
    private static final String STATUS_REJECTED = "REJECTED";

    private final BagsService bagsService;
    private final OrderEventProducer orderEventProducer;
    private final OrderViewRepository orderViewRepository;
    private final WalletService walletService;
    private final ModelMapper modelMapper;

    @Transactional
    public OrderResultDto createOrder(Long userId){
        OrderView view = orderViewRepository.findByUserId(userId);
        if (view == null || view.getTotalPrice() == null) {
            throw new EmptyCartException();
        }

        boolean charged = walletService.charge(userId, view.getTotalPrice());
        if (!charged) {
            return new OrderResultDto(STATUS_REJECTED, view.getTotalPrice(),
                    walletService.getBalance(userId), "Недостаточно средств на балансе");
        }

        OrderCreatedEvent orderCreatedEvent = modelMapper.map(view, OrderCreatedEvent.class);
        bagsService.clearCart(userId);
        orderEventProducer.createOrder(orderCreatedEvent);

        return new OrderResultDto(STATUS_CONFIRMED, view.getTotalPrice(),
                walletService.getBalance(userId), null);
    }
}
