package com.epam.esm.repository;

import com.epam.esm.dto.SearchCertificateDto;
import com.epam.esm.entity.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateRepository {
    GiftCertificate save(GiftCertificate giftCertificate);

    List<GiftCertificate> findAll(SearchCertificateDto searchDto);

    Optional<GiftCertificate> findById(Long certificateId);

    void update(GiftCertificate giftCertificate);

    void updatePrice(GiftCertificate giftCertificate);

    void delete(Long giftCertificateId);
}
