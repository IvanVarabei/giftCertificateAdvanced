package com.epam.esm.repository.impl;

import com.epam.esm.entity.User;
import com.epam.esm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<User> findAllPaginated(Integer offset, Integer limit) {
        return entityManager
                .createQuery("from User", User.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public Long countAll() {
        return entityManager.createQuery("select count(id) from User ", Long.class).getSingleResult();
    }

    @Override
    public Optional<User> findById(Long userId) {
        return Optional.ofNullable(entityManager.find(User.class, userId));
    }
}
