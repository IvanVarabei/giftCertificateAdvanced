package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TagRepositoryImplTest {
    @Autowired
    TagRepository tagRepository;
    @Autowired
    GiftCertificateRepository certificateRepository;

    @Test
    @Transactional
    void should_id_not_be_null_when_save() {
        Tag tag = new Tag();
        tag.setName("name test 1");
        Tag savedTag = tagRepository.save(tag);

        Assertions.assertNotNull(savedTag.getId());
    }

    @Test
    void should_return_one_tags_when_findPaginated() {
        List<Tag> tags = tagRepository.findPaginated(0, 1);

        assertEquals(tags.size(), 1);
    }

    @Test
    void should_be_not_empty_optional_when_findById() {
        Optional<Tag> tagOptional = tagRepository.findById(1L);

        assertEquals(1L, (long) tagOptional.get().getId());
    }

    @Test
    @Transactional
    void should_be_empty_optional_after_delete_when_find_by_id() {
        Tag testTag = new Tag();
        testTag.setName("testNameDelete");
        Tag savedTag = tagRepository.save(testTag);

        tagRepository.delete(savedTag);

        assertEquals(Optional.empty(), tagRepository.findById(savedTag.getId()));
    }
}