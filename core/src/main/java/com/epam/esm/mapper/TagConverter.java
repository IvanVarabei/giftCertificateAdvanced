package com.epam.esm.mapper;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;

//@Component
//@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
//        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagConverter {
    TagDto toDTO(Tag artifact);

    Tag toEntity(TagDto dto);
}
