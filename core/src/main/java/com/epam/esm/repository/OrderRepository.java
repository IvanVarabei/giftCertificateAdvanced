package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Order createOrder(Order order);

    Optional<Order> findById(Long orderId);

    List<Order> findOrdersByUserId(Long userId);

    List<OrderItem> findOrderItemsByOrderId(Long orderId);

    void update(Order order);

    void delete(Long orderId);

    void deleteAllOrderItems(Long orderId);
}
