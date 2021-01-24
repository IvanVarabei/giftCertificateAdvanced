package com.epam.esm.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Data
public class PriceDto {
    @Min(1)
    private Long id;
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 7, fraction = 2)
    private BigDecimal price;
}
