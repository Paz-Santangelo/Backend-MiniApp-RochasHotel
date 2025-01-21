package com.PazHotel.BookHotelPaz.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDTO {

    private Long id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int adultsQuantity;
    private int childrenQuantity;
    private int totalGuests;
    private String confirmationCode;
    private UserDTO user;
    private RoomDTO room;

}
