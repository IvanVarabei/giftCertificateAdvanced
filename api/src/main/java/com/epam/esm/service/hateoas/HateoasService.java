package com.epam.esm.service.hateoas;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class HateoasService {
    private final OrderService orderService;

    public void attachHateoas(TagDto tagDto) {
        Link selfLink = linkTo(methodOn(TagController.class)
                .getTagById(tagDto.getId())).withSelfRel();
        Link certificates = linkTo(methodOn(CertificateController.class)
                .getCertificates(List.of(tagDto.getName()),
                        null, null, null,
                        null, null, null, null))
                .withRel("certificates");
        tagDto.add(selfLink);
        tagDto.add(certificates);
    }

    public void attachHateoas(GiftCertificateDto certificateDto) {
        certificateDto.add(linkTo(methodOn(CertificateController.class)
                .getCertificateById(certificateDto.getId())).withSelfRel());
        certificateDto.getTags().forEach(this::attachHateoas);
    }

    public void attachHateoas(UserDto userDto) {
        Long userId = userDto.getId();
        Link selfLink = linkTo(UserController.class).slash(userId).withSelfRel();
        userDto.add(selfLink);
        if (orderService.getOrdersByUserId(userId).size() > 0) {
            Link ordersLink = linkTo(methodOn(UserController.class)
                    .getOrdersByUserId(userId, null)).withRel("allOrders");
            userDto.add(ordersLink);
        }
    }

    public void attachHateoas(OrderDto orderDto) {
        attachHateoas(orderDto.getUser());
        orderDto.getOrderItems().forEach(orderItemDto -> attachHateoas(orderItemDto.getCertificate()));
    }
}
