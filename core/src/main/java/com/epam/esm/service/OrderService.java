package com.epam.esm.service;

import com.epam.esm.dto.ReceiveOrderDto;
import com.epam.esm.dto.ResponseOrderDto;

import java.util.Map;

public interface OrderService {
    ResponseOrderDto createOrder(ReceiveOrderDto orderDto);

    Map<Long, ResponseOrderDto> getOrdersByUserId(Long userId);

    ResponseOrderDto getOrderById(Long orderId);

    ResponseOrderDto updateOrder(ReceiveOrderDto orderDto, Long orderId);

    void deleteOrder(Long orderId);
}
