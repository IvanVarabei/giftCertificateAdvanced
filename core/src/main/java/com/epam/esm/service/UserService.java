package com.epam.esm.service;

import com.epam.esm.dto.AuthenticationRequestDto;
import com.epam.esm.dto.CustomPage;
import com.epam.esm.dto.CustomPageable;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;

public interface UserService {
    UserDto register(AuthenticationRequestDto authenticationRequestDto);

    CustomPage<UserDto> getPaginated(CustomPageable pageRequest);

    UserDto getUserById(Long userId);

    User getUserByEmail(String email);
}
