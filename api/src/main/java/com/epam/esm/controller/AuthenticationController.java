package com.epam.esm.controller;

import com.epam.esm.dto.AuthenticationRequestDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Role;
import com.epam.esm.security.JwtTokenProvider;
import com.epam.esm.security.JwtUser;
import com.epam.esm.service.UserService;
import com.epam.esm.service.hateoas.HateoasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * REST controller for authentication requests (login, register)
 */
@RestController
@RequestMapping(value = "/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final HateoasService hateoasService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(
            @RequestBody @Valid AuthenticationRequestDto authenticationRequestDto) {
        UserDto createdUser = userService.register(authenticationRequestDto);
        String token = jwtTokenProvider.createToken(createdUser.getEmail(), createdUser.getRole());
        hateoasService.attachHateoas(createdUser);
        Map<String, Object> response = Map.of("user", createdUser, "token", token);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthenticationRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        String role = jwtUser.getAuthorities().stream().findAny().get().toString();
        String token = jwtTokenProvider.createToken(email, Role.valueOf(role));
        return ResponseEntity.ok(Map.of("email", email, "token", token));
    }
}
