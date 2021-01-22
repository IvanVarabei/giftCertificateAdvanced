//package com.epam.esm.integration;
//
//import com.epam.esm.config.DatabaseConfig;
//import com.epam.esm.dto.TagDto;
//import com.epam.esm.entity.Tag;
//import com.epam.esm.exception.ExceptionDto;
//import com.epam.esm.repository.TagRepository;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {DatabaseConfig.class})
//@WebAppConfiguration
//class TagControllerTest {
//    @Autowired
//    WebApplicationContext wac;
//    @Autowired
//    TagRepository tagRepository;
//    ObjectMapper objectMapper;
//    MockMvc mockMvc;
//
//    @BeforeEach
//    public void setup() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//        objectMapper = new ObjectMapper();
//        objectMapper.findAndRegisterModules();
//    }
//
//    @Test
//    void should_return_created_tag_having_generated_id() throws Exception {
//        String responseAsString = mockMvc
//                .perform(post("/api/tags")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(new TagDto(null, "testTagName"))))
//                .andExpect(status().isCreated())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//        TagDto createdTag = objectMapper.readValue(responseAsString, TagDto.class);
//
//        assertNotNull(createdTag.getId());
//    }
//
//    @Test
//    void should_return_not_empty_list_of_exception_dto_when_crete_non_valid_tag() throws Exception {
//        String responseAsString = mockMvc
//                .perform(post("/api/tags")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(new TagDto(null, "not valid name"))))
//                .andExpect(status().isBadRequest())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//        List<ExceptionDto> exceptionDtoList = objectMapper.readValue(responseAsString, new TypeReference<>() {
//        });
//
//        assertFalse(exceptionDtoList.isEmpty());
//    }
//
//    @Test
//    void should_return_not_empty_list_of_tags_when_get_all() throws Exception {
//        Tag tag = new Tag();
//        tag.setName("testGetAll");
//        tagRepository.save(tag);
//
//        String responseAsString = mockMvc
//                .perform(get("/api/tags"))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//        List<Tag> foundTags = objectMapper.readValue(responseAsString, new TypeReference<>() {
//        });
//
//        assertFalse(foundTags.isEmpty());
//    }
//
//    @Test
//    void should_return_tag_having_specified_id() throws Exception {
//        Tag tag = new Tag();
//        tag.setName("testGeById");
//        Long id = tagRepository.save(tag).getId();
//
//        String responseAsString = mockMvc
//                .perform(get("/api/tags/" + id))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//        TagDto foundTag = objectMapper.readValue(responseAsString, TagDto.class);
//
//        assertEquals(id, foundTag.getId());
//    }
//
//    @Test
//    void find_by_id_should_return_empty_optional_after_delete() throws Exception {
//        Tag tag = new Tag();
//        tag.setName("testGetDelete");
//        Long id = tagRepository.save(tag).getId();
//
//        mockMvc.perform(delete("/api/tags/" + id))
//                .andExpect(status().is(204));
//
//        assertFalse(tagRepository.findById(id).isPresent());
//    }
//}
