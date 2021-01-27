package com.epam.esm.mapper;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderConverter {
    OrderDto toDTO(Order order);

    Order toEntity(OrderDto orderDto);
}
