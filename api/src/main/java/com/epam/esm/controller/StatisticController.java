package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import com.epam.esm.service.hateoas.HateoasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticController {
    private final TagService tagService;
    private final HateoasService hateoasService;

    /**
     * The method finds the most widely used tag of a user with the highest cost of all orders.
     *
     * @return found tag
     */
    @GetMapping("/prevalent")
    public ResponseEntity<TagDto> getPrevalentTagOfMostProfitableUser() {
        TagDto tagDto = tagService.getPrevalentTagOfMostProfitableUser();
        hateoasService.attachHateoas(tagDto);
        return ResponseEntity.ok().body(tagDto);
    }
}
