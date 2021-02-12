package com.epam.esm.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

public class GenericRepository<T> implements CustomCrudRepository<T> {
    @PersistenceContext
    protected EntityManager entityManager;
    private final Class<T> clazz;

    public GenericRepository(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T save(T t) {
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
