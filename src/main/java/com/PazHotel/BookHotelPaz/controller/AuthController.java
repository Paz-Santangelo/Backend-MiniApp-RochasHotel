package com.PazHotel.BookHotelPaz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PazHotel.BookHotelPaz.dto.LoginDTO;
import com.PazHotel.BookHotelPaz.dto.UserDTO;
import com.PazHotel.BookHotelPaz.model.User;
import com.PazHotel.BookHotelPaz.service.IUserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        userService.register(user);
        return ResponseEntity.ok("Usuario registrado con éxito");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDto) {
        UserDTO userDTO = userService.login(loginDto);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

}
