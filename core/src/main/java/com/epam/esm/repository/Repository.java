package com.epam.esm.repository;

public interface Repository<T> {

    void update(T t);

    void delete(T t);
}
