package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceOrderDto {
    @Min(1)
    @NotNull
    private Long userId;
    /**
     * certificateId : amount
     */
    @NotNull
    private Map<@Min(1) Long, @Min(1) Integer> itemEntries;
}
