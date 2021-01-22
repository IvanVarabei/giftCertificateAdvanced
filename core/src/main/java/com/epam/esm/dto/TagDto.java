package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class TagDto {
    private Long id;
    @NotBlank
    @Pattern(regexp = "\\w{2,64}")
    private String name;
}
