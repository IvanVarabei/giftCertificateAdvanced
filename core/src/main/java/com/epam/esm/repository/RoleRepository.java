package com.epam.esm.repository;

import com.epam.esm.entity.Role;

public interface RoleRepository {
    Role findByName(String name);
}
