package com.epam.esm.service.impl;

import com.epam.esm.config.TimeZoneConfig;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.PlaceOrderDto;
import com.epam.esm.entity.GiftCertificateAsOrderItem;
import com.epam.esm.entity.Order;
import com.epam.esm.exception.ErrorMessage;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.mapper.CertificateConverter;
import com.epam.esm.mapper.OrderConverter;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final GiftCertificateService certificateService;
    private final OrderConverter orderConverter;
    private final CertificateConverter certificateConverter;

    @Override
    @Transactional
    public OrderDto placeOrder(PlaceOrderDto placeOrderDto) {
        Long userId = placeOrderDto.getUserId();
        userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND, userId)));
        Order order = new Order();
        order.setUserId(userId);
        order.setPlacedDate(LocalDateTime.now(TimeZoneConfig.DATABASE_ZONE));
        order.setOrderItems(placeOrderDto.getItemEntries().keySet().stream()
                .map(o -> {
                    GiftCertificateDto giftCertificateDto = certificateService.getCertificateById(o);
                    GiftCertificateAsOrderItem orderItem = certificateConverter.dtoToOrderItem(giftCertificateDto);
                    orderItem.setAmount(placeOrderDto.getItemEntries().get(o));
                    return orderItem;
                })
                .collect(Collectors.toList()));
        orderRepository.createOrder(order);
        return orderConverter.toDTO(adjustDateTimeAccordingToClientTimeZone(order, TimeZoneConfig.CLIENT_ZONE));
    }

    @Override
    public List<OrderDto> getOrdersByUserId(Long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND, userId)));
        return orderRepository.findOrdersByUserId(userId).stream()
                .map(order -> adjustDateTimeAccordingToClientTimeZone(order, TimeZoneConfig.CLIENT_ZONE))
                .map(orderConverter::toDTO)
                .collect(Collectors.toList());
    }

    private Order adjustDateTimeAccordingToClientTimeZone(Order order, ZoneId toZone) {
        ZoneId repositoryZone = TimeZoneConfig.DATABASE_ZONE;
        order.setPlacedDate(DateTimeUtil.toZone(order.getPlacedDate(), repositoryZone, toZone));
        return order;
    }
}
