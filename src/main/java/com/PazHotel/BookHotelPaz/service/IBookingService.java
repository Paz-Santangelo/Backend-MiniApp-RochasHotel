package com.PazHotel.BookHotelPaz.service;

import java.util.List;

import com.PazHotel.BookHotelPaz.dto.BookingDTO;
import com.PazHotel.BookHotelPaz.model.Booking;

public interface IBookingService {

    public BookingDTO saveBooking(Long roomId, Long userId, Booking booking);

    public BookingDTO findBookingByConfirmationCode(String confirmationCode);

    public List<BookingDTO> getAllBookings();

    public void deleteBookingById(Long bookingId);

}
