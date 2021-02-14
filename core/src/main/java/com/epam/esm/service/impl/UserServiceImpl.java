package com.epam.esm.service.impl;

import com.epam.esm.dto.AuthenticationRequestDto;
import com.epam.esm.dto.CustomPage;
import com.epam.esm.dto.CustomPageable;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ErrorMessage;
import com.epam.esm.exception.ResourceAlreadyExistException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.mapper.UserConverter;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDto register(AuthenticationRequestDto authenticationRequestDto) {
        String email = authenticationRequestDto.getEmail();
        userRepository.findByEmail(email).ifPresent(r -> {
            throw new ResourceAlreadyExistException(String.format(ErrorMessage.USER_ALREADY_EXISTS, email));
        });
        User user = userConverter.toUser(authenticationRequestDto);
        user.setRole(Role.ROLE_USER);
        user.setUsername("defaultUsername");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userConverter.toDTO(userRepository.save(user));
    }

    @Override
    public CustomPage<UserDto> getPaginated(CustomPageable pageRequest) {
        int size = pageRequest.getSize();
        int page = pageRequest.getPage();
        long totalUserAmount = userRepository.countAll();
        long lastPage = (totalUserAmount + size - 1) / size - 1;
        if (page > lastPage) {
            throw new ResourceNotFoundException(String.format(ErrorMessage.PAGE_NOT_FOUND, size, page, lastPage));
        }
        int offset = size * page;
        List<User> foundUsers = userRepository.findPaginated(offset, size);
        return new CustomPage<>(foundUsers.stream().map(userConverter::toDTO).collect(Collectors.toList()),
                pageRequest, totalUserAmount);
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND, userId)));
        return userConverter.toDTO(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format(ErrorMessage.USER_NOT_FOUND, email)));
    }
}
