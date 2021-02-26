//package com.epam.esm.service.impl;
//
//import com.epam.esm.dto.TagDto;
//import com.epam.esm.entity.Tag;
//import com.epam.esm.mapper.TagConverter;
//import com.epam.esm.mapper.TagConverterImpl;
//import com.epam.esm.repository.TagRepository;
//import com.epam.esm.service.TagService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//import static org.mockito.Mockito.mock;
//
//
//class TagServiceImplTest {
//    TagService tagService;
//    TagRepository tagRepository;
//    Tag tag;
//
//    {
//        tag = new Tag();
//        tag.setId(1L);
//        tag.setName("tagTestName");
//    }
//
//    @BeforeEach
//    public void setUp() {
//        tagRepository = mock(TagRepository.class);
//        tagService = new TagServiceImpl(tagRepository, new TagConverterImpl());
//    }
//
//    @Test
//    void should_invoke_tagRepository_save_when_createTag() {
//        tagService.createTag(new TagDto(null, "tagTestName"));
//
//        verify(tagRepository).save(any());
//    }
//
//    @Test
//    void getTagById() {
//
//    }
//}