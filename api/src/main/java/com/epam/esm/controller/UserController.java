package com.epam.esm.controller;

import com.epam.esm.dto.UserDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;
    private final OrderService orderService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("userId") @Min(1) Long userId) {
        UserDto userDto = userService.getUserById(userId);
        Link selfLink = linkTo(UserController.class).slash(userId).withSelfRel();
        userDto.add(selfLink);
        if (orderService.getOrdersByUserId(userId).size() > 0) {
            Link ordersLink = linkTo(methodOn(OrderController.class)
                    .getOrdersByUserId(userId)).withRel("allOrders");
            userDto.add(ordersLink);
        }
        return ResponseEntity.ok().body(userDto);
    }
}
