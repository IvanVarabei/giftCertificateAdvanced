package com.epam.esm.service;

import com.epam.esm.dto.CustomPageable;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.mapper.TagConverter;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.impl.TagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    void should_invoke_findPaginated_when_getPaginated() {
        when(tagRepository.countAll()).thenReturn(20L);

        tagService.getPaginated(new CustomPageable(3, 4));

        verify(tagRepository).findPaginated(12, 3);
    }

    @Test
    void if_requested_page_grater_than_last_page_exception_thrown() {
        when(tagRepository.countAll()).thenReturn(20L);
        CustomPageable pageable = new CustomPageable(3, 7);

        assertThrows(ResourceNotFoundException.class, () -> tagService.getPaginated(pageable));
    }

    @Test
    void returns_tag_having_specified_id_when_getTagBuId() {
        Tag foundTag = new Tag(1L, "newTagName");
        when(tagRepository.findById(any())).thenReturn(Optional.of(foundTag));

        TagDto tagDto = tagService.getTagById(foundTag.getId());

        assertNotNull(tagDto.getId());
        assertEquals(foundTag.getName(), tagDto.getName());
    }

    @Test
    void tag_not_found_exception_thrown() {
        assertThrows(ResourceNotFoundException.class, () -> tagService.getTagById(1L));
    }

    @Test
    void returns_tag_dto_with_updated_name_when_updateTag() {
        TagDto update = new TagDto(1L, "newTagName");
        Tag foundTag = new Tag(1L, "tagName");
        when(tagRepository.findById(update.getId())).thenReturn(Optional.of(foundTag));
        when(tagRepository.update(foundTag)).thenReturn(new Tag(update.getId(), update.getName()));
        TagDto expectedTagDto = new TagDto(update.getId(), update.getName());

        TagDto actualTagDto = tagService.updateTag(update);

        assertEquals(expectedTagDto, actualTagDto);
    }

    @Test
    void if_tag_not_found_when_update_exception_thrown() {
        TagDto sourceTagDto = new TagDto(1L, "tagName");

        assertThrows(ResourceNotFoundException.class, () -> tagService.updateTag(sourceTagDto));
    }

    @Test
    void should_invoke_tag_repository_delete_when_deleteTag() {
        Tag foundTag = new Tag(1L, "tagName");
        when(tagRepository.findById(foundTag.getId())).thenReturn(Optional.of(foundTag));

        tagService.deleteTag(foundTag.getId());

        verify(tagRepository).delete(foundTag);
    }

    @Test
    void if_tag_not_found_when_delete_then_exception_thrown() {
        assertThrows(ResourceNotFoundException.class, () -> tagService.deleteTag(1L));
    }

    @Test
    void should_return_tag_dto_when_get_prevalent_tag() {
        Tag foundTag = new Tag(1L, "tagName");
        when(tagRepository.getPrevalentTagOfMostProfitableUser()).thenReturn(Optional.of(foundTag));
        TagDto expected = new TagDto(foundTag.getId(), foundTag.getName());

        TagDto actual = tagService.getPrevalentTagOfMostProfitableUser();

        assertEquals(expected, actual);
    }

    @Test
    void if_tag_not_found_when_get_prevalent_tag_then_exception_thrown() {
        assertThrows(ResourceNotFoundException.class, () -> tagService.getPrevalentTagOfMostProfitableUser());
    }
}
