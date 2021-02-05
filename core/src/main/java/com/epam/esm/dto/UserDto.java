package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.*;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class UserDto extends RepresentationModel<UserDto> {
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
