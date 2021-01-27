package com.epam.esm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GiftCertificateAsOrderItem extends BaseEntity {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private Integer amount;
}
