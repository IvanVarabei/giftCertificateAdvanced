package com.epam.esm.repository;

import com.epam.esm.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAllPaginated(Integer offset, Integer limit);

    Long countAll();

    Optional<User> findById(Long userId);
}
