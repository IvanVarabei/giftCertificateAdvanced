package com.epam.esm.service;

import com.epam.esm.dto.CustomPage;
import com.epam.esm.dto.CustomPageable;
import com.epam.esm.dto.SignupUserDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;

public interface UserService {
    UserDto register(SignupUserDto signupUserDto);

    CustomPage<UserDto> getPaginated(CustomPageable pageRequest);

    UserDto getUserById(Long userId);

    User findByUsername(String username);
}
