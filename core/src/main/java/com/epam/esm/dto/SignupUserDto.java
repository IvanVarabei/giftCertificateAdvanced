package com.epam.esm.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class SignupUserDto {
    @NotNull
    @Valid
    private UserDto userDto;

    @NotNull
    @Pattern(regexp = "[\\w\\d]{4,64}")
    private String password;
}
