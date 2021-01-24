package com.epam.esm.mapper;

import com.epam.esm.entity.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {
    @Bean
    public RowMapper<Tag> creteTagMapper() {
        return new BeanPropertyRowMapper<>(Tag.class);
    }
}