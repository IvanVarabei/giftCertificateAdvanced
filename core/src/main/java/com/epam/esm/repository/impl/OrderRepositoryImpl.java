package com.epam.esm.repository.impl;

import com.epam.esm.config.TimeZoneConfig;
import com.epam.esm.entity.GiftCertificateAsOrderItem;
import com.epam.esm.entity.Order;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
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
import java.time.ZonedDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private static final String CREATE_ORDER = "insert into purchase (consumer_id, placed_date) values (?, ?)";
    private static final String CREATE_CERTIFICATE_AS_ORDER_ITEM =
            "insert into gift_certificate_as_purchase_item (name, description, price, duration, purchase_id) " +
                    "values (?, ?, ?, ?, ?);";
    private static final String READ_ORDER_BY_USER_ID =
            "select id, placed_date from purchase where consumer_id = ?";
    private static final String READ_CERTIFICATE_AS_ORDER_ITEM_BY_ORDER_ID =
            "select id, name, description, price, duration from gift_certificate_as_purchase_item where purchase_id =?";
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<GiftCertificateAsOrderItem> certificateAsOrderItemMapper;

    @Override
    public Order createOrder(Order order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime placedDate = DateTimeUtil.toZone(order.getPlacedDate(), TimeZoneConfig.DATABASE_ZONE,
                ZoneId.systemDefault());
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(CREATE_ORDER, Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            preparedStatement.setLong(index++, order.getUserId());
            preparedStatement.setTimestamp(index, Timestamp.valueOf(placedDate));
            return preparedStatement;
        }, keyHolder);
        Long orderId = ((Number) keyHolder.getKeys().get("id")).longValue();
        order.setId(orderId);
        order.getOrderItems().forEach(certificate -> saveGiftCertificateAsOrderItem(orderId, certificate));
        return order;
    }

    @Override
    public List<Order> findOrdersByUserId(Long userId) {
        return jdbcTemplate.query(READ_ORDER_BY_USER_ID, (rs, rowNum) -> {
            Order order = new Order();
            order.setUserId(userId);
            order.setId(rs.getLong("id"));
            order.setPlacedDate(ZonedDateTime.ofInstant(rs.getTimestamp("placed_date").toInstant(),
                    TimeZoneConfig.DATABASE_ZONE).toLocalDateTime());
            order.setOrderItems(jdbcTemplate.query(READ_CERTIFICATE_AS_ORDER_ITEM_BY_ORDER_ID,
                    certificateAsOrderItemMapper, order.getId()));
            return order;
        }, userId);
    }

    private void saveGiftCertificateAsOrderItem(Long orderId, GiftCertificateAsOrderItem certificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(CREATE_CERTIFICATE_AS_ORDER_ITEM, Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            preparedStatement.setString(index++, certificate.getName());
            preparedStatement.setString(index++, certificate.getDescription());
            preparedStatement.setBigDecimal(index++, certificate.getPrice());
            preparedStatement.setInt(index++, certificate.getDuration());
            preparedStatement.setLong(index, orderId);
            return preparedStatement;
        }, keyHolder);
        certificate.setId(((Number) keyHolder.getKeys().get("id")).longValue());
    }
}
