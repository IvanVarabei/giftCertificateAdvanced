package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "default_generator")
    private Long id;
    @Column(unique = true)
    private String name;
}
