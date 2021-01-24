package com.epam.esm.repository;

import com.epam.esm.config.EmbeddedTestConfig;
import com.epam.esm.dto.SearchCertificateDto;
import com.epam.esm.dto.search.SortByField;
import com.epam.esm.dto.search.SortOrder;
import com.epam.esm.entity.GiftCertificate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ContextConfiguration(classes = {EmbeddedTestConfig.class})
class GiftCertificateRepositoryImplTest {
    @Autowired
    GiftCertificateRepository certificateRepository;

    @Test
    void should_id_not_be_null_when_save() {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("name test 11");
        certificate.setDescription("description test 1");
        certificate.setPrice(new BigDecimal(100));
        certificate.setDuration(5);
        certificate.setCreatedDate(LocalDateTime.now());
        certificateRepository.save(certificate);

        Assertions.assertNotNull(certificate.getId());
    }

    @Test
    void should_return_certificates_corresponding_the_search_dto() {
        SearchCertificateDto searchDto = new SearchCertificateDto(
                List.of("cheap"),
                "e",
                "f",
                SortByField.NAME,
                SortOrder.DESC
        );

        List<GiftCertificate> giftCertificateList = certificateRepository.findAll(searchDto);
        long actualAmount = giftCertificateList.size();
        long checkedAmount = giftCertificateList.stream()
                .filter(c -> c.getName().contains("e"))
                .filter(c -> c.getDescription().contains("f"))
                .count();

        assertEquals(checkedAmount, actualAmount);
    }

    @Test
    void should_find_certificate_having_specified_id() {
        Optional<GiftCertificate> giftCertificateOptional = certificateRepository.findById(1L);

        assertEquals(1L, (long) giftCertificateOptional.get().getId());
    }

    @Test
    void should_be_updated_name_after_update() {
        GiftCertificate initialCertificate = new GiftCertificate();
        initialCertificate.setName("initialName");
        initialCertificate.setDescription("initialDescription");
        initialCertificate.setPrice(BigDecimal.ONE);
        initialCertificate.setDuration(2);
        initialCertificate.setCreatedDate(LocalDateTime.now());
        Long id = certificateRepository.save(initialCertificate).getId();

        GiftCertificate update = new GiftCertificate();
        update.setId(id);
        update.setName("updatedName");
        update.setDescription("updatedDescription");
        update.setPrice(BigDecimal.TEN);
        update.setDuration(1);
        update.setUpdatedDate(LocalDateTime.now());

        certificateRepository.update(update);

        assertEquals(update.getName(), certificateRepository.findById(id).get().getName());
    }

    @Test
    void should_be_empty_optional_after_delete_when_find_by_id() {
        GiftCertificate initialCertificate = new GiftCertificate();
        initialCertificate.setName("testDelete");
        initialCertificate.setDescription("testDelete");
        initialCertificate.setPrice(BigDecimal.ONE);
        initialCertificate.setDuration(2);
        initialCertificate.setCreatedDate(LocalDateTime.now());
        Long id = certificateRepository.save(initialCertificate).getId();

        certificateRepository.delete(id);

        assertEquals(Optional.empty(), certificateRepository.findById(id));
    }
}