package com.epam.esm.repository;

import com.epam.esm.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends Repository<Order> {
    Order save(Order order);

    Optional<Order> findById(Long orderId);

    List<Order> findOrdersByUserId(Long userId);
}
