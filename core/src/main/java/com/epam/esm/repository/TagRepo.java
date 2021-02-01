package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import org.springframework.data.repository.CrudRepository;

public interface TagRepo extends CrudRepository<Tag, Long> {
    Tag findByName(String gym);
}
