package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;

import java.util.Map;

public interface OrderService {
    OrderDto saveOrder(OrderDto orderDto);

    Map<Long, OrderDto> getOrdersByUserId(Long userId);

    OrderDto updateOrder(OrderDto orderDto);

    void deleteOrder(Long orderId);
}
