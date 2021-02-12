package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomPageable {
    @Min(1)
    private Integer size = 10;

    @Min(0)
    private Integer page = 0;
}
