package com.epam.esm.integration;

import com.epam.esm.dto.CustomPage;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ExceptionDto;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.security.JwtTokenProvider;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TagControllerTest {
    @Autowired
    TagRepository tagRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    String token;

    @BeforeAll
    void setup(@Autowired DataSource dataSource, @Autowired JwtTokenProvider tokenProvider) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO \"user\" (password, email, role) " +
                    "VALUES ('qwerty', 'testTagController@gamil.com', 'ROLE_ADMIN')");
        }
        token = "Bearer " + tokenProvider.createToken("testTagController@gamil.com", Role.ROLE_ADMIN);
    }

    @Test
    void should_return_created_tag_having_generated_id() throws Exception {
        String responseAsString = mockMvc
                .perform(post("/api/tags")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new TagDto(null, "testTagName")))
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        TagDto createdTag = objectMapper.readValue(responseAsString, TagDto.class);

        assertNotNull(createdTag.getId());
    }

    @Test
    void should_return_not_empty_list_of_exception_dto_when_crete_non_valid_tag() throws Exception {
        String responseAsString = mockMvc
                .perform(post("/api/tags")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new TagDto(null, "not valid name"))))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<ExceptionDto> exceptionDtoList = objectMapper.readValue(responseAsString, new TypeReference<>() {
        });

        assertFalse(exceptionDtoList.isEmpty());
    }

    @Test
    @Transactional
    void should_return_not_empty_list_of_tags_when_get_all() throws Exception {
        Tag tag = new Tag();
        tag.setName("testGetAll");
        tagRepository.save(tag);

        String responseAsString = mockMvc
                .perform(get("/api/tags"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        CustomPage<Tag> foundPage = objectMapper.readValue(responseAsString, new TypeReference<>() {
        });

        assertFalse(foundPage.getContent().isEmpty());
    }

    @Test
    @Transactional
    void should_return_tag_having_specified_id() throws Exception {
        Tag tag = new Tag();
        tag.setName("testGeById");
        Long id = tagRepository.save(tag).getId();

        String responseAsString = mockMvc
                .perform(get("/api/tags/" + id))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        TagDto foundTag = objectMapper.readValue(responseAsString, TagDto.class);

        assertEquals(id, foundTag.getId());
    }

    @Test
    @Transactional
    void find_by_id_should_return_empty_optional_after_delete() throws Exception {
        Tag tag = new Tag();
        tag.setName("testGetDelete");
        Long id = tagRepository.save(tag).getId();

        mockMvc.perform(delete("/api/tags/" + id).header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().is(204));

        assertFalse(tagRepository.findById(id).isPresent());
    }
}
