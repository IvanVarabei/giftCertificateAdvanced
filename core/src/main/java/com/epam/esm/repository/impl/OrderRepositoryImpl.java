package com.epam.esm.repository.impl;

import com.epam.esm.config.TimeZoneConfig;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderItem;
import com.epam.esm.entity.User;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<OrderItem> orderItemMapper;
    private static final String[] RETURN_GENERATED_KEY = {"id"};

    private static final String CREATE_ORDER = "insert into \"order\" (cost, created_date, user_id) values (?, ?, ?)";

    private static final String CREATE_ORDER_ITEM = "insert into order_item " +
            "(quantity, order_id, gift_certificate_id) values (?, ?, ?)";

    private static final String READ_ORDER_BY_ID =
            "select \"user\".id, username, password, email, cost, created_date from \"order\" " +
                    "join \"user\" on \"user\".id = \"order\".user_id and \"order\".id = ?";

    private static final String READ_ORDER_BY_USER_ID =
            "select id, cost, created_date from \"order\" where user_id = ?";

    private static final String READ_ORDER_ITEM_BY_ORDER_ID = "select order_item.id, " +
            "order_item.gift_certificate_id as certificateId, name, description, price, duration, quantity " +
            "from order_item join gift_certificate on order_item.gift_certificate_id = gift_certificate.id " +
            "where order_id =?";

    private static final String UPDATE_COST = "update \"order\" set cost = ? where id = ?";

    private static final String DELETE_ORDER = "delete from \"order\" where id = ?";

    private static final String DELETE_ORDER_ITEMS = "delete from order_item where order_id = ?";
    private final RowMapper<User> userMapper;

    @Override
    public Order save(Order order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime cratedDate;
        cratedDate = DateTimeUtil.toZone(order.getCreatedDate(), TimeZoneConfig.DATABASE_ZONE, ZoneId.systemDefault());
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_ORDER, RETURN_GENERATED_KEY);
            int index = 1;
            preparedStatement.setBigDecimal(index++, order.getCost());
            preparedStatement.setTimestamp(index++, Timestamp.valueOf(cratedDate));
            preparedStatement.setLong(index, order.getUser().getId());
            return preparedStatement;
        }, keyHolder);
        Long orderId = keyHolder.getKey().longValue();
        order.setId(orderId);
        order.getOrderItems().forEach(item -> saveOrderItem(orderId, item));
        return order;
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return jdbcTemplate.query(READ_ORDER_BY_ID, (rs, rowNum) -> {
            Order order = new Order();
            order.setId(orderId);
            order.setCost(rs.getBigDecimal("cost"));
            order.setCreatedDate(ZonedDateTime.ofInstant(rs.getTimestamp("created_date").toInstant(),
                    TimeZoneConfig.DATABASE_ZONE).toLocalDateTime());
            order.setUser(userMapper.mapRow(rs, rowNum));
            return order;
        }, orderId).stream().findAny();
    }

    @Override
    public List<Order> findOrdersByUserId(Long userId) {
        return jdbcTemplate.query(READ_ORDER_BY_USER_ID, (rs, rowNum) -> {
            Order order = new Order();
            order.setId(rs.getLong("id"));
            order.setCost(rs.getBigDecimal("cost"));
            order.setCreatedDate(ZonedDateTime.ofInstant(rs.getTimestamp("created_date").toInstant(),
                    TimeZoneConfig.DATABASE_ZONE).toLocalDateTime());
            return order;
        }, userId);
    }

    @Override
    public List<OrderItem> findOrderItemsByOrderId(Long orderId) {
        return jdbcTemplate.query(READ_ORDER_ITEM_BY_ORDER_ID, orderItemMapper, orderId);
    }

    @Override
    public void update(Order order) {
        jdbcTemplate.update(UPDATE_COST, order.getCost(), order.getId());
        order.getOrderItems().forEach(item -> saveOrderItem(order.getId(), item));
    }

    @Override
    public void delete(Long orderId) {
        jdbcTemplate.update(DELETE_ORDER, orderId);
    }

    @Override
    public void deleteAllOrderItems(Long orderId) {
        jdbcTemplate.update(DELETE_ORDER_ITEMS, orderId);
    }

    private void saveOrderItem(Long orderId, OrderItem orderItem) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(CREATE_ORDER_ITEM, RETURN_GENERATED_KEY);
            int index = 1;
            preparedStatement.setInt(index++, orderItem.getQuantity());
            preparedStatement.setLong(index++, orderId);
            preparedStatement.setLong(index, orderItem.getCertificateId());
            return preparedStatement;
        }, keyHolder);
        orderItem.setId(keyHolder.getKey().longValue());
    }
}
