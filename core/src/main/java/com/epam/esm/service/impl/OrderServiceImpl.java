package com.epam.esm.service.impl;

import com.epam.esm.config.TimeZoneConfig;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderItem;
import com.epam.esm.exception.ErrorMessage;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.mapper.OrderConverter;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderConverter orderConverter;

    @Override
    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = orderConverter.toEntity(orderDto);
        order.setCreatedDate(LocalDateTime.now(TimeZoneConfig.DATABASE_ZONE));
        BigDecimal cost = order.getOrderItems().stream()
                .map(item -> item.getCertificate().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setCost(cost);
        List<OrderItem> orderItems = order.getOrderItems();
        order.setOrderItems(null);
        orderRepository.save(order);
        order.setOrderItems(orderItems);
        orderRepository.update(order);
        adjustDateTimeAccordingToClientTimeZone(order, TimeZoneConfig.CLIENT_ZONE);
        return orderConverter.toDTO(order);
    }

    @Override
    public Map<Long, OrderDto> getOrdersByUserId(Long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND, userId)));
        List<Order> orders = orderRepository.findOrdersByUserId(userId);
        return orders.stream()
                .map(orderConverter::toDTO)
                .collect(Collectors.toMap(OrderDto::getId, o -> o));
    }

    @Override
    @Transactional
    public OrderDto updateOrder(OrderDto orderDto) {
        Order existed = orderRepository.findById(orderDto.getId()).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND, orderDto.getId())));
        BigDecimal cost = orderDto.getOrderItems().stream()
                .map(item -> item.getCertificate().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        existed.setCost(cost);
        existed.getOrderItems().clear();
        existed.getOrderItems().addAll(orderConverter.toEntities(orderDto.getOrderItems()));
        existed = orderRepository.update(existed);
        return orderConverter.toDTO(existed);
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND, orderId)));
        orderRepository.delete(order);
    }

    private void adjustDateTimeAccordingToClientTimeZone(Order order, ZoneId toZone) {
        ZoneId repositoryZone = TimeZoneConfig.DATABASE_ZONE;
        order.setCreatedDate(DateTimeUtil.toZone(order.getCreatedDate(), repositoryZone, toZone));
    }
}
