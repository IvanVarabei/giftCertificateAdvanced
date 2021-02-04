package com.epam.esm.repository;

public interface Repository<T> {

    T update(T t);

    void delete(T t);
}
