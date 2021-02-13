package com.epam.esm.repository.impl;

import com.epam.esm.entity.User;
import com.epam.esm.repository.GenericRepository;
import com.epam.esm.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl extends GenericRepository<User> implements UserRepository {
    public UserRepositoryImpl() {
        super(User.class);
    }

    @Override
    public List<User> findPaginated(Integer offset, Integer limit) {
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
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(
                entityManager.createQuery("select u from User u where u.username = :username", User.class)
                        .setParameter("username", username)
                        .getSingleResult());
    }
}
