package com.PazHotel.BookHotelPaz.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.PazHotel.BookHotelPaz.dto.LoginDTO;
import com.PazHotel.BookHotelPaz.dto.UserDTO;
import com.PazHotel.BookHotelPaz.model.User;

public interface IUserService {

    public UserDTO register(User user);
    
    public UserDTO login(LoginDTO loginDto);

    public List<UserDTO> getAllUsers();

    public UserDTO getUserWithBookingsHistory(String userId);

    public UserDTO getUserById(Long id);

    public UserDTO getUserByEmail(String email);

    public void deleteUserById(Long id);

    public UserDTO updateUser(Long idUser, MultipartFile image, String name, String phoneNumber, String email, String password);

}
