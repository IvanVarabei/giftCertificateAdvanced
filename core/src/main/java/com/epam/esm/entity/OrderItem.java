package com.epam.esm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OrderItem extends BaseEntity {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private List<Tag> tags;
    private Integer quantity;
    private Long certificateId;
}
