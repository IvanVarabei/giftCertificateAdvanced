package com.epam.esm.service.impl;

import com.epam.esm.config.TimeZoneConfig;
import com.epam.esm.dto.CustomPage;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.PriceDto;
import com.epam.esm.dto.SearchCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ErrorMessage;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.mapper.CertificateConverter;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateRepository giftCertificateRepository;
    private final CertificateConverter certificateConverter;

    @Override
    @Transactional
    public GiftCertificateDto createCertificate(GiftCertificateDto giftCertificateDto) {
        GiftCertificate certificate = certificateConverter.toEntity(giftCertificateDto);
        certificate.setId(null);
        LocalDateTime cratedDate = LocalDateTime.now(TimeZoneConfig.DATABASE_ZONE);
        certificate.setCreatedDate(cratedDate);
        certificate.setUpdatedDate(cratedDate);
        Set<Tag> tags = certificate.getTags();
        certificate.setTags(null);
        GiftCertificate savedCertificate = giftCertificateRepository.save(certificate);
        savedCertificate.setTags(tags);
        giftCertificateRepository.update(savedCertificate);
        adjustDateTimeAccordingToClientTimeZone(savedCertificate, TimeZoneConfig.CLIENT_ZONE);
        return certificateConverter.toDTO(savedCertificate);
    }

    @Override
    public CustomPage<GiftCertificateDto> getPaginated(SearchCertificateDto searchDto) {
        int size = searchDto.getPageRequest().getSize();
        int page = searchDto.getPageRequest().getPage();
        long totalCertificateAmount = giftCertificateRepository.countAll(searchDto);
        long lastPage = (totalCertificateAmount + size - 1) / size - 1;
        if (page > lastPage) {
            throw new ResourceNotFoundException(String.format(ErrorMessage.PAGE_NOT_FOUND, size, page, lastPage));
        }
        List<GiftCertificate> certificates = giftCertificateRepository.findPaginated(searchDto);
        return new CustomPage<>(certificates.stream().map(certificateConverter::toDTO).collect(Collectors.toList()),
                searchDto.getPageRequest(), totalCertificateAmount);
    }

    @Override
    public GiftCertificateDto getCertificateById(Long certificateId) {
        GiftCertificate certificate = giftCertificateRepository.findById(certificateId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND, certificateId)));
        return certificateConverter.toDTO(certificate);
    }

    @Override
    @Transactional
    public GiftCertificateDto updateCertificate(GiftCertificateDto updateDto) {
        GiftCertificate existed = giftCertificateRepository
                .findById(updateDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ErrorMessage.RESOURCE_NOT_FOUND, updateDto.getId())));
        GiftCertificate update = certificateConverter.toEntity(updateDto);
        existed.setName(update.getName());
        existed.setPrice(update.getPrice());
        existed.setDescription(update.getDescription());
        existed.setDuration(update.getDuration());
        LocalDateTime updatedDate = LocalDateTime.now(TimeZoneConfig.DATABASE_ZONE);
        existed.setUpdatedDate(updatedDate);
        existed.setTags(update.getTags());
        giftCertificateRepository.update(existed);
        updatedDate = DateTimeUtil.toZone(updatedDate, TimeZoneConfig.DATABASE_ZONE, TimeZoneConfig.CLIENT_ZONE);
        existed.setUpdatedDate(updatedDate);
        return certificateConverter.toDTO(existed);
    }

    @Override
    @Transactional
    public void deleteCertificate(Long certificateId) {
        GiftCertificate certificate = giftCertificateRepository.findById(certificateId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND, certificateId)));
        giftCertificateRepository.delete(certificate);
    }

    @Override
    @Transactional
    public GiftCertificateDto updatePrice(PriceDto priceDto) {
        GiftCertificate existed = giftCertificateRepository
                .findById(priceDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ErrorMessage.RESOURCE_NOT_FOUND, priceDto.getId())));
        existed.setPrice(priceDto.getPrice());
        LocalDateTime updatedDate = LocalDateTime.now(TimeZoneConfig.DATABASE_ZONE);
        existed.setUpdatedDate(updatedDate);
        giftCertificateRepository.updatePrice(existed);
        updatedDate = DateTimeUtil.toZone(updatedDate, TimeZoneConfig.DATABASE_ZONE, TimeZoneConfig.CLIENT_ZONE);
        existed.setUpdatedDate(updatedDate);
        return certificateConverter.toDTO(existed);
    }

    private void adjustDateTimeAccordingToClientTimeZone(GiftCertificate certificate, ZoneId toZone) {
        ZoneId repositoryZone = TimeZoneConfig.DATABASE_ZONE;
        certificate.setCreatedDate(DateTimeUtil.toZone(certificate.getCreatedDate(), repositoryZone, toZone));
        certificate.setUpdatedDate(DateTimeUtil.toZone(certificate.getUpdatedDate(), repositoryZone, toZone));
    }
}