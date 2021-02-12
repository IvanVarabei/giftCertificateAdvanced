package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends CustomCrudRepository<Tag> {
    List<Tag> findPaginated(Integer offset, Integer limit);

    Long countAll();

    Optional<Tag> getPrevalentTagOfMostProfitableUser();
}
