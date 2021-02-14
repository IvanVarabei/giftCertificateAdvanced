package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;

import java.util.Map;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);

    Map<Long, OrderDto> getOrdersByUserId(Long userId);

    OrderDto getOrderById(Long orderId);

    OrderDto updateOrder(OrderDto orderDto);

    void deleteOrder(Long orderId);
}
