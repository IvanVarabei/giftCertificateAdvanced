package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.PriceDto;
import com.epam.esm.dto.SearchCertificateDto;
import com.epam.esm.dto.search.SortByField;
import com.epam.esm.dto.search.SortOrder;
import com.epam.esm.service.GiftCertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

/**
 * The class provides operations having to do with {@link com.epam.esm.entity.GiftCertificate}
 */
@RestController
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
@Validated
public class CertificateController {
    private final GiftCertificateService giftCertificateService;

    /**
     * The method allows creating {@link com.epam.esm.entity.GiftCertificate}.
     *
     * @param giftCertificateDto should be valid according to {@link GiftCertificateDto}. Otherwise, certificate won't
     *                           be created. Error will be returned(400).
     * @return ResponseEntity witch contains created certificate with generated id. Response code 201.
     */
    @PostMapping
    public ResponseEntity<GiftCertificateDto> createCertificate(
            @Valid @RequestBody GiftCertificateDto giftCertificateDto) {
        return ResponseEntity.status(CREATED).body(giftCertificateService.createCertificate(giftCertificateDto));
    }

    /**
     * The method allows a client to get certificates with tags. All params are optional and can be used in conjunction:
     * <ul>
     *  <li>by tag name</li>
     *  <li>search by part of name/description</li>
     *  <li>sort by date or by name ASC/DESC</li>
     * </ul>
     *
     * @param tagName     is a list of tags that found certificates should have.
     * @param name        or part of gift certificate's name.
     * @param description or part of gift certificate's description.
     * @param sortByField can be either name or last updated date.
     * @param sortOrder   either ascending or descending.
     * @return Response entity containing the list of certificates. Response code 200.
     */
    @GetMapping
    public ResponseEntity<List<GiftCertificateDto>> getCertificates(
            @RequestParam(required = false) List<@Pattern(regexp = "\\w{2,64}") String> tagName,
            @RequestParam(required = false) @Pattern(regexp = "\\w{2,64}") String name,
            @RequestParam(required = false) @Pattern(regexp = ".{2,512}") String description,
            @RequestParam(required = false) SortByField sortByField,
            @RequestParam(required = false) SortOrder sortOrder
    ) {
        SearchCertificateDto searchCertificateDto = SearchCertificateDto.builder()
                .tagNames(tagName)
                .name(name)
                .description(description)
                .sortByField(sortByField)
                .sortOrder(sortOrder)
                .build();
        return ResponseEntity.ok().body(giftCertificateService.getCertificates(searchCertificateDto));
    }

    /**
     * The method provide a giftCertificate having passed id. If it's absent error will be returned(404).
     *
     * @param certificateId should be positive integer number.
     * @return {@link GiftCertificateDto}. Response code 200.
     */
    @GetMapping("/{certificateId}")
    public ResponseEntity<GiftCertificateDto> getCertificateById(
            @PathVariable("certificateId") @Min(1) Long certificateId) {
        return ResponseEntity.ok().body(giftCertificateService.getCertificateById(certificateId));
    }

    /**
     * Provides ability to update certificate entirely.
     *
     * @param giftCertificateDto should have all fields filled and valid. Otherwise error will be returned(400).
     * @return updated certificate wrapped into {@link GiftCertificateDto}. Response code 200.
     */
    @PutMapping
    public ResponseEntity<GiftCertificateDto> updateCertificate(
            @Valid @RequestBody GiftCertificateDto giftCertificateDto) {
        return ResponseEntity.ok().body(giftCertificateService.updateCertificate(giftCertificateDto));
    }

    /**
     * Allows certificate deleting. CertificateId should be passed.
     *
     * @param certificateId should be positive integer number.
     * @return responseEntity having empty body. Response code 204.
     */
    @DeleteMapping("/{certificateId}")
    public ResponseEntity<GiftCertificateDto> deleteCertificate(
            @PathVariable("certificateId") @Min(1) Long certificateId) {
        giftCertificateService.deleteCertificate(certificateId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping
    public ResponseEntity<GiftCertificateDto> updatePrice(@Valid @RequestBody PriceDto priceDto) {
        return ResponseEntity.ok().body(giftCertificateService.updatePrice(priceDto));
    }
}
