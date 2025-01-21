package com.PazHotel.BookHotelPaz.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private Long id;
    
    private String email;
    
    private String name;
    
    private String phoneNumber;

    private String token;

    private String tokenExpirationTime;
    
    private String role;

    private ImageDTO imageUser;
    
    private List<BookingDTO> bookings = new ArrayList<>();

}
