package com.epam.esm.mapper;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateAsOrderItem;
import com.epam.esm.entity.Tag;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CertificateConverter {
    GiftCertificateDto toDTO(GiftCertificate certificate);

    GiftCertificate toEntity(GiftCertificateDto giftCertificateDto);

    List<Tag> toEntities(List<TagDto> tagsDto);

    List<TagDto> toDTOs(List<Tag> tags);

    GiftCertificateAsOrderItem dtoToOrderItem(GiftCertificateDto certificateDto);
}
