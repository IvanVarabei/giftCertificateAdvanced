package com.epam.esm.service;

import com.epam.esm.dto.CustomPage;
import com.epam.esm.dto.CustomPageable;
import com.epam.esm.dto.UserDto;

public interface UserService {
    CustomPage<UserDto> getPaginated(CustomPageable pageRequest);

    UserDto getUserById(Long userId);
}
