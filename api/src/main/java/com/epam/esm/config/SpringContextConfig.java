package com.epam.esm.config;

import com.epam.esm.entity.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

@Configuration
public class SpringContextConfig {
    @Bean
    public RowMapper<Tag> creteTagMapper() {
        return new BeanPropertyRowMapper<>(Tag.class);
    }
}
