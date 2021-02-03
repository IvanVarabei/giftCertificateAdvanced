package com.epam.esm.mapper;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-02-02T20:39:13+0300",
    comments = "version: 1.4.1.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-6.7.1.jar, environment: Java 14.0.1 (Oracle Corporation)"
)
@Component
public class CertificateConverterImpl implements CertificateConverter {

    @Override
    public GiftCertificateDto toDTO(GiftCertificate certificate) {
        if ( certificate == null ) {
            return null;
        }

        GiftCertificateDto giftCertificateDto = new GiftCertificateDto();

        giftCertificateDto.setId( certificate.getId() );
        giftCertificateDto.setName( certificate.getName() );
        giftCertificateDto.setDescription( certificate.getDescription() );
        giftCertificateDto.setPrice( certificate.getPrice() );
        giftCertificateDto.setDuration( certificate.getDuration() );
        giftCertificateDto.setCreatedDate( certificate.getCreatedDate() );
        giftCertificateDto.setUpdatedDate( certificate.getUpdatedDate() );
        giftCertificateDto.setTags( tagSetToTagDtoList( certificate.getTags() ) );

        return giftCertificateDto;
    }

    @Override
    public GiftCertificate toEntity(GiftCertificateDto giftCertificateDto) {
        if ( giftCertificateDto == null ) {
            return null;
        }

        GiftCertificate giftCertificate = new GiftCertificate();

        giftCertificate.setId( giftCertificateDto.getId() );
        giftCertificate.setName( giftCertificateDto.getName() );
        giftCertificate.setDescription( giftCertificateDto.getDescription() );
        giftCertificate.setPrice( giftCertificateDto.getPrice() );
        giftCertificate.setDuration( giftCertificateDto.getDuration() );
        giftCertificate.setCreatedDate( giftCertificateDto.getCreatedDate() );
        giftCertificate.setUpdatedDate( giftCertificateDto.getUpdatedDate() );
        giftCertificate.setTags( tagDtoListToTagSet( giftCertificateDto.getTags() ) );

        return giftCertificate;
    }

    @Override
    public List<Tag> toEntities(List<TagDto> tagsDto) {
        if ( tagsDto == null ) {
            return null;
        }

        List<Tag> list = new ArrayList<Tag>( tagsDto.size() );
        for ( TagDto tagDto : tagsDto ) {
            list.add( tagDtoToTag( tagDto ) );
        }

        return list;
    }

    @Override
    public List<TagDto> toDTOs(List<Tag> tags) {
        if ( tags == null ) {
            return null;
        }

        List<TagDto> list = new ArrayList<TagDto>( tags.size() );
        for ( Tag tag : tags ) {
            list.add( tagToTagDto( tag ) );
        }

        return list;
    }

    protected TagDto tagToTagDto(Tag tag) {
        if ( tag == null ) {
            return null;
        }

        TagDto tagDto = new TagDto();

        tagDto.setId( tag.getId() );
        tagDto.setName( tag.getName() );

        return tagDto;
    }

    protected List<TagDto> tagSetToTagDtoList(Set<Tag> set) {
        if ( set == null ) {
            return null;
        }

        List<TagDto> list = new ArrayList<TagDto>( set.size() );
        for ( Tag tag : set ) {
            list.add( tagToTagDto( tag ) );
        }

        return list;
    }

    protected Tag tagDtoToTag(TagDto tagDto) {
        if ( tagDto == null ) {
            return null;
        }

        Tag tag = new Tag();

        tag.setId( tagDto.getId() );
        tag.setName( tagDto.getName() );

        return tag;
    }

    protected Set<Tag> tagDtoListToTagSet(List<TagDto> list) {
        if ( list == null ) {
            return null;
        }

        Set<Tag> set = new HashSet<Tag>( Math.max( (int) ( list.size() / .75f ) + 1, 16 ) );
        for ( TagDto tagDto : list ) {
            set.add( tagDtoToTag( tagDto ) );
        }

        return set;
    }
}
