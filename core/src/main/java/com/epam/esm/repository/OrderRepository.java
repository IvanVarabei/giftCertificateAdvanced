package com.epam.esm.repository;

import com.epam.esm.entity.Order;

import java.util.List;

public interface OrderRepository extends CustomCrudRepository<Order> {
    List<Order> findOrdersByUserId(Long userId);
}
