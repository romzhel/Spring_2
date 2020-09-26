package ru.romzhel.eshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.romzhel.eshop.config.jwt.JwtRequest;
import ru.romzhel.eshop.config.jwt.JwtResponse;
import ru.romzhel.eshop.entities.User;
import ru.romzhel.eshop.providers.JwtProvider;
import ru.romzhel.eshop.services.UserService;

@RestController
@RequestMapping("/")
public class JwtAuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/auth")
    public ResponseEntity<?> auth(@RequestBody JwtRequest request) {
        User user = userService.findByUserName(request.getUsername());
        String token = jwtProvider.generateToken(user.getUserName());
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
