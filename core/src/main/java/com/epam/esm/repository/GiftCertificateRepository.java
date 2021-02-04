package com.epam.esm.repository;

import com.epam.esm.dto.SearchCertificateDto;
import com.epam.esm.entity.GiftCertificate;

import java.util.List;

public interface GiftCertificateRepository extends CustomCrudRepository<GiftCertificate> {
    List<GiftCertificate> findPaginated(SearchCertificateDto searchDto);

    Long countAll(SearchCertificateDto searchDto);

    void updatePrice(GiftCertificate giftCertificate);
}
