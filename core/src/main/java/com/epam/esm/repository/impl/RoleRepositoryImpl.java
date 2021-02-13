package com.epam.esm.repository.impl;

import com.epam.esm.entity.Role;
import com.epam.esm.repository.GenericRepository;
import com.epam.esm.repository.RoleRepository;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepositoryImpl extends GenericRepository<Role> implements RoleRepository {
    public RoleRepositoryImpl() {
        super(Role.class);
    }

    @Override
    public Role findByName(String name) {
        return entityManager.createQuery("select r from Role r  where r.name = :name", Role.class)
                .setParameter("name", name)
                .getSingleResult();
    }
}
