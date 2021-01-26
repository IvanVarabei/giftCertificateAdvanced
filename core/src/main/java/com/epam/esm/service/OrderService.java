package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.PlaceOrderDto;

import java.util.List;

public interface OrderService {
    OrderDto placeOrder(PlaceOrderDto placeOrderDto);

    List<OrderDto> getOrdersByUserId(Long userId);
}
