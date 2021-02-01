package com.epam.esm.mapper;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Component
public class CertificateMapper implements RowMapper<GiftCertificate> {
    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(rs.getLong("id"));
        giftCertificate.setName(rs.getString("name"));
        giftCertificate.setDescription(rs.getString("description"));
        giftCertificate.setPrice(rs.getBigDecimal("price"));
        giftCertificate.setDuration(rs.getInt("duration"));
        giftCertificate.setCreatedDate(ZonedDateTime.ofInstant(
                rs.getTimestamp("create_date").toInstant(), ZoneOffset.UTC).toLocalDateTime());
        giftCertificate.setUpdatedDate(ZonedDateTime.ofInstant(
                rs.getTimestamp("last_update_date").toInstant(), ZoneOffset.UTC).toLocalDateTime());
       // giftCertificate.setTags(new ArrayList<>());
        return giftCertificate;
    }
}