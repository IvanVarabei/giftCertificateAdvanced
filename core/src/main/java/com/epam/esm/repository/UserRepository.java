package com.epam.esm.repository;

import com.epam.esm.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CustomCrudRepository<User> {
    List<User> findPaginated(Integer offset, Integer limit);

    Long countAll();

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
