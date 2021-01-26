package com.epam.esm.mapper;

import com.epam.esm.entity.GiftCertificateAsOrderItem;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class GiftCertificateAsOrderItemMapper {
    @Bean
    public RowMapper<GiftCertificateAsOrderItem> createCertificateAsOrderItemMapper() {
        return new BeanPropertyRowMapper<>(GiftCertificateAsOrderItem.class);
    }
}
