package com.epam.esm.controller;

import com.epam.esm.dto.UserDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
    private final PagedResourcesAssembler<UserDto> pagedResourcesAssembler;

    @GetMapping
    public PagedModel<EntityModel<UserDto>> getUsers(@PageableDefault Pageable pageRequest) {
        Page<UserDto> users = userService.getPaginated(pageRequest);
        for (UserDto userDto : users.getContent()) {
            Link userDtoSelfLink = linkTo(methodOn(UserController.class)
                    .getUserById(userDto.getId())).withSelfRel();
            userDto.add(userDtoSelfLink);
            if (orderService.getOrdersByUserId(userDto.getId()).size() > 0) {
                Link ordersLink = linkTo(methodOn(OrderController.class)
                        .getOrdersByUserId(userDto.getId())).withRel("allOrders");
                userDto.add(ordersLink);
            }
        }
        Link selfLink = Link.of(ServletUriComponentsBuilder.fromCurrentRequest().build().toString());
        return pagedResourcesAssembler.toModel(users, selfLink);
    }

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
