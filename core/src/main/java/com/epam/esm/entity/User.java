package com.epam.esm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "\"user\"")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class User extends BaseEntity {
    protected String email;
    protected String password;

    @Enumerated(EnumType.STRING)
    protected Role role;

    public User(Long id, String email, String password, Role role) {
        super(id);
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
