package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderItem;

import java.util.List;

public interface OrderRepository {
    Order createOrder(Order order);

    List<Order> findOrdersByUserId(Long userId);

    List<OrderItem> findOrderItemsByOrderId(Long orderId);
}
