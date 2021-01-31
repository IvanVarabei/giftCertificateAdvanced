package com.epam.esm.service;

import com.epam.esm.dto.CustomPage;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.PriceDto;
import com.epam.esm.dto.SearchCertificateDto;

public interface GiftCertificateService {
    GiftCertificateDto createCertificate(GiftCertificateDto giftCertificateDto);

    CustomPage<GiftCertificateDto> getPaginated(SearchCertificateDto searchDto);

    GiftCertificateDto getCertificateById(Long certificateId);

    GiftCertificateDto updateCertificate(GiftCertificateDto giftCertificateDto);

    void deleteCertificate(Long certificateId);

    GiftCertificateDto updatePrice(PriceDto priceDto);
}
