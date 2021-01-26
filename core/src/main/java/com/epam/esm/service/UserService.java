package com.epam.esm.service;

import com.epam.esm.dto.UserDto;

public interface UserService {
    UserDto getUserById(Long userId);
}
