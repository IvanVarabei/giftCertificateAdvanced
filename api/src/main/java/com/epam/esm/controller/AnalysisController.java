package com.epam.esm.controller;

import com.epam.esm.controller.hateoas.DtoHateoas;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
@Validated
public class AnalysisController {
    private final TagService tagService;
    private final DtoHateoas dtoHateoas;

    @GetMapping
    public ResponseEntity<TagDto> getPrevalentTagOfMostProfitableUser() {
        TagDto tagDto = tagService.getPrevalentTagOfMostProfitableUser();
        dtoHateoas.attachHateoas(tagDto);
        return ResponseEntity.ok().body(tagDto);
    }
}
