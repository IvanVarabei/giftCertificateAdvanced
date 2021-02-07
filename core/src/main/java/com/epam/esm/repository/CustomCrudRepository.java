package com.epam.esm.repository;

import java.util.Optional;

public interface CustomCrudRepository<T> {
    T save(T t);

    Optional<T> findById(Long id);

    T update(T t);

    void delete(T t);
}
