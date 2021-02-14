package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.SecurityService;
import com.epam.esm.service.hateoas.HateoasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {
    private final OrderService orderService;
    private final HateoasService hateoasService;
    private final SecurityService securityService;

    /**
     * The method allows {@link com.epam.esm.entity.Order} creating.
     *
     * @param orderDto should be valid according to {@link OrderDto}. Otherwise, certificate won`t be created. Error
     *                 will be returned(400).
     * @return ResponseEntity witch contains created order with generated id. Response code 201.
     */
    @PostMapping
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderDto orderDto, Authentication authentication) {
        securityService.ifUserIdNotMatchingThrowException(authentication, orderDto.getUser().getId());
        OrderDto createdOrderDto = orderService.createOrder(orderDto);
        hateoasService.attachHateoas(createdOrderDto);
        return ResponseEntity.status(CREATED).body(createdOrderDto);
    }

    /**
     * Provides ability to update order entirely.
     *
     * @param orderDto should have all fields filled and valid. Otherwise error will be returned(400).
     * @return updated order wrapped into {@link OrderDto}. Response code 200.
     */
    @PutMapping
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<OrderDto> updateOrder(@Valid @RequestBody OrderDto orderDto, Authentication authentication) {
        securityService.ifUserIdNotMatchingThrowException(authentication, orderDto.getUser().getId());
        OrderDto updatedOrderDto = orderService.updateOrder(orderDto);
        hateoasService.attachHateoas(updatedOrderDto);
        return ResponseEntity.ok().body(updatedOrderDto);
    }

    /**
     * Allows order deleting. orderId should be passed.
     *
     * @param orderId should be positive integer number.
     * @return responseEntity having empty body. Response code 204.
     */
    @DeleteMapping("/{orderId}")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<GiftCertificateDto> deleteOrder(@PathVariable("orderId") @Min(1) Long orderId,
                                                          Authentication authentication) {
        OrderDto orderDto = orderService.getOrderById(orderId);
        securityService.ifUserIdNotMatchingThrowException(authentication, orderDto.getUser().getId());
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
