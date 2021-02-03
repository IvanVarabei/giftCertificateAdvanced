package com.epam.esm.repository;

import javax.persistence.EntityManager;

public class GenericRepository<T> implements Repository<T> {
    private final EntityManager entityManager;

    public GenericRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void update(T t) {
        entityManager.merge(t);
    }

    @Override
    public void delete(T t) {
        entityManager.remove(t);
    }
}
