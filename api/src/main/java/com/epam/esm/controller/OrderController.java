package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.ReceiveOrderDto;
import com.epam.esm.dto.ResponseOrderDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.SecurityService;
import com.epam.esm.service.hateoas.HateoasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
     * @param orderDto should be valid according to {@link ResponseOrderDto}. Otherwise, certificate won`t be created.
     *                 Error will be returned(400).
     * @return ResponseEntity witch contains created order with generated id. Response code 201.
     */
    @PostMapping
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<ResponseOrderDto> createOrder(@Valid @RequestBody ReceiveOrderDto orderDto) {
        securityService.validateUserAccess(orderDto.getUserId());
        ResponseOrderDto createdOrderDto = orderService.createOrder(orderDto);
        hateoasService.attachHateoas(createdOrderDto);
        return ResponseEntity.status(CREATED).body(createdOrderDto);
    }

    /**
     * Provides ability to update order entirely.
     *
     * @return updated order wrapped into {@link ResponseOrderDto}. Response code 200.
     */
    @PutMapping("/{orderId}")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<ResponseOrderDto> updateOrder(@Valid @RequestBody ReceiveOrderDto receiveOrderDto,
                                                        @PathVariable("orderId") @Min(1) Long orderId) {
        ResponseOrderDto responseOrderDto = orderService.getOrderById(orderId);
        securityService.validateUserAccess(responseOrderDto.getUser().getId());
        ResponseOrderDto updatedOrderDto = orderService.updateOrder(receiveOrderDto, orderId);
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
    public ResponseEntity<GiftCertificateDto> deleteOrder(@PathVariable("orderId") @Min(1) Long orderId) {
        ResponseOrderDto orderDto = orderService.getOrderById(orderId);
        securityService.validateUserAccess(orderDto.getUser().getId());
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
