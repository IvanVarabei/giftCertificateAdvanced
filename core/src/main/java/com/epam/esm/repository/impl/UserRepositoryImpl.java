package com.epam.esm.repository.impl;

import com.epam.esm.entity.User;
import com.epam.esm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> userMapper;

    private static final String READ_USER_BY_ID = "select id, username, password, email from \"user\" where id = ?";

    @Override
    public Optional<User> findById(Long userId) {
        return jdbcTemplate.query(READ_USER_BY_ID, userMapper, userId).stream().findAny();
    }
}
