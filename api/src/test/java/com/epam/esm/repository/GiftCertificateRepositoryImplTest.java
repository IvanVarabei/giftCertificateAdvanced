package com.epam.esm.repository;

import com.epam.esm.dto.CustomPageable;
import com.epam.esm.dto.SearchCertificateDto;
import com.epam.esm.dto.search.SortByField;
import com.epam.esm.dto.search.SortOrder;
import com.epam.esm.entity.GiftCertificate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class GiftCertificateRepositoryImplTest {
    @Autowired
    GiftCertificateRepository certificateRepository;

    @Test
    @Transactional
    void should_id_not_be_null_when_save() {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("name test 11");
        certificate.setDescription("description test 1");
        certificate.setPrice(new BigDecimal(100));
        certificate.setDuration(5);
        certificate.setCreatedDate(LocalDateTime.now());
        certificate.setUpdatedDate(LocalDateTime.now());
        certificateRepository.save(certificate);

        Assertions.assertNotNull(certificate.getId());
    }

    @Test
    void should_return_certificates_corresponding_the_search_dto() {
        SearchCertificateDto searchDto = SearchCertificateDto.builder()
                .pageRequest(new CustomPageable())
                .tagNames(List.of("cheap"))
                .name("e")
                .description("f")
                .sortByField(SortByField.NAME)
                .sortOrder(SortOrder.DESC)
                .build();

        List<GiftCertificate> giftCertificateList = certificateRepository.findPaginated(searchDto);
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

        assertEquals(1L, (long) giftCertificateOptional.orElseThrow(AssertionError::new).getId());
    }

    @Test
    @Transactional
    void should_be_updated_name_after_update() {
        GiftCertificate initialCertificate = new GiftCertificate();
        initialCertificate.setName("initialName");
        initialCertificate.setDescription("initialDescription");
        initialCertificate.setPrice(BigDecimal.ONE);
        initialCertificate.setDuration(2);
        initialCertificate.setCreatedDate(LocalDateTime.now());
        initialCertificate.setUpdatedDate(LocalDateTime.now());
        Long id = certificateRepository.save(initialCertificate).getId();

        GiftCertificate update = new GiftCertificate();
        update.setId(id);
        update.setName("updatedName");
        update.setDescription("updatedDescription");
        update.setPrice(BigDecimal.TEN);
        update.setDuration(1);
        update.setUpdatedDate(LocalDateTime.now());

        certificateRepository.update(update);

        assertEquals(update.getName(), certificateRepository.findById(id).orElseThrow(AssertionError::new).getName());
    }

    @Test
    @Transactional
    void should_be_empty_optional_after_delete_when_find_by_id() {
        GiftCertificate initialCertificate = new GiftCertificate();
        initialCertificate.setName("testDelete");
        initialCertificate.setDescription("testDelete");
        initialCertificate.setPrice(BigDecimal.ONE);
        initialCertificate.setDuration(2);
        initialCertificate.setCreatedDate(LocalDateTime.now());
        initialCertificate.setUpdatedDate(LocalDateTime.now());
        GiftCertificate savedCertificate = certificateRepository.save(initialCertificate);

        certificateRepository.delete(savedCertificate);

        assertEquals(Optional.empty(), certificateRepository.findById(savedCertificate.getId()));
    }
}