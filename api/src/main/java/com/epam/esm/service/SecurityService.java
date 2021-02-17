package com.epam.esm.service;

import com.epam.esm.entity.Role;
import com.epam.esm.exception.CustomAccessDeniedException;
import com.epam.esm.security.JwtUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    /**
     * The method prevents a user to change other user's data.
     *
     * @param userId which request sender tries to access.
     */
    public void validateUserAccess(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        boolean isAdmin = jwtUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(Role.ROLE_ADMIN.name()::equalsIgnoreCase);
        if (!isAdmin && !jwtUser.getId().equals(userId)) {
            throw new CustomAccessDeniedException("The user is not allowed to access other user's resources");
        }
    }
}
