package com.PazHotel.BookHotelPaz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PazHotel.BookHotelPaz.dto.BookingDTO;
import com.PazHotel.BookHotelPaz.model.Booking;
import com.PazHotel.BookHotelPaz.service.IBookingService;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private IBookingService bookingService;

    @PostMapping("/create/{roomId}/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<?> saveBooking(@PathVariable Long roomId, @PathVariable Long userId,
            @RequestBody Booking booking) {
        bookingService.saveBooking(roomId, userId, booking);
        return ResponseEntity.ok("Reserva realizada con éxito.");
    }

    @GetMapping("/confirmation/{confirmationCode}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode) {
        BookingDTO bookingDTO = bookingService.findBookingByConfirmationCode(confirmationCode);
        return ResponseEntity.ok(bookingDTO);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @DeleteMapping("/cancel/{bookingId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) {
        bookingService.deleteBookingById(bookingId);
        return ResponseEntity.ok("Reserva cancelada con éxito.");
    }
}
