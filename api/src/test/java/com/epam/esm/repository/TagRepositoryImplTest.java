package com.epam.esm.repository;

import com.epam.esm.config.EmbeddedTestConfig;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {EmbeddedTestConfig.class})
class TagRepositoryImplTest {
    @Autowired
    TagRepository tagRepository;
    @Autowired
    GiftCertificateRepository certificateRepository;

    @Test
    void should_id_not_be_null_when_save() {
        Tag tag = new Tag();
        tag.setName("name test 1");
        tagRepository.save(tag);

        Assertions.assertNotNull(tag.getId());
    }

    @Test
    void should_return_more_than_zero_tags_when_findAll() {
        List<Tag> tags = tagRepository.findAll();

        assertTrue(tags.size() > 0);
    }

    @Test
    void should_be_not_empty_optional_when_findById() {
        Optional<Tag> tagOptional = tagRepository.findById(1L);

        assertEquals(1L, (long) tagOptional.get().getId());
    }

    @Test
    void should_be_the_same_name_when_find_by_name() {
        Optional<Tag> tagOptional = tagRepository.findByName("gym");

        assertEquals("gym", tagOptional.get().getName());
    }

    @Test
    void row_set_should_be_empty_after_delete() {
        Tag testTag = new Tag();
        testTag.setName("testNameDelete");
        Tag tagWithId = tagRepository.save(testTag);

        tagRepository.delete(tagWithId.getId());

        assertEquals(Optional.empty(), tagRepository.findById(tagWithId.getId()));
    }

    @Test
    void should_return_tags_that_related_to_certificate_id() {
        List<Tag> tagsRefToCertificate = tagRepository.getTagsByCertificateId(1L);
        List<Long> expectedTagIdList = tagsRefToCertificate.stream()
                .map(Tag::getId)
                .collect(Collectors.toList());

        assertEquals(List.of(1L, 2L), expectedTagIdList);
    }

    @Test
    void row_set_should_not_be_empty_after_bind() {
        Tag tag = new Tag();
        tag.setName("testBind");
        Long tagId = tagRepository.save(tag).getId();
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("testBind");
        certificate.setDescription("testBind");
        certificate.setPrice(BigDecimal.ONE);
        certificate.setDuration(1);
        certificate.setCreatedDate(LocalDateTime.now());
        Long certificateId = certificateRepository.save(certificate).getId();

        tagRepository.bindWithCertificate(certificateId, tagId);

        assertEquals(tagId, tagRepository.getTagsByCertificateId(certificateId).get(0).getId());
    }

    @Test
    void row_set_should_be_empty_after_unbind() {
        Tag tag = new Tag();
        tag.setName("testUnbind");
        Long tagId = tagRepository.save(tag).getId();
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("testUnbind");
        certificate.setDescription("testUnbind");
        certificate.setPrice(BigDecimal.ONE);
        certificate.setDuration(1);
        certificate.setCreatedDate(LocalDateTime.now());
        Long certificateId = certificateRepository.save(certificate).getId();
        tagRepository.bindWithCertificate(certificateId, tagId);

        tagRepository.unbindTagsFromCertificate(certificateId);

        assertTrue(tagRepository.getTagsByCertificateId(certificateId).isEmpty());
    }
}