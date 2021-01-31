package com.epam.esm.service;

import com.epam.esm.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserDto> getPaginated(Pageable pageRequest);

    UserDto getUserById(Long userId);
}
