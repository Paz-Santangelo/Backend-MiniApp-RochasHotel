package com.PazHotel.BookHotelPaz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.PazHotel.BookHotelPaz.dto.UserDTO;
import com.PazHotel.BookHotelPaz.service.IUserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/bookings/{userId}")
    public ResponseEntity<?> getUserBookingsHistory(@PathVariable String userId) {
        UserDTO userDto = userService.getUserWithBookingsHistory(userId);
        return ResponseEntity.ok().body(userDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        UserDTO userDto = userService.getUserById(id);
        return ResponseEntity.ok().body(userDto);
    }

    @GetMapping("/logged")
    public ResponseEntity<?> getUserByEmail() {
        Authentication authenticaiton = SecurityContextHolder.getContext().getAuthentication();
        String email = authenticaiton.getName();
        UserDTO userDto = userService.getUserByEmail(email);
        return ResponseEntity.ok().body(userDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("El perfil fue eliminado con éxito.");
    }

    @PutMapping("/update/{idUser}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<?> updateUser(@PathVariable Long idUser,
            @RequestParam(value = "image", required = false) MultipartFile imageUser,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "email", required = true) String email,
            @RequestParam(value = "password", required = true) String password) {
        userService.updateUser(idUser, imageUser, name, phoneNumber, email, password);
        return ResponseEntity.ok().body(
                "Usuario modificado con éxito. Deberá volver a iniciar sesión para poder continuar usando el sistema.");
    }
}
