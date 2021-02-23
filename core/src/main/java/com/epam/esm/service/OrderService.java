package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;

import java.util.Map;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto, User user);

    Map<Long, OrderDto> getOrdersByUserId(Long userId);

    Order getOrderById(Long orderId);

    OrderDto updateOrder(OrderDto orderDto);

    void deleteOrder(Long orderId);
}
