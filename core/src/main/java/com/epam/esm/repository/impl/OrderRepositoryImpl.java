package com.epam.esm.repository.impl;

import com.epam.esm.config.TimeZoneConfig;
import com.epam.esm.entity.Order;
import com.epam.esm.repository.GenericRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.util.DateTimeUtil;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepositoryImpl extends GenericRepository<Order> implements OrderRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    public OrderRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public Order save(Order order) {
        LocalDateTime cratedDate;
        cratedDate = DateTimeUtil.toZone(order.getCreatedDate(), TimeZoneConfig.DATABASE_ZONE, ZoneId.systemDefault());
        entityManager.persist(order);
        return order;
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return Optional.ofNullable(entityManager.find(Order.class, orderId));
    }

    @Override
    public List<Order> findOrdersByUserId(Long userId) {
        return entityManager.createQuery("from Order where user_id =:userId", Order.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
