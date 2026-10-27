package com.ilya.notificationservice.controllers;

import com.ilya.notificationservice.dtos.OrderViewDto;
import com.ilya.notificationservice.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final OrderService orderService;

    @GetMapping("/{userId}")
    public ResponseEntity<OrderViewDto> getByUserId(@PathVariable Long userId){
        OrderViewDto dto = orderService.findByUserId(userId);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }
}
