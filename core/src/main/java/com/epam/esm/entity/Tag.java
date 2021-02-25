package com.epam.esm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Audited
public class Tag extends BaseEntity {
    private String name;

    public Tag(Long id, String name) {
        super(id);
        this.name = name;
    }
}
