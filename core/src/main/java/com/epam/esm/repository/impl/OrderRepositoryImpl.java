package com.epam.esm.repository.impl;

import com.epam.esm.config.TimeZoneConfig;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderItem;
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
    private final JdbcTemplate jdbcTemplate;
    private static final String CREATE_ORDER = "insert into \"order\" (user_id, created_date) values (?, ?)";
    private static final String CREATE_CERTIFICATE_AS_ORDER_ITEM = "insert into order_item " +
            "(name, description, price, duration, order_id, quantity) values (?, ?, ?, ?, ?, ?)";
    private static final String READ_ORDER_BY_USER_ID = "select id, created_date from \"order\" where user_id = ?";
    private static final String READ_CERTIFICATE_AS_ORDER_ITEM_BY_ORDER_ID =
            "select id, name, description, price, duration, quantity from order_item where order_id =?";
    private final RowMapper<OrderItem> certificateAsOrderItemMapper;

    @Override
    public Order createOrder(Order order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime cratedDate;
        cratedDate = DateTimeUtil.toZone(order.getCreatedDate(), TimeZoneConfig.DATABASE_ZONE, ZoneId.systemDefault());
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(CREATE_ORDER, Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            preparedStatement.setLong(index++, order.getUser().getId());
            preparedStatement.setTimestamp(index, Timestamp.valueOf(cratedDate));
            return preparedStatement;
        }, keyHolder);
        Long orderId = ((Number) keyHolder.getKeys().get("id")).longValue();
        order.setId(orderId);
        order.getOrderItems().forEach(certificate -> saveOrderItem(orderId, certificate));
        return order;
    }

    @Override
    public List<Order> findOrdersByUserId(Long userId) {
        return jdbcTemplate.query(READ_ORDER_BY_USER_ID, (rs, rowNum) -> {
            Order order = new Order();
            order.setId(rs.getLong("id"));
            order.setCreatedDate(ZonedDateTime.ofInstant(rs.getTimestamp("created_date").toInstant(),
                    TimeZoneConfig.DATABASE_ZONE).toLocalDateTime());
            return order;
        }, userId);
    }

    @Override
    public List<OrderItem> findOrderItemsByOrderId(Long orderId) {
        return jdbcTemplate.query(READ_CERTIFICATE_AS_ORDER_ITEM_BY_ORDER_ID, certificateAsOrderItemMapper, orderId);
    }

    private void saveOrderItem(Long orderId, OrderItem orderItem) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(CREATE_CERTIFICATE_AS_ORDER_ITEM, Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            preparedStatement.setString(index++, orderItem.getName());
            preparedStatement.setString(index++, orderItem.getDescription());
            preparedStatement.setBigDecimal(index++, orderItem.getPrice());
            preparedStatement.setInt(index++, orderItem.getDuration());
            preparedStatement.setLong(index++, orderId);
            preparedStatement.setInt(index, orderItem.getQuantity());
            return preparedStatement;
        }, keyHolder);
        orderItem.setId(((Number) keyHolder.getKeys().get("id")).longValue());
    }
}
