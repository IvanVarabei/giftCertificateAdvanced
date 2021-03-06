package com.epam.esm.service;

import com.epam.esm.dto.CustomPage;
import com.epam.esm.dto.CustomPageable;
import com.epam.esm.dto.TagDto;

public interface TagService {
    TagDto createTag(TagDto tagDto);

    CustomPage<TagDto> getPaginated(CustomPageable pageRequest);

    TagDto getTagById(Long tagId);

    TagDto updateTag(TagDto tagDto);

    void deleteTag(Long tagId);

    TagDto getPrevalentTagOfMostProfitableUser();
}
