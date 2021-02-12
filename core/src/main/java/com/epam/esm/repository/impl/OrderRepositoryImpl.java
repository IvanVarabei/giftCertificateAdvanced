package com.epam.esm.repository.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.repository.GenericRepository;
import com.epam.esm.repository.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepositoryImpl extends GenericRepository<Order> implements OrderRepository {
    public OrderRepositoryImpl() {
        super(Order.class);
    }

    @Override
    public List<Order> findOrdersByUserId(Long userId) {
        return entityManager.createQuery("from Order where user_id =:userId", Order.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
