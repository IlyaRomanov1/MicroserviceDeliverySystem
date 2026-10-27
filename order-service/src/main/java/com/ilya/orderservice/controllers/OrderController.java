package com.ilya.orderservice.controllers;

import com.ilya.orderservice.dtos.OrderResultDto;
import com.ilya.orderservice.security.MyUserDetails;
import com.ilya.orderservice.services.OrderViewService;
import com.ilya.orderservice.util.exceptions.EmptyCartException;
import com.ilya.orderservice.util.exceptions.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderViewService orderViewService;

    @PostMapping("/send")
    public ResponseEntity<OrderResultDto> send(@AuthenticationPrincipal MyUserDetails userDetails){
        OrderResultDto result = orderViewService.createOrder(userDetails.getId());
        HttpStatus status = "REJECTED".equals(result.getStatus()) ? HttpStatus.PAYMENT_REQUIRED : HttpStatus.CREATED;
        return new ResponseEntity<>(result, status);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(EmptyCartException e){
        ErrorResponse errorResponse = new ErrorResponse(
                "Корзина пуста",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
