package com.epam.esm.service.impl;

import com.epam.esm.dto.CustomPage;
import com.epam.esm.dto.CustomPageable;
import com.epam.esm.dto.SignupUserDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ErrorMessage;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.mapper.UserConverter;
import com.epam.esm.repository.RoleRepository;
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
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDto register(SignupUserDto signupUserDto) {
        User user = userConverter.toUser(signupUserDto.getUserDto());
        Role roleUser = roleRepository.findByName("ROLE_USER");
        user.setRoles(List.of(roleUser));
        user.setPassword(passwordEncoder.encode(signupUserDto.getPassword()));
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
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format(ErrorMessage.USER_NOT_FOUND, username)));
    }
}
