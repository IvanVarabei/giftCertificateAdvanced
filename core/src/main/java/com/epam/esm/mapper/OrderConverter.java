package com.epam.esm.mapper;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderItemDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderItem;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderConverter {
    OrderDto toDTO(Order order);

    Order toEntity(OrderDto orderDto);

    List<OrderItem> toEntities(List<OrderItemDto> orderItemDtoList);
}
