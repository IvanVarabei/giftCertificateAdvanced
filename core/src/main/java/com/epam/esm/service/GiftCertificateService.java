package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.SearchCertificateDto;

import java.util.List;

public interface GiftCertificateService {
    GiftCertificateDto createCertificate(GiftCertificateDto giftCertificateDto);

    List<GiftCertificateDto> getCertificates(SearchCertificateDto searchDto);

    GiftCertificateDto getCertificateById(Long certificateId);

    GiftCertificateDto updateCertificate(GiftCertificateDto giftCertificateDto);

    void deleteCertificate(Long certificateId);
}
