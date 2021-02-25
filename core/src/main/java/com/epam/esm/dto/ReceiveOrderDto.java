package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiveOrderDto {
    @NotNull
    @Min(1)
    private Long userId;

    @NotNull
    private Map<@NotNull @Min(1) Long, @NotNull @Min(1) Integer> orderItems = new HashMap<>();
}


