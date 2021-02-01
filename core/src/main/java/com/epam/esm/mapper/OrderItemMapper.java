package com.epam.esm.mapper;

import com.epam.esm.entity.OrderItem;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {
    @Bean
    public RowMapper<OrderItem> createCertificateAsOrderItemMapper() {
        return new BeanPropertyRowMapper<>(OrderItem.class);
    }
}
