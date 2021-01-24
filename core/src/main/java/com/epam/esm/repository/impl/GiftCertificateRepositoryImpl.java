package com.epam.esm.repository.impl;

import com.epam.esm.dto.SearchCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.dto.search.SortOrder.DESC;

@Repository
@RequiredArgsConstructor
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {
    private static final ZoneId defaultZone = ZoneOffset.UTC;
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<GiftCertificate> certificateMapper;

    private static final String CREATE_CERTIFICATE =
            "insert into gift_certificate (name, description, price, duration, create_date, last_update_date) " +
                    "values (?, ?, ?, ?, ?, ?);";

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

    private static final String READ_CERTIFICATE_BY_ID = "select id, name, description, price, duration, " +
            "create_date, last_update_date from gift_certificate where id = ?";

    private static final String UPDATE_CERTIFICATE = "update gift_certificate set name = ?, description = ?, " +
            "price = ?, duration = ?, last_update_date = ? where id = ?";

    private static final String DELETE_CERTIFICATE = "delete from gift_certificate where id = ?";

    private static final String BLANK = " ";

    @Override
    public GiftCertificate save(GiftCertificate giftCertificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime createdDate = DateTimeUtil
                .toZone(giftCertificate.getCreatedDate(), defaultZone, ZoneId.systemDefault());
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_CERTIFICATE, Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            preparedStatement.setString(index++, giftCertificate.getName());
            preparedStatement.setString(index++, giftCertificate.getDescription());
            preparedStatement.setBigDecimal(index++, giftCertificate.getPrice());
            preparedStatement.setInt(index++, giftCertificate.getDuration());
            preparedStatement.setTimestamp(index++, Timestamp.valueOf(createdDate));
            preparedStatement.setTimestamp(index, Timestamp.valueOf(createdDate));
            return preparedStatement;
        }, keyHolder);
        giftCertificate.setId(((Number) keyHolder.getKeys().get("id")).longValue());
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
     * WHERE tag.name IN ('cheap', 'gym')
     * GROUP BY gift_certificate_id
     * HAVING COUNT(tag_id) = 2)
     * and name ilike '%e%'
     * and description ilike '%fr%'
     * order by last_update_date
     * desc
     */
    @Override
    public List<GiftCertificate> findAll(SearchCertificateDto searchDto) {
        StringBuilder sb = new StringBuilder(READ_CERTIFICATES_BASE);
        List<String> tags = searchDto.getTagNames();
        List queryParams = new ArrayList<>();
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
        if (searchDto.getSortByField() != null) {
            sb.append(SORT_FIELD)
                    .append(searchDto.getSortByField())
                    .append(BLANK);
            if (DESC == searchDto.getSortOrder()) {
                sb.append(DESC);
            }
        }
        return jdbcTemplate.query(sb.toString(), certificateMapper, queryParams.toArray());
    }

    @Override
    public Optional<GiftCertificate> findById(Long certificateId) {
        return jdbcTemplate.query(READ_CERTIFICATE_BY_ID, certificateMapper, certificateId).stream().findAny();
    }

    @Override
    public void update(GiftCertificate giftCertificate) {
        LocalDateTime updatedDate = DateTimeUtil
                .toZone(giftCertificate.getUpdatedDate(), defaultZone, ZoneId.systemDefault());
        jdbcTemplate.update(UPDATE_CERTIFICATE, giftCertificate.getName(), giftCertificate.getDescription(),
                giftCertificate.getPrice(), giftCertificate.getDuration(), updatedDate, giftCertificate.getId());
    }

    @Override
    public void delete(Long giftCertificateId) {
        jdbcTemplate.update(DELETE_CERTIFICATE, giftCertificateId);
    }

    @Override
    public ZoneId getDatabaseZoneId() {
        return defaultZone;
    }
}
