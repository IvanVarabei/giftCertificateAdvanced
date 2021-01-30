package com.epam.esm.service.impl;

import com.epam.esm.dto.Page;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ErrorMessage;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.mapper.TagConverter;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final TagConverter tagConverter;

    @Override
    public TagDto createTag(TagDto tagDto) {
        Tag tag = tagConverter.toEntity(tagDto);
        return tagConverter.toDTO(tagRepository.save(tag));
    }

    @Override
    public Page<TagDto> findPaginated(Integer page, Integer size) {
        int lastPage = (tagRepository.countAll() + size - 1) / size - 1;
        if (page > lastPage) {
            throw new ResourceNotFoundException(String.format(ErrorMessage.PAGE_NOT_FOUND, size, page, lastPage));
        }
        int offset = size * page;
        List<Tag> foundTags = tagRepository.findAllPaginated(offset, size);
        Page<TagDto> pageEntity = new Page<>();
        pageEntity.setContent(foundTags.stream().map(tagConverter::toDTO).collect(Collectors.toList()));
        pageEntity.setLastPage(lastPage);
        return pageEntity;
    }

    @Override
    public TagDto getTagById(Long tagId) {
        return tagConverter.toDTO(tagRepository.findById(tagId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND, tagId))));
    }

    @Override
    public TagDto updateTag(TagDto tagDto) {
        Long tagId = tagDto.getId();
        Tag existed = tagRepository.findById(tagId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND, tagId)));
        existed.setName(tagDto.getName());
        tagRepository.update(existed);
        return tagConverter.toDTO(existed);
    }

    @Override
    @Transactional
    public void deleteTag(Long tagId) {
        tagRepository.findById(tagId).ifPresentOrElse(t -> tagRepository.delete(tagId), () -> {
            throw new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND, tagId));
        });
    }

    @Override
    public List<Tag> bindTags(Long certificateId, List<Tag> tags) {
        tags.forEach(t -> {
            Optional<Tag> tagOptional = tagRepository.findByName(t.getName());
            if (tagOptional.isEmpty()) {
                t.setId(null);
                tagRepository.save(t);
            } else {
                Tag existedTag = tagOptional.get();
                t.setId(existedTag.getId());
            }
            tagRepository.bindWithCertificate(certificateId, t.getId());
        });
        return tags;
    }

    @Override
    public void unbindTagsFromCertificate(Long certificateId) {
        tagRepository.unbindTagsFromCertificate(certificateId);
    }

    @Override
    public List<Tag> getTagsByCertificateId(Long certificateId) {
        return tagRepository.getTagsByCertificateId(certificateId);
    }

    @Override
    public TagDto getMostCommonTagOfUserWithHighestCostOfAllOrders() {
        Optional<Tag> tagOptional = tagRepository.getMostCommonTagOfUserWithHighestCostOfAllOrders();
        return tagConverter.toDTO(tagOptional.get());
    }
}
