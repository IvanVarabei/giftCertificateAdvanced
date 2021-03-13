package com.epam.esm.service.impl;

import com.epam.esm.config.TimeZoneConfig;
import com.epam.esm.dto.ReceiveOrderDto;
import com.epam.esm.dto.ResponseOrderDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderItem;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ErrorMessage;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.mapper.OrderConverter;
import com.epam.esm.repository.GiftCertificateRepository;
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
    private final GiftCertificateRepository certificateRepository;
    private final OrderConverter orderConverter;

    @Override
    @Transactional
    public ResponseOrderDto createOrder(ReceiveOrderDto orderDto) {
        User user = userRepository.findById(orderDto.getUserId()).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND, orderDto.getUserId())));
        Order order = new Order();
        order.setUser(user);
        order.setOrderItems(getOrderItems(orderDto));
        order.setCreatedDate(LocalDateTime.now(TimeZoneConfig.DATABASE_ZONE));
        BigDecimal cost = order.getOrderItems().stream()
                .map(item -> item.getCertificate().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setCost(cost);
        orderRepository.save(order);
        adjustDateTimeAccordingToClientTimeZone(order, TimeZoneConfig.CLIENT_ZONE);
        return orderConverter.toDTO(order);
    }

    @Override
    public Map<Long, ResponseOrderDto> getOrdersByUserId(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND, userId));
        }
        List<Order> orders = orderRepository.findOrdersByUserId(userId);
        return orders.stream()
                .map(orderConverter::toDTO)
                .collect(Collectors.toMap(ResponseOrderDto::getId, o -> o));
    }

    @Override
    public ResponseOrderDto getOrderById(Long orderId) {
        return orderConverter.toDTO(orderRepository.findById(orderId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND, orderId))));
    }

    @Override
    @Transactional
    public ResponseOrderDto updateOrder(ReceiveOrderDto orderDto, Long orderId) {
        Order existed = orderRepository.findById(orderId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND, orderId)));
        List<OrderItem> orderItems = getOrderItems(orderDto);
        BigDecimal cost = orderItems.stream()
                .map(item -> item.getCertificate().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        existed.setCost(cost);
        existed.getOrderItems().clear();
        existed.getOrderItems().addAll(orderItems);
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

    private List<OrderItem> getOrderItems(ReceiveOrderDto orderDto) {
        return orderDto.getOrderItems().keySet().stream()
                .map(id -> certificateRepository.findById(id).orElseThrow(() ->
                        new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND, id))))
                .map(c -> new OrderItem(c, orderDto.getOrderItems().get(c.getId())))
                .collect(Collectors.toList());
    }

    private void adjustDateTimeAccordingToClientTimeZone(Order order, ZoneId toZone) {
        ZoneId repositoryZone = TimeZoneConfig.DATABASE_ZONE;
        order.setCreatedDate(DateTimeUtil.toZone(order.getCreatedDate(), repositoryZone, toZone));
    }
}
