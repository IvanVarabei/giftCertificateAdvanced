package com.epam.esm.integration;

import com.epam.esm.dto.CustomPage;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Role;
import com.epam.esm.exception.ExceptionDto;
import com.epam.esm.repository.GiftCertificateRepository;
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
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CertificateControllerTest {
    @Autowired
    GiftCertificateRepository certificateRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    String token;

    @BeforeAll
    void setup(@Autowired DataSource dataSource, @Autowired JwtTokenProvider tokenProvider) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("INSERT INTO \"user\" (password, email, role) " +
                    "VALUES ('qwerty', 'testCertificateController@gamil.com', 'ROLE_ADMIN')");
        }
        token = "Bearer " + tokenProvider.createToken("testCertificateController@gamil.com", Role.ROLE_ADMIN);
    }

    @Test
    void should_return_created_certificate_having_generated_id() throws Exception {
        GiftCertificateDto certificateDto = new GiftCertificateDto();
        certificateDto.setName("creteCertificate");
        certificateDto.setDescription("test description");
        certificateDto.setPrice(BigDecimal.TEN);
        certificateDto.setDuration(1);
        certificateDto.setTags(Set.of(new TagDto(null, "testCreateCertificateTagName")));

        String responseAsString = mockMvc
                .perform(post("/api/certificates")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(certificateDto))
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        GiftCertificateDto createdCertificate = objectMapper.readValue(responseAsString, GiftCertificateDto.class);

        assertNotNull(createdCertificate.getId());
        assertNotNull(createdCertificate.getTags().stream().findAny().get().getId());
    }

    @Test
    void should_return_not_empty_list_of_exception_dto_when_crete_non_valid_certificate() throws Exception {
        String responseAsString = mockMvc
                .perform(post("/api/certificates")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new GiftCertificateDto())))
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
    void should_return_not_empty_list_of_certificates_after_get_method() throws Exception {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("testGetAll");
        certificate.setDescription("test description");
        certificate.setPrice(BigDecimal.TEN);
        certificate.setDuration(1);
        certificate.setCreatedDate(LocalDateTime.now());
        certificate.setUpdatedDate(LocalDateTime.now());
        certificateRepository.save(certificate);

        String responseAsString = mockMvc
                .perform(get("/api/certificates"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        CustomPage<GiftCertificateDto> foundPage = objectMapper.readValue(responseAsString, new TypeReference<>() {
        });

        assertFalse(foundPage.getContent().isEmpty());
    }

    @Test
    @Transactional
    void should_return_certificate_having_specified_id_when_get_by_id() throws Exception {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("testGetById");
        certificate.setDescription("test description");
        certificate.setPrice(BigDecimal.TEN);
        certificate.setDuration(1);
        certificate.setCreatedDate(LocalDateTime.now());
        certificate.setUpdatedDate(LocalDateTime.now());
        Long id = certificateRepository.save(certificate).getId();

        String responseAsString = mockMvc
                .perform(get("/api/certificates/" + id))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        GiftCertificateDto foundCertificate = objectMapper.readValue(responseAsString, GiftCertificateDto.class);

        assertEquals(id, foundCertificate.getId());
    }

    @Test
    @Transactional
    void should_return_certificate_having_updated_fields_after_update() throws Exception {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("testUpdate");
        certificate.setDescription("test description");
        certificate.setPrice(BigDecimal.TEN);
        certificate.setDuration(1);
        certificate.setCreatedDate(LocalDateTime.now());
        certificate.setUpdatedDate(LocalDateTime.now());
        Long id = certificateRepository.save(certificate).getId();

        GiftCertificateDto certificateDto = new GiftCertificateDto();
        certificateDto.setId(id);
        certificateDto.setName("updated name");
        certificateDto.setDescription("updated description");
        certificateDto.setPrice(BigDecimal.ONE);
        certificateDto.setDuration(2);
        certificateDto.setTags(Set.of(new TagDto(null, "newCreatedTag")));

        String responseAsString = mockMvc
                .perform(put("/api/certificates")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(certificateDto))
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        GiftCertificateDto updatedCertificate = objectMapper.readValue(responseAsString, GiftCertificateDto.class);

        assertEquals("updated name", updatedCertificate.getName());
        assertEquals("newCreatedTag", updatedCertificate.getTags()
                .stream().findAny().orElseThrow(AssertionError::new).getName());
    }

    @Test
    @Transactional
    void repository_should_return_empty_optional_after_delete() throws Exception {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("testDelete");
        certificate.setDescription("test description");
        certificate.setPrice(BigDecimal.TEN);
        certificate.setDuration(1);
        certificate.setCreatedDate(LocalDateTime.now());
        certificate.setUpdatedDate(LocalDateTime.now());
        Long id = certificateRepository.save(certificate).getId();

        mockMvc.perform(delete("/api/certificates/" + id).header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().is(204));

        assertFalse(certificateRepository.findById(id).isPresent());
    }
}
