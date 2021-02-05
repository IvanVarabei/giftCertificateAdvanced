package com.epam.esm.controller;

import com.epam.esm.dto.CustomPage;
import com.epam.esm.dto.CustomPageable;
import com.epam.esm.dto.UserDto;
import com.epam.esm.service.UserService;
import com.epam.esm.service.hateoas.HateoasService;
import com.epam.esm.service.hateoas.PaginationHateoas;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;
    private final PaginationHateoas<UserDto> paginationHateoas;
    private final HateoasService hateoasService;

    /**
     * The method provides all existing users paginated.
     *
     * @param pageRequest created automatically from uri params (page, size).
     * @param uriBuilder  is necessary for creating hateoas pagination.
     * @param request     is necessary for creating hateoas pagination.
     * @return Response entity containing page object. Response code 200.
     */
    @GetMapping
    public CustomPage<UserDto> getUsers(@Valid CustomPageable pageRequest, UriComponentsBuilder uriBuilder,
                                        HttpServletRequest request) {
        CustomPage<UserDto> users = userService.getPaginated(pageRequest);
        users.getContent().forEach(hateoasService::attachHateoas);
        uriBuilder.path(request.getRequestURI());
        uriBuilder.query(request.getQueryString());
        paginationHateoas.addPaginationLinks(uriBuilder, users);
        return users;
    }

    /**
     * The method provide a user having passed id. If it's absent error will be returned(404).
     *
     * @param userId should be positive integer number.
     * @return TagDto. Response code 200.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("userId") @Min(1) Long userId) {
        UserDto userDto = userService.getUserById(userId);
        hateoasService.attachHateoas(userDto);
        return ResponseEntity.ok().body(userDto);
    }
}
