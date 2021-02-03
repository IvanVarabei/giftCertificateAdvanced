package com.epam.esm.service.impl;

import com.epam.esm.config.TimeZoneConfig;
import com.epam.esm.dto.CustomPage;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.PriceDto;
import com.epam.esm.dto.SearchCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ErrorMessage;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.mapper.CertificateConverter;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;
    private final CertificateConverter certificateConverter;

    @Override
    @Transactional
    public GiftCertificateDto createCertificate(GiftCertificateDto giftCertificateDto) {
        GiftCertificate createdCertificate = certificateConverter.toEntity(giftCertificateDto);
        createdCertificate.setId(null);
        createdCertificate.setCreatedDate(LocalDateTime.now(TimeZoneConfig.DATABASE_ZONE));
        createdCertificate.setUpdatedDate(createdCertificate.getCreatedDate());
        createdCertificate.setTags(createdCertificate.getTags().stream().map(tag ->
                tagRepository.findByName(tag.getName()).orElse(tag)
        ).collect(Collectors.toSet()));
        giftCertificateRepository.save(createdCertificate);
        adjustDateTimeAccordingToClientTimeZone(createdCertificate, TimeZoneConfig.CLIENT_ZONE);
        return certificateConverter.toDTO(createdCertificate);
    }

    @Override
    public CustomPage<GiftCertificateDto> getPaginated(SearchCertificateDto searchDto) {
        int size = searchDto.getPageRequest().getSize();
        int page = searchDto.getPageRequest().getPage();
        int totalCertificateAmount = giftCertificateRepository.countAll(searchDto);
        int lastPage = (totalCertificateAmount + size - 1) / size - 1;
        if (page > lastPage) {
            throw new ResourceNotFoundException(String.format(ErrorMessage.PAGE_NOT_FOUND, size, page, lastPage));
        }
        List<GiftCertificate> certificates = giftCertificateRepository.findPaginated(searchDto);
        certificates.forEach(c -> adjustDateTimeAccordingToClientTimeZone(c, TimeZoneConfig.CLIENT_ZONE));
        return new CustomPage<>(certificates.stream().map(certificateConverter::toDTO).collect(Collectors.toList()),
                searchDto.getPageRequest(), (long) totalCertificateAmount);
    }

    @Override
    public GiftCertificateDto getCertificateById(Long certificateId) {
        GiftCertificate certificate = giftCertificateRepository.findById(certificateId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND, certificateId)));
        adjustDateTimeAccordingToClientTimeZone(certificate, TimeZoneConfig.CLIENT_ZONE);
        return certificateConverter.toDTO(certificate);
    }
    @Override
    @Transactional
    public GiftCertificateDto updateCertificate(GiftCertificateDto updateDto) {
        GiftCertificate existed = giftCertificateRepository
                .findById(updateDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND, updateDto.getId())));
        GiftCertificate update = certificateConverter.toEntity(updateDto);
        existed.setName(update.getName());
        existed.setPrice(update.getPrice());
        existed.setDescription(update.getDescription());
        existed.setDuration(update.getDuration());
        existed.setUpdatedDate(LocalDateTime.now(TimeZoneConfig.DATABASE_ZONE));
        existed.getTags().clear();
        existed.setTags(update.getTags());
//        existed.setTags(update.getTags().stream().map(tag ->
//                tagRepository.findByName(tag.getName()).orElseGet(() -> tagRepository.save(tag))
//        ).collect(Collectors.toSet()));
        giftCertificateRepository.update(existed);
        adjustDateTimeAccordingToClientTimeZone(existed, TimeZoneConfig.CLIENT_ZONE);
        return certificateConverter.toDTO(existed);
    }

//    @Override
//    @Transactional
//    public GiftCertificateDto updateCertificate(GiftCertificateDto updateDto) {
//        GiftCertificate existed = giftCertificateRepository
//                .findById(updateDto.getId())
//                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND, updateDto.getId())));
//        GiftCertificate update = certificateConverter.toEntity(updateDto);
//        existed.setName(update.getName());
//        existed.setPrice(update.getPrice());
//        existed.setDescription(update.getDescription());
//        existed.setDuration(update.getDuration());
//        existed.setUpdatedDate(LocalDateTime.now(TimeZoneConfig.DATABASE_ZONE));
//        existed.setTags(update.getTags().stream().map(tag ->
//                tagRepository.findByName(tag.getName()).orElseGet(() -> tagRepository.save(tag))
//        ).collect(Collectors.toSet()));
//        giftCertificateRepository.update(existed);
//        adjustDateTimeAccordingToClientTimeZone(existed, TimeZoneConfig.CLIENT_ZONE);
//        return certificateConverter.toDTO(existed);
//    }

    @Override
    @Transactional
    public void deleteCertificate(Long certificateId) {
        giftCertificateRepository.findById(certificateId)
                .ifPresentOrElse(t -> giftCertificateRepository.delete(certificateId), () -> {
                    throw new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND, certificateId));
                });
    }

    @Override
    @Transactional
    public GiftCertificateDto updatePrice(PriceDto priceDto) {
        GiftCertificate existed = giftCertificateRepository
                .findById(priceDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND, priceDto.getId())));
        existed.setPrice(priceDto.getPrice());
        existed.setUpdatedDate(LocalDateTime.now(TimeZoneConfig.DATABASE_ZONE));
        giftCertificateRepository.updatePrice(existed);
        adjustDateTimeAccordingToClientTimeZone(existed, TimeZoneConfig.CLIENT_ZONE);
        return certificateConverter.toDTO(existed);
    }

    private void adjustDateTimeAccordingToClientTimeZone(GiftCertificate certificate, ZoneId toZone) {
        ZoneId repositoryZone = TimeZoneConfig.DATABASE_ZONE;
        certificate.setCreatedDate(DateTimeUtil.toZone(certificate.getCreatedDate(), repositoryZone, toZone));
        certificate.setUpdatedDate(DateTimeUtil.toZone(certificate.getUpdatedDate(), repositoryZone, toZone));
    }
}