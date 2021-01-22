//package com.epam.esm.integration;
//
//import com.epam.esm.config.EmbeddedTestConfig;
//import com.epam.esm.dto.GiftCertificateDto;
//import com.epam.esm.dto.TagDto;
//import com.epam.esm.entity.GiftCertificate;
//import com.epam.esm.exception.ExceptionDto;
//import com.epam.esm.repository.GiftCertificateRepository;
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
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {EmbeddedTestConfig.class})
//@WebAppConfiguration
//class CertificateControllerTest {
//    @Autowired
//    WebApplicationContext wac;
//    @Autowired
//    GiftCertificateRepository certificateRepository;
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
//    void should_return_created_certificate_having_generated_id() throws Exception {
//        GiftCertificateDto certificateDto = new GiftCertificateDto();
//        certificateDto.setName("creteCertificate");
//        certificateDto.setDescription("test description");
//        certificateDto.setPrice(BigDecimal.TEN);
//        certificateDto.setDuration(1);
//        certificateDto.setTags(List.of(new TagDto(null, "testCreateCertificateTagName")));
//
//        String responseAsString = mockMvc
//                .perform(post("/api/certificates")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(certificateDto)))
//                .andExpect(status().isCreated())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//        GiftCertificateDto createdCertificate = objectMapper.readValue(responseAsString, GiftCertificateDto.class);
//
//        assertNotNull(createdCertificate.getId());
//        assertNotNull(createdCertificate.getTags().get(0).getId());
//    }
//
//    @Test
//    void should_return_not_empty_list_of_exception_dto_when_crete_non_valid_certificate() throws Exception {
//        String responseAsString = mockMvc
//                .perform(post("/api/certificates")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(new GiftCertificateDto())))
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
//    void should_return_not_empty_list_of_certificates_after_get_method() throws Exception {
//        GiftCertificate certificate = new GiftCertificate();
//        certificate.setName("testGetAll");
//        certificate.setDescription("test description");
//        certificate.setPrice(BigDecimal.TEN);
//        certificate.setDuration(1);
//        certificate.setCreatedDate(LocalDateTime.now());
//        certificateRepository.save(certificate);
//
//        String responseAsString = mockMvc
//                .perform(get("/api/certificates"))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//        List<GiftCertificate> foundCertificates = objectMapper.readValue(responseAsString, new TypeReference<>() {
//        });
//
//        assertFalse(foundCertificates.isEmpty());
//    }
//
//    @Test
//    void should_return_certificate_having_specified_id_when_get_by_id() throws Exception {
//        GiftCertificate certificate = new GiftCertificate();
//        certificate.setName("testGetById");
//        certificate.setDescription("test description");
//        certificate.setPrice(BigDecimal.TEN);
//        certificate.setDuration(1);
//        certificate.setCreatedDate(LocalDateTime.now());
//        Long id = certificateRepository.save(certificate).getId();
//
//        String responseAsString = mockMvc
//                .perform(get("/api/certificates/" + id))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//        GiftCertificateDto foundCertificate = objectMapper.readValue(responseAsString, GiftCertificateDto.class);
//
//        assertEquals(id, foundCertificate.getId());
//    }
//
//    @Test
//    void should_return_certificate_having_updated_fields_after_update() throws Exception {
//        GiftCertificate certificate = new GiftCertificate();
//        certificate.setName("testUpdate");
//        certificate.setDescription("test description");
//        certificate.setPrice(BigDecimal.TEN);
//        certificate.setDuration(1);
//        certificate.setCreatedDate(LocalDateTime.now());
//        Long id = certificateRepository.save(certificate).getId();
//
//        GiftCertificateDto certificateDto = new GiftCertificateDto();
//        certificateDto.setId(id);
//        certificateDto.setName("updated name");
//        certificateDto.setDescription("updated description");
//        certificateDto.setPrice(BigDecimal.ONE);
//        certificateDto.setDuration(2);
//        certificateDto.setTags(List.of(new TagDto(null, "newCreatedTag")));
//
//        String responseAsString = mockMvc
//                .perform(put("/api/certificates")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(certificateDto)))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        GiftCertificateDto updatedCertificate = objectMapper.readValue(responseAsString, GiftCertificateDto.class);
//
//        assertEquals("updated name", updatedCertificate.getName());
//        assertEquals("newCreatedTag", updatedCertificate.getTags().get(0).getName());
//    }
//
//    @Test
//    void repository_should_return_empty_optional_after_delete() throws Exception {
//        GiftCertificate certificate = new GiftCertificate();
//        certificate.setName("testDelete");
//        certificate.setDescription("test description");
//        certificate.setPrice(BigDecimal.TEN);
//        certificate.setDuration(1);
//        certificate.setCreatedDate(LocalDateTime.now());
//        Long id = certificateRepository.save(certificate).getId();
//
//        mockMvc.perform(delete("/api/certificates/" + id))
//                .andExpect(status().is(204));
//
//        assertFalse(certificateRepository.findById(id).isPresent());
//    }
//}
