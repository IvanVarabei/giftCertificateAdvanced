package com.epam.esm.controller;

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

    @GetMapping
    public ResponseEntity<TagDto> getMostCommonTagOfUserWithHighestCostOfAllOrders() {
        return ResponseEntity.ok().body(tagService.getMostCommonTagOfUserWithHighestCostOfAllOrders());
    }
}
