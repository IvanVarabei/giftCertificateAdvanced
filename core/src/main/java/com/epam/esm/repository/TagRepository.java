package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
    Tag save(Tag tag);

    List<Tag> findAll();

    Optional<Tag> findById(Long tagId);

    Optional<Tag> findByName(String name);

    void update(Tag tag);

    void delete(Long tagId);

    List<Tag> getTagsByCertificateId(Long id);

    void bindWithCertificate(Long certificateId, Long tagId);

    void unbindTagsFromCertificate(Long certificateId);
}
