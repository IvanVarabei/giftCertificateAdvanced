package com.epam.esm.dto;

import com.epam.esm.entity.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class UserDto extends RepresentationModel<UserDto> {
    @NotNull
    @Min(1)
    private Long id;

    @NotBlank
    @Email
    private String email;

    @JsonIgnore
    private Role role;
}
