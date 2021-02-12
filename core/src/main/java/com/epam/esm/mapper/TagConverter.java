package com.epam.esm.mapper;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagConverter {
    TagDto toDTO(Tag tag);

    Tag toEntity(TagDto tagDto);
}
