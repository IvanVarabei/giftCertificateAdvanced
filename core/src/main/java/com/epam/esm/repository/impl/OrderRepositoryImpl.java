package com.epam.esm.repository.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.repository.GenericRepository;
import com.epam.esm.repository.OrderRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class OrderRepositoryImpl extends GenericRepository<Order> implements OrderRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    public OrderRepositoryImpl(EntityManager entityManager) {
        super(entityManager, Order.class);
        this.entityManager = entityManager;
    }

    @Override
    public List<Order> findOrdersByUserId(Long userId) {
        return entityManager.createQuery("from Order where user_id =:userId", Order.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
