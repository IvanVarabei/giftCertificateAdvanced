package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends Repository<Tag> {
    Tag save(Tag tag);

    List<Tag> findPaginated(Integer offset, Integer limit);

    Long countAll();

    Optional<Tag> findById(Long tagId);

    Optional<Tag> findByName(String name);

    List<Tag> getTagsByCertificateId(Long id);

//    void bindWithCertificate(Long certificateId, Long tagId);
//
//    void unbindTagsFromCertificate(Long certificateId);

    Optional<Tag> getPrevalentTagOfMostProfitableUser();
}
