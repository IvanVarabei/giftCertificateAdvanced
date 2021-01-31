package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


/**
 * The class provides operations having to do with {@link com.epam.esm.entity.Tag}
 */
@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@Validated
public class TagController {
    private final TagService tagService;
    private final PagedResourcesAssembler<TagDto> pagedResourcesAssembler;

    /**
     * The method allows creating {@link com.epam.esm.entity.Tag}.
     *
     * @param tagDto should be valid according to {@link TagDto}. Otherwise, tag won't be created.
     * @return ResponseEntity witch contains created tag with generated id. Response code 201.
     */
    @PostMapping
    public ResponseEntity<TagDto> createTag(@RequestBody @Valid TagDto tagDto) {
        TagDto createdTagDto = tagService.createTag(tagDto);
        Link selfLink = linkTo(methodOn(TagController.class)
                .getTagById(tagDto.getId())).withSelfRel();
        createdTagDto.add(selfLink);
        URI locationUri = selfLink.toUri();
        return ResponseEntity.created(locationUri).body(createdTagDto);
    }

    /**
     * The method provides all existing tags.
     *
     * @return list of {@link TagDto}. Response code 200.
     */
    @GetMapping
    public PagedModel<EntityModel<TagDto>> getTags(@PageableDefault Pageable pageRequest) {
        Page<TagDto> tags = tagService.findPaginated(pageRequest);
        for (TagDto tagDto : tags.getContent()) {
            Link tagDtoSelfLink = linkTo(methodOn(TagController.class)
                    .getTagById(tagDto.getId())).withSelfRel();
            tagDto.add(tagDtoSelfLink);
        }
        Link selfLink = Link.of(ServletUriComponentsBuilder.fromCurrentRequest().build().toString());
        return pagedResourcesAssembler.toModel(tags, selfLink);
    }

    /**
     * The method provide a tag having passed id. If it's absent error will be returned(404).
     *
     * @param tagId should be positive integer number.
     * @return TagDto. Response code 200.
     */
    @GetMapping("/{tagId}")
    public ResponseEntity<TagDto> getTagById(@PathVariable("tagId") @Min(1) Long tagId) {
        return ResponseEntity.ok().body(tagService.getTagById(tagId));
    }

    /**
     * Provides ability to update tag entirely.
     *
     * @param tagDto should have all fields filled and valid. Otherwise error will be returned(400).
     * @return updated tag wrapped into {@link TagDto}. Response code 200.
     * @see com.epam.esm.dto.TagDto
     */
    @PutMapping
    public ResponseEntity<TagDto> updateTag(@Valid @RequestBody TagDto tagDto) {
        return ResponseEntity.ok().body(tagService.updateTag(tagDto));
    }

    /**
     * Allows tag deleting. TagId should be passed.
     *
     * @param tagId should be positive integer number.
     * @return responseEntity having empty body. Response code 204.
     */
    @DeleteMapping("/{tagId}")
    public ResponseEntity<TagDto> deleteTagById(@PathVariable("tagId") @Min(1) Long tagId) {
        tagService.deleteTag(tagId);
        return ResponseEntity.noContent().build();
    }
}
