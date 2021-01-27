package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
public class UserDto {
    @NotNull
    @Min(1)
    private Long id;
    @NotBlank
    @Pattern(regexp = "[\\w\\s]{2,64}")
    private String username;
    @NotBlank
    @Email
    private String email;
}
