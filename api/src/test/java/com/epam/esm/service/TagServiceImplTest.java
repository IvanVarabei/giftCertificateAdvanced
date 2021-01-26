package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.mapper.TagConverter;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.impl.TagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class TagServiceImplTest {
    @Autowired
    TagConverter tagConverter;
    TagService tagService;
    TagRepository tagRepository;
    Tag tag;

    {
        tag = new Tag();
        tag.setId(1L);
        tag.setName("tagTestName");
    }

    @BeforeEach
    public void setUp() {
        tagRepository = mock(TagRepository.class);
        tagService = new TagServiceImpl(tagRepository, tagConverter);
    }

    @Test
    void should_invoke_tagRepository_save_when_createTag() {
        tagService.createTag(new TagDto(null, "tagTestName"));

        verify(tagRepository).save(any());
    }

    @Test
    void should_invoke_findAll_when_getTags() {
        tagService.getTags();

        verify(tagRepository).findAll();
    }

    @Test
    void returns_tag_having_specified_id_when_getTagBuId() {
        when(tagRepository.findById(any())).thenReturn(Optional.ofNullable(tag));

        TagDto tagDto = tagService.getTagById(1L);

        assertNotNull(tagDto.getId());
        assertEquals("tagTestName", tagDto.getName());
    }

    @Test
    void tag_not_found_exception_thrown() {
        assertThrows(ResourceNotFoundException.class, () -> tagService.getTagById(1L));
    }

    @Test
    void returns_tag_dto_with_updated_name_when_updateTag() {
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        TagDto expectedTagDto = new TagDto(1L, "newTagName");

        TagDto actualTagDto = tagService.updateTag(new TagDto(1L, "newTagName"));

        assertEquals(expectedTagDto, actualTagDto);
    }

    @Test
    void tag_not_found_when_update_exception_thrown() {
        TagDto sourceTagDto = new TagDto(1L, "tagName");

        assertThrows(ResourceNotFoundException.class, () -> tagService.updateTag(sourceTagDto));
    }

    @Test
    void should_invoke_tag_repository_delete_when_deleteTag() {
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));

        tagService.deleteTag(1L);

        verify(tagRepository).delete(1L);
    }

    @Test
    void tag_not_found_when_delete_exception_thrown() {
        assertThrows(ResourceNotFoundException.class, () -> tagService.deleteTag(1L));
    }

    @Test
    void should_invoke_tagRepository_bindWithCertificate_two_times_when_bindTags() {
        GiftCertificate certificate = mock(GiftCertificate.class);
        Tag notExistedTag = mock(Tag.class);
        when(certificate.getTags()).thenReturn(List.of(tag, notExistedTag));
        when(tagRepository.findByName(notExistedTag.getName())).thenReturn(Optional.empty());
        when(tagRepository.findByName("tagTestName")).thenReturn(Optional.of(tag));

        tagService.bindTags(certificate.getId(), certificate.getTags());

        verify(tagRepository, times(2)).bindWithCertificate(any(), any());
    }

    @Test
    void should_invoke_tagRepository_unbindTagsFromCertificate() {
        tagService.unbindTagsFromCertificate(1L);

        verify(tagRepository).unbindTagsFromCertificate(1L);
    }

    @Test
    void should_invoke_tagRepository_getTagsByCertificateId() {
        tagService.getTagsByCertificateId(1L);

        verify(tagRepository).getTagsByCertificateId(1L);
    }
}
