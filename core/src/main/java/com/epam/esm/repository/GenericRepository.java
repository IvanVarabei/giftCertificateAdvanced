package com.epam.esm.repository;

import javax.persistence.EntityManager;
import java.util.Optional;

public class GenericRepository<T> implements CustomCrudRepository<T> {
    private final EntityManager entityManager;
    private final Class<T> clazz;

    public GenericRepository(EntityManager entityManager, Class<T> clazz) {
        this.entityManager = entityManager;
        this.clazz = clazz;
    }

    @Override
    public T save(T t) {
//        LocalDateTime cratedDate;
//        cratedDate = DateTimeUtil.toZone(order.getCreatedDate(), TimeZoneConfig.DATABASE_ZONE, ZoneId.systemDefault());
        entityManager.persist(t);
        return t;
    }

    @Override
    public Optional<T> findById(Long id) {
        return Optional.ofNullable(entityManager.find(clazz, id));
    }

    @Override
    public T update(T t) {
        return entityManager.merge(t);
    }

    @Override
    public void delete(T t) {
        entityManager.remove(entityManager.contains(t) ? t : entityManager.merge(t));
    }
}
