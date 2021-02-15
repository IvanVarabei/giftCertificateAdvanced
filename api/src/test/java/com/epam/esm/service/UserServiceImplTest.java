package com.epam.esm.service;

import com.epam.esm.dto.CustomPageable;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.mapper.UserConverter;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {
    @Autowired
    UserConverter userConverter;

    UserService userService;
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository, userConverter, null);
    }

    @Test
    void should_invoke_findPaginated_when_getPaginated() {
        when(userRepository.countAll()).thenReturn(20L);

        userService.getPaginated(new CustomPageable(3, 4));

        verify(userRepository).findPaginated(12, 3);
    }

    @Test
    void if_requested_page_grater_than_last_page_exception_thrown() {
        when(userRepository.countAll()).thenReturn(20L);
        CustomPageable pageable = new CustomPageable(3, 7);

        assertThrows(ResourceNotFoundException.class, () -> userService.getPaginated(pageable));
    }

    @Test
    void returns_user_having_specified_id_when_getUserBuId() {
        User foundUser = new User();
        foundUser.setId(1L);
        foundUser.setUsername("ivan");
        when(userRepository.findById(foundUser.getId())).thenReturn(Optional.of(foundUser));

        UserDto userDto = userService.getUserById(foundUser.getId());

        assertEquals(foundUser.getUsername(), userDto.getUsername());
    }

    @Test
    void if_user_not_found_exception_thrown() {
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
    }
}
