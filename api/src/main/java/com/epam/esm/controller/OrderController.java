package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.hateoas.HateoasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    /**
     * The method allows {@link com.epam.esm.entity.Order} creating.
     *
     * @param orderDto should be valid according to {@link OrderDto}. Otherwise, certificate won`t be created. Error
     *                 will be returned(400).
     * @return ResponseEntity witch contains created order with generated id. Response code 201.
     */
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderDto orderDto,
                                                @RequestParam @Min(1) Long userId) {
        orderDto.setId(userId);
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
    public ResponseEntity<OrderDto> updateOrder(@Valid @RequestBody OrderDto orderDto,
                                                @RequestParam @Min(1) Long userId) {
        orderDto.setId(userId);
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
    public ResponseEntity<GiftCertificateDto> deleteOrder(@PathVariable("orderId") @Min(1) Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
