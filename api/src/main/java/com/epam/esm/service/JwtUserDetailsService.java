package com.epam.esm.service;

import com.epam.esm.entity.User;
import com.epam.esm.security.JwtUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.getUserByEmail(email);
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                true,
                List.of(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}
