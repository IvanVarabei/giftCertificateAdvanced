package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemEntryDto {
    @Min(1)
    private Long itemId;
    @Min(1)
    private Integer amount;
}
