package com.epam.esm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OrderItem extends BaseEntity {
    @NotBlank
    @Pattern(regexp = "[\\w\\s]{2,64}")
    private String name;
    @NotBlank
    @Pattern(regexp = ".{2,512}")
    private String description;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 7, fraction = 2)
    private BigDecimal price;
    @NotNull
    @Positive
    @Max(1_000)
    private Integer duration;
    @NotNull
    @Positive
    @Max(100_000)
    private Integer quantity;
}
