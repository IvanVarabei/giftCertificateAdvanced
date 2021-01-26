package com.epam.esm.mapper;

import com.epam.esm.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    @Bean
    public RowMapper<User> creteUserMapper() {
        return new BeanPropertyRowMapper<>(User.class);
    }
}
