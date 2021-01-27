package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private Integer quantity;
}
