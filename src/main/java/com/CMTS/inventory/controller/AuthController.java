package com.CMTS.inventory.controller;

import com.CMTS.inventory.domain.dto.AuthResponseDto;
import com.CMTS.inventory.domain.dto.LoginRequestDto;
import com.CMTS.inventory.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthController {

    private final AuthenticationService authenticationService;


    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        UserDetails userDetails = authenticationService.authenticate(
                loginRequestDto.email(),
                loginRequestDto.password());

        String tokenValue = authenticationService.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponseDto(tokenValue, 86400));
    }
}
