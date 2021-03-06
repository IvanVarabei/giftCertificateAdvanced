package com.epam.esm.controller;

import com.epam.esm.dto.*;
import com.epam.esm.dto.search.SortByField;
import com.epam.esm.dto.search.SortOrder;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.hateoas.HateoasService;
import com.epam.esm.service.hateoas.PaginationHateoas;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
@Validated
public class CertificateController {
    private final GiftCertificateService giftCertificateService;
    private final PaginationHateoas<GiftCertificateDto> paginationHateoas;
    private final HateoasService hateoasService;

    /**
     * The method allows {@link com.epam.esm.entity.GiftCertificate} creating.
     *
     * @param giftCertificateDto should be valid according to {@link GiftCertificateDto}. Otherwise, certificate won't
     *                           be created. Error will be returned(400).
     * @return ResponseEntity witch contains created certificate with generated id. Response code 201.
     */
    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<GiftCertificateDto> createCertificate(
            @Valid @RequestBody GiftCertificateDto giftCertificateDto) {
        GiftCertificateDto certificateDto = giftCertificateService.createCertificate(giftCertificateDto);
        hateoasService.attachHateoas(certificateDto);
        return ResponseEntity.status(CREATED).body(certificateDto);
    }

    /**
     * The method allows a client to get certificates paginated. All params are optional and can be used in conjunction:
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
     * @param pageRequest created automatically from uri params (page, size).
     * @param uriBuilder  is necessary for creating hateoas pagination.
     * @param request     is necessary for creating hateoas pagination.
     * @return Response entity containing page object. Response code 200.
     */
    @GetMapping
    public CustomPage<GiftCertificateDto> getCertificates(
            @RequestParam(required = false) List<@Pattern(regexp = "\\w{2,64}") String> tagName,
            @RequestParam(required = false) @Pattern(regexp = "\\w{2,64}") String name,
            @RequestParam(required = false) @Pattern(regexp = ".{2,512}") String description,
            @RequestParam(required = false) SortByField sortByField,
            @RequestParam(required = false) SortOrder sortOrder,
            @Valid CustomPageable pageRequest,
            UriComponentsBuilder uriBuilder,
            HttpServletRequest request
    ) {
        SearchCertificateDto searchCertificateDto = SearchCertificateDto.builder()
                .tagNames(tagName)
                .name(name)
                .description(description)
                .sortByField(sortByField)
                .sortOrder(sortOrder)
                .pageRequest(pageRequest)
                .build();
        CustomPage<GiftCertificateDto> certificateDtoPage = giftCertificateService.getPaginated(searchCertificateDto);
        certificateDtoPage.getContent().forEach(hateoasService::attachHateoas);
        uriBuilder.path(request.getRequestURI());
        uriBuilder.query(request.getQueryString());
        paginationHateoas.addPaginationLinks(uriBuilder, certificateDtoPage);
        return certificateDtoPage;
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
        GiftCertificateDto certificateDto = giftCertificateService.getCertificateById(certificateId);
        hateoasService.attachHateoas(certificateDto);
        return ResponseEntity.ok().body(certificateDto);
    }

    /**
     * Provides ability to update certificate entirely.
     *
     * @param giftCertificateDto should have all fields filled and valid. Otherwise error will be returned(400).
     * @return updated certificate wrapped into {@link GiftCertificateDto}. Response code 200.
     */
    @PutMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<GiftCertificateDto> updateCertificate(
            @Valid @RequestBody GiftCertificateDto giftCertificateDto) {
        GiftCertificateDto certificateDto = giftCertificateService.updateCertificate(giftCertificateDto);
        hateoasService.attachHateoas(certificateDto);
        return ResponseEntity.ok().body(certificateDto);
    }

    /**
     * Provides ability to update only price using lightweight dto. So client shouldn't send the whole object.
     *
     * @param priceDto contains certificateId and new price.
     * @return the whole certificate object having all fields populated.
     */
    @PatchMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<GiftCertificateDto> updatePrice(@Valid @RequestBody PriceDto priceDto) {
        GiftCertificateDto certificateDto = giftCertificateService.updatePrice(priceDto);
        hateoasService.attachHateoas(certificateDto);
        return ResponseEntity.ok().body(certificateDto);
    }

    /**
     * Allows certificate deleting. CertificateId should be passed.
     *
     * @param certificateId should be positive integer number.
     * @return responseEntity having empty body. Response code 204.
     */
    @DeleteMapping("/{certificateId}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<GiftCertificateDto> deleteCertificate(
            @PathVariable("certificateId") @Min(1) Long certificateId) {
        giftCertificateService.deleteCertificate(certificateId);
        return ResponseEntity.noContent().build();
    }
}
