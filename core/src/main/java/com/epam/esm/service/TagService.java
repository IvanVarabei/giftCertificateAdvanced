package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {
    TagDto createTag(TagDto tagDto);

    Page<TagDto> findPaginated(Pageable pageRequest);

    TagDto getTagById(Long tagId);

    TagDto updateTag(TagDto tagDto);

    void deleteTag(Long tagId);

    List<Tag> bindTags(Long certificateId, List<Tag> tags);

    void unbindTagsFromCertificate(Long id);

    List<Tag> getTagsByCertificateId(Long certificateId);

    TagDto getMostCommonTagOfUserWithHighestCostOfAllOrders();
}
