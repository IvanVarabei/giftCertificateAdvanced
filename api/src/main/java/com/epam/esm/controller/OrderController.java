package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderDto orderDto) {
        return ResponseEntity.status(CREATED).body(orderService.saveOrder(orderDto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Map<Long, OrderDto>> getOrdersByUserId(@PathVariable("userId") @Min(1) Long userId) {
        return ResponseEntity.ok().body(orderService.getOrdersByUserId(userId));
    }
}
