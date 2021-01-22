package com.epam.esm.mapper;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import java.util.List;

//@Component
//@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
//        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CertificateConverter {
    GiftCertificateDto toDTO(GiftCertificate giftCertificate);

    GiftCertificate toEntity(GiftCertificateDto dto);

    List<Tag> toEntities(List<TagDto> tagsDto);

    List<TagDto> toDTOs(List<Tag> tags);
}
