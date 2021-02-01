package com.epam.esm.repository.impl;

import com.epam.esm.config.TimeZoneConfig;
import com.epam.esm.dto.SearchCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.mapper.CertificateMapper;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.dto.search.SortOrder.DESC;

@Repository
@RequiredArgsConstructor
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {
    private final JdbcTemplate jdbcTemplate;
    private final CertificateMapper certificateMapper;

    private static final String CREATE_CERTIFICATE =
            "insert into gift_certificate (name, description, price, duration, create_date, last_update_date) " +
                    "values (?, ?, ?, ?, ?, ?)";

    private static final String READ_CERTIFICATES_BASE =
            "select id, name, description, price, duration, create_date, last_update_date from gift_certificate " +
                    "where true ";

    private static final String READ_CERTIFICATES_TAGS1 = "and id in (SELECT gift_certificate_id FROM " +
            "certificate_tag LEFT JOIN tag ON tag_id = tag.id WHERE tag.name IN (";

    private static final String READ_CERTIFICATES_TAGS2 = ") " +
            "GROUP BY gift_certificate_id HAVING COUNT(tag_id) = ?)";

    private static final String NAME_FILTER = "and name ilike '%";

    private static final String DESCRIPTION_FILTER = "and description ilike '%";

    private static final String SORT_FIELD = "order by ";

    private static final String PAGINATION = "%s offset %s limit %s";

    private static final String READ_CERTIFICATE_BY_ID = "select id, name, description, price, duration, " +
            "create_date, last_update_date from gift_certificate where id = ?";

    private static final String UPDATE_CERTIFICATE = "update gift_certificate set name = ?, description = ?, " +
            "price = ?, duration = ?, last_update_date = ? where id = ?";

    private static final String UPDATE_PRICE = "update gift_certificate set price = ?, last_update_date = ? " +
            "where id = ?";

    private static final String DELETE_CERTIFICATE = "delete from gift_certificate where id = ?";

    private static final String COUNT_CERTIFICATES_BASE = "select count(id) from gift_certificate where true ";

    private static final String BLANK = " ";

    private static final String[] RETURN_GENERATED_KEY = {"id"};

    @Override
    public GiftCertificate save(GiftCertificate giftCertificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime createdDate = DateTimeUtil
                .toZone(giftCertificate.getCreatedDate(), TimeZoneConfig.DATABASE_ZONE, ZoneId.systemDefault());
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_CERTIFICATE, RETURN_GENERATED_KEY);
            int index = 1;
            preparedStatement.setString(index++, giftCertificate.getName());
            preparedStatement.setString(index++, giftCertificate.getDescription());
            preparedStatement.setBigDecimal(index++, giftCertificate.getPrice());
            preparedStatement.setInt(index++, giftCertificate.getDuration());
            preparedStatement.setTimestamp(index++, Timestamp.valueOf(createdDate));
            preparedStatement.setTimestamp(index, Timestamp.valueOf(createdDate));
            return preparedStatement;
        }, keyHolder);
        giftCertificate.setId(keyHolder.getKey().longValue());
        return giftCertificate;
    }

    /**
     * select id, name, description, price, duration, create_date, last_update_date
     * from gift_certificate
     * where true
     * and id in (
     * SELECT gift_certificate_id
     * FROM certificate_tag
     * LEFT JOIN tag ON tag_id = tag.id
     * WHERE tag.name IN ('tag_name_1', 'tag_name_2')
     * GROUP BY gift_certificate_id
     * HAVING COUNT(tag_id) = 2)
     * and name ilike '%name_fragment%'
     * and description ilike '%description_fragment%'
     * order by last_update_date
     * desc
     * offset %s limit %s
     */
    @Override
    public List<GiftCertificate> findPaginated(SearchCertificateDto searchDto) {
        List queryParams = new ArrayList<>();
        StringBuilder sb = generateSearchQuery(searchDto, READ_CERTIFICATES_BASE, queryParams);
        if (searchDto.getSortByField() != null) {
            sb.append(SORT_FIELD)
                    .append(searchDto.getSortByField())
                    .append(BLANK);
            if (DESC == searchDto.getSortOrder()) {
                sb.append(DESC);
            }
        }
        int size = searchDto.getPageRequest().getSize();
        int page = searchDto.getPageRequest().getPage();
        int offset = size * page;
        String query = String.format(PAGINATION, sb.toString(), offset, size);
        return jdbcTemplate.query(query, certificateMapper, queryParams.toArray());
    }

    /**
     * select count(id) from gift_certificate where true
     * and id in (
     * SELECT gift_certificate_id
     * FROM certificate_tag
     * LEFT JOIN tag ON tag_id = tag.id
     * WHERE tag.name IN ('tag_name_1', 'tag_name_2')
     * GROUP BY gift_certificate_id
     * HAVING COUNT(tag_id) = 2)
     * and name ilike '%name_fragment%'
     * and description ilike '%description_fragment%'
     */
    @Override
    public Integer countAll(SearchCertificateDto searchDto) {
        List queryParams = new ArrayList<>();
        StringBuilder sb = generateSearchQuery(searchDto, COUNT_CERTIFICATES_BASE, queryParams);
        return jdbcTemplate.query(sb.toString(), (rs, rowNum) -> rs.getInt("count"), queryParams.toArray())
                .stream().findAny().get();
    }

    @Override
    public Optional<GiftCertificate> findById(Long certificateId) {
        return jdbcTemplate.query(READ_CERTIFICATE_BY_ID, certificateMapper, certificateId).stream().findAny();
    }

    @Override
    public void update(GiftCertificate giftCertificate) {
        LocalDateTime updatedDate = DateTimeUtil
                .toZone(giftCertificate.getUpdatedDate(), TimeZoneConfig.DATABASE_ZONE, ZoneId.systemDefault());
        jdbcTemplate.update(UPDATE_CERTIFICATE, giftCertificate.getName(), giftCertificate.getDescription(),
                giftCertificate.getPrice(), giftCertificate.getDuration(), updatedDate, giftCertificate.getId());
    }

    @Override
    public void updatePrice(GiftCertificate giftCertificate) {
        LocalDateTime updatedDate = DateTimeUtil
                .toZone(giftCertificate.getUpdatedDate(), TimeZoneConfig.DATABASE_ZONE, ZoneId.systemDefault());
        jdbcTemplate.update(UPDATE_PRICE, giftCertificate.getPrice(), updatedDate, giftCertificate.getId());
    }

    @Override
    public void delete(Long giftCertificateId) {
        jdbcTemplate.update(DELETE_CERTIFICATE, giftCertificateId);
    }

    /**
     * Generates a query regarding passed params (searchDto and and tableColumns).
     *
     * @param tableColumns + following query fragment:
     *                     and id in (
     *                     SELECT gift_certificate_id
     *                     FROM certificate_tag
     *                     LEFT JOIN tag ON tag_id = tag.id
     *                     WHERE tag.name IN ('tag_name_1', 'tag_name_2')
     *                     GROUP BY gift_certificate_id
     *                     HAVING COUNT(tag_id) = 2)
     *                     and name ilike '%name_fragment%'
     *                     and description ilike '%description_fragment%'
     */
    private StringBuilder generateSearchQuery(SearchCertificateDto searchDto, String tableColumns, List queryParams) {
        StringBuilder sb = new StringBuilder(tableColumns);
        List<String> tags = searchDto.getTagNames();
        if (tags != null && !tags.isEmpty()) {
            sb.append(READ_CERTIFICATES_TAGS1)
                    .append("?, ".repeat(tags.size() - 1))
                    .append("?")
                    .append(READ_CERTIFICATES_TAGS2);
            queryParams.addAll(tags);
            queryParams.add(tags.size());
        }
        if (!StringUtils.isBlank(searchDto.getName())) {
            sb.append(NAME_FILTER)
                    .append(searchDto.getName())
                    .append("%' ");
        }
        if (!StringUtils.isBlank(searchDto.getDescription())) {
            sb.append(DESCRIPTION_FILTER)
                    .append(searchDto.getDescription())
                    .append("%' ");
        }
        return sb;
    }
}
