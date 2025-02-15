package com.PazHotel.BookHotelPaz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.PazHotel.BookHotelPaz.Exception.CustomException;
import com.PazHotel.BookHotelPaz.Exception.InvalidInputException;
import com.PazHotel.BookHotelPaz.Exception.MyAuthenticationException;
import com.PazHotel.BookHotelPaz.Exception.NotFoundException;
import com.PazHotel.BookHotelPaz.dto.LoginDTO;
import com.PazHotel.BookHotelPaz.dto.UserDTO;
import com.PazHotel.BookHotelPaz.model.ImageUser;
import com.PazHotel.BookHotelPaz.model.User;
import com.PazHotel.BookHotelPaz.repository.IUserRepository;
import com.PazHotel.BookHotelPaz.utils.JWTUtils;
import com.PazHotel.BookHotelPaz.utils.Utils;
import com.cloudinary.api.exceptions.NotFound;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IImageService imageService;

    @Override
    public UserDTO register(User user) {
        try {

            if (user.getName() == null || user.getName().isBlank()) {
                throw new InvalidInputException("El nombre es obligatorio.");
            }

            if (user.getPassword() == null || user.getPassword().isBlank()) {
                throw new InvalidInputException("La contraseña es obligatoria.");
            }

            if (user.getEmail() == null || user.getEmail().isBlank()) {
                throw new InvalidInputException("El correo es obligatorio.");
            }

            if (userRepository.existsByEmail(user.getEmail())) {
                throw new InvalidInputException("El usuario con el correo" + user.getEmail() + " ya existe.");
            }

            if (user.getRole() == null || user.getRole().isBlank()) {
                user.setRole("USER");
            }

            // Asignar imagen por defecto si no se proporciona
            if (user.getImageUser() == null) {
                ImageUser defaultImage = new ImageUser(
                        "default-user.jpg",
                        "https://as1.ftcdn.net/jpg/00/64/67/52/1000_F_64675209_7ve2XQANuzuHjMZXP3aIYIpsDKEbF5dD.jpg",
                        "default_image_id");
                user.setImageUser(defaultImage);
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User userSaved = userRepository.save(user);
            UserDTO userDTO = Utils.convertUserEntityToUserDTO(userSaved);

            return userDTO;
        } catch (InvalidInputException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Error inesperado durante el registro:" + e.getMessage());
        }
    }

    @Override
    public UserDTO login(LoginDTO loginDto) {
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            User user = userRepository.findByEmail(loginDto.getEmail())
                    .orElseThrow(() -> new NotFoundException("Usuario no encontrado. Verifica el email ingresado."));

            String token = jwtUtils.generateToken(user);

            return Utils.convertUserEntityToUserDTOWithToken(user, token, "7 Days");
        } catch (InvalidInputException e) {
            throw e;
        } catch (AuthenticationException e) {
            throw new MyAuthenticationException(
                    "Error: Las credenciales ingresadas son incorrectas. Por favor verifique su email o contraseña.");
        } catch (Exception e) {
            throw new CustomException("Error inesperado durante el inicio de sesión." + e.getMessage());
        }
    }

    @Override
    public List<UserDTO> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            List<UserDTO> usersDTO = Utils.convertUserEntityListToUserDTOList(users);

            return usersDTO;
        } catch (Exception e) {
            throw new CustomException("Error al obtener los usuarios: " + e.getMessage());
        }
    }

    @Override
    public UserDTO getUserWithBookingsHistory(String idUser) {
        try {
            Long userId = Long.parseLong(idUser);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new Exception("Usuario no encontrado."));
            UserDTO userDto = Utils.convertUserEntityToUserDTOWithBookingsAndRoom(user);
            return userDto;
        } catch (Exception e) {
            throw new CustomException(
                    "Error inesperado al obtener el historial de reservas del usuario: " + e.getMessage());
        }
    }

    @Override
    public UserDTO getUserById(Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Usuario no encontrado."));
            UserDTO userDto = Utils.convertUserEntityToUserDTO(user);
            return userDto;
        } catch (Exception e) {
            throw new CustomException("Error inesperado al obtener el usuario: " + e.getMessage());
        }
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException("Usuario no encontrado con el email: " + email));
            UserDTO userDto = Utils.convertUserEntityToUserDTO(user);
            return userDto;
        } catch (Exception e) {
            throw new CustomException("Error inesperado al obtener el usuario: " + e.getMessage());
        }
    }

    @Override
    public void deleteUserById(Long id) {
        try {
            User userFound = userRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Usuario no encontrado."));
            userRepository.deleteById(userFound.getId());
            imageService.deleteImageUser(userFound.getImageUser());
        } catch (Exception e) {
            throw new CustomException("Error inesperado al eliminar el usuario: " + e.getMessage());
        }
    }

    @Override
    public UserDTO updateUser(Long idUser, MultipartFile image, String name, String phoneNumber, String email,
            String password) {

        try {
            User userFound = userRepository.findById(idUser).orElseThrow(() -> new NotFoundException("Usuario no encontrado."));

            if (image != null) {
                ImageUser imageFound = userFound.getImageUser();
                imageService.deleteImageUser(imageFound);
            }

            ImageUser newImageUser = imageService.uploadImageUser(image);
            userFound.setImageUser(newImageUser);

            if (name != null && !name.isBlank())
                userFound.setName(name);
            if (phoneNumber != null && !phoneNumber.isBlank())
                userFound.setPhoneNumber(phoneNumber);
            if (email != null && !email.isBlank())
                userFound.setEmail(email);
            if (password != null && !password.isBlank())
                userFound.setPassword(passwordEncoder.encode(password));

            User userSaved = userRepository.save(userFound);
            UserDTO userDTO = Utils.convertUserEntityToUserDTO(userSaved);
            return userDTO;

        } catch (Exception e) {
            throw new CustomException("Error inesperado al actualizar el usuario: " + e.getMessage());
        }

    }

}