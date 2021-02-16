package com.epam.esm.service;

import com.epam.esm.entity.Role;
import com.epam.esm.security.JwtUser;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    /**
     * The method prevents a user to change other user's data.
     *
     * @param authentication contains request sender's user object.
     * @param userId         which request sender tries to access.
     */
    public void ifUserIdNotMatchingThrowException(Authentication authentication, Long userId) {
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        boolean containsRoleUser = jwtUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(Role.ROLE_USER.name()::equalsIgnoreCase);
        if (containsRoleUser && !jwtUser.getId().equals(userId)) {
            throw new BadCredentialsException("The user doesn't have access to other user's resources");
        }
    }
}
