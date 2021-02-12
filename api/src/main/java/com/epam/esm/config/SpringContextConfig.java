package com.epam.esm.config;


import com.epam.esm.dto.search.SortByField;
import com.epam.esm.dto.search.SortOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class SpringContextConfig {
    /**
     * Can't be replaced with lambda. Otherwise it fails.
     */
    @Bean
    public Converter<String, SortByField> createSortFieldConverter() {
        return new Converter<>() {
            @Override
            public SortByField convert(String source) {
                return SortByField.valueOf(source.toUpperCase());
            }
        };
    }

    /**
     * Can't be replaced with lambda. Otherwise it fails.
     */
    @Bean
    public Converter<String, SortOrder> createSortOrderConverter() {
        return new Converter<>() {
            @Override
            public SortOrder convert(String source) {
                return SortOrder.valueOf(source.toUpperCase());
            }
        };
    }
}
