package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.mapper.OrderConverter;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrderServiceImplTest {
    OrderRepository orderRepository;
    UserRepository userRepository;
    OrderService orderService;
    Order order;
    @Autowired
    OrderConverter orderConverter;

    {
        order = new Order();
        order.setId(1L);
        order.setCost(BigDecimal.ONE);
        order.setOrderItems(new ArrayList<>());
    }

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        userRepository = mock(UserRepository.class);
        orderService = new OrderServiceImpl(orderRepository, userRepository, orderConverter);
    }

    @Test
    void should_invoke_orderRepository_update_when_createOrder() {
        OrderDto orderDto = new OrderDto();
        when(orderRepository.save(any())).thenReturn(order);

        orderService.createOrder(orderDto);

        verify(orderRepository).update(any(Order.class));
    }

    @Test
    void should_return_map_of_orders_when_getOrdersByUserId() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        Order order = new Order();
        order.setId(1L);
        when(orderRepository.findOrdersByUserId(1L)).thenReturn(List.of(order));
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setOrderItems(null);
        Map<Long, OrderDto> expected = Map.of(1L, orderDto);

        Map<Long, OrderDto> actual = orderService.getOrdersByUserId(1L);

        assertEquals(expected, actual);
    }

    @Test
    void should_throw_ResourceNotFoundException_when_user_not_found_when_getOrdersByUserId() {
        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrdersByUserId(1L));
    }

    @Test
    void should_invoke_orderRepository_update_when_updateOrder() {
        OrderDto updateOrderDto = new OrderDto();
        updateOrderDto.setId(order.getId());
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        orderService.updateOrder(updateOrderDto);

        verify(orderRepository).update(any());
    }

    @Test
    void should_throw_ResourceNotFoundException_when_order_not_found_when_update() {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);

        assertThrows(ResourceNotFoundException.class, () -> orderService.updateOrder(orderDto));
    }

    @Test
    void should_invoke_orderRepository_delete_when_deleteOrder() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        orderService.deleteOrder(order.getId());

        verify(orderRepository).delete(order);
    }

    @Test
    void should_throw_ResourceNotFoundException_when_order_not_found_when_delete() {
        assertThrows(ResourceNotFoundException.class, () -> orderService.deleteOrder(1L));
    }
}
