package com.epam.esm.service;

import com.epam.esm.dto.CustomPageable;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.PriceDto;
import com.epam.esm.dto.SearchCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.mapper.CertificateConverter;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class CertificateServiceImplTest {
    GiftCertificateRepository certificateRepository;
    GiftCertificateService giftCertificateService;
    GiftCertificate certificate;
    @Autowired
    CertificateConverter certificateConverter;

    {
        certificate = new GiftCertificate();
        certificate.setId(1L);
        certificate.setName("name test 1");
        certificate.setDescription("description test 1");
        certificate.setPrice(new BigDecimal(100));
        certificate.setDuration(5);
        certificate.setCreatedDate(LocalDateTime.now());
        certificate.setUpdatedDate(LocalDateTime.now());
    }

    @BeforeEach
    public void setUp() {
        certificateRepository = mock(GiftCertificateRepository.class);
        giftCertificateService = new GiftCertificateServiceImpl(certificateRepository, certificateConverter);
    }

    @Test
    void should_invoke_certificateRepository_update_when_createCertificate() {
        GiftCertificateDto certificateDto = new GiftCertificateDto();
        when(certificateRepository.save(any())).thenReturn(certificate);

        giftCertificateService.createCertificate(certificateDto);

        verify(certificateRepository).update(certificate);
    }

    @Test
    void should_invoke_findPaginated_when_getPaginated() {
        SearchCertificateDto searchDto = SearchCertificateDto.builder()
                .pageRequest(new CustomPageable(3, 4))
                .build();
        when(certificateRepository.countAll(searchDto)).thenReturn(20L);

        giftCertificateService.getPaginated(searchDto);

        verify(certificateRepository).findPaginated(searchDto);
    }

    @Test
    void if_requested_page_grater_than_last_page_exception_thrown() {
        SearchCertificateDto searchDto = SearchCertificateDto.builder()
                .pageRequest(new CustomPageable(3, 7))
                .build();
        when(certificateRepository.countAll(searchDto)).thenReturn(20L);

        assertThrows(ResourceNotFoundException.class, () -> giftCertificateService.getPaginated(searchDto));
    }

    @Test
    void should_throw_ResourceNotFoundException_when_certificate_not_found() {
        assertThrows(ResourceNotFoundException.class, () -> giftCertificateService.getCertificateById(1L));
    }

    @Test
    void returns_certificate_having_specified_id_when_getCertificateById() {
        when(certificateRepository.findById(any())).thenReturn(Optional.of(certificate));

        GiftCertificateDto certificateDTO = giftCertificateService.getCertificateById(certificate.getId());

        assertNotNull(certificateDTO.getId());
        assertEquals(certificate.getName(), certificateDTO.getName());
        assertEquals(certificate.getDescription(), certificateDTO.getDescription());
        assertEquals(certificate.getPrice(), certificateDTO.getPrice());
        assertEquals(certificate.getDuration(), certificateDTO.getDuration());
    }

    @Test
    void should_invoke_certificateRepository_update_when_updateCertificate() {
        GiftCertificateDto updateCertificateDto = new GiftCertificateDto();
        updateCertificateDto.setId(certificate.getId());
        when(certificateRepository.findById(certificate.getId())).thenReturn(Optional.of(certificate));

        giftCertificateService.updateCertificate(updateCertificateDto);

        verify(certificateRepository).update(any());
    }

    @Test
    void should_throw_ResourceNotFoundException_when_certificate_not_found_when_update() {
        GiftCertificateDto certificateDto = new GiftCertificateDto();
        certificateDto.setId(certificate.getId());

        assertThrows(ResourceNotFoundException.class, () -> giftCertificateService.updateCertificate(certificateDto));
    }

    @Test
    void should_return_updated_certificate_dto_when_update_price() {
        PriceDto priceDto = new PriceDto();
        priceDto.setId(certificate.getId());
        priceDto.setPrice(BigDecimal.valueOf(25));
        when(certificateRepository.findById(certificate.getId())).thenReturn(Optional.of(certificate));

        GiftCertificateDto expectedCertificateDto = giftCertificateService.updatePrice(priceDto);

        assertEquals(priceDto.getPrice(), expectedCertificateDto.getPrice());
    }

    @Test
    void should_throw_ResourceNotFoundException_when_certificate_not_found_when_update_price() {
        PriceDto priceDto = new PriceDto();
        priceDto.setId(certificate.getId());

        assertThrows(ResourceNotFoundException.class, () -> giftCertificateService.updatePrice(priceDto));
    }

    @Test
    void should_invoke_certificateRepository_delete_when_deleteCertificate() {
        when(certificateRepository.findById(certificate.getId())).thenReturn(Optional.of(certificate));

        giftCertificateService.deleteCertificate(1L);

        verify(certificateRepository).delete(certificate);
    }

    @Test
    void should_throw_ResourceNotFoundException_when_certificate_not_found_when_delete() {
        assertThrows(ResourceNotFoundException.class, () -> giftCertificateService.deleteCertificate(1L));
    }
}