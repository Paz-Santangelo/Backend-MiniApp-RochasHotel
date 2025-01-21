package com.PazHotel.BookHotelPaz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.PazHotel.BookHotelPaz.Exception.CustomException;
import com.PazHotel.BookHotelPaz.Exception.InvalidInputException;
import com.PazHotel.BookHotelPaz.Exception.NotFoundException;
import com.PazHotel.BookHotelPaz.dto.BookingDTO;
import com.PazHotel.BookHotelPaz.model.Booking;
import com.PazHotel.BookHotelPaz.model.Room;
import com.PazHotel.BookHotelPaz.model.User;
import com.PazHotel.BookHotelPaz.repository.IBookingRepository;
import com.PazHotel.BookHotelPaz.repository.IRoomRepository;
import com.PazHotel.BookHotelPaz.repository.IUserRepository;
import com.PazHotel.BookHotelPaz.utils.Utils;

@Service
public class BookingService implements IBookingService {

    @Autowired
    private IBookingRepository bookingRepository;

    @Autowired
    private IRoomRepository roomRepository;

    @Autowired
    private IUserRepository userRepository;

    @Override
    public BookingDTO saveBooking(Long roomId, Long userId, Booking booking) {

        try {
            if (booking.getCheckInDate() == null || booking.getCheckOutDate() == null) {
                throw new InvalidInputException("Se requieren todos los datos: fecha de entrada y fecha de salida.");
            }

            if (booking.getAdultsQuantity() == 0) {
                throw new InvalidInputException("Se requiere la cantidad de adultos.");
            }

            if (booking.getCheckOutDate().isBefore(booking.getCheckInDate())) {
                throw new InvalidInputException("La fecha de salida debe ser posterior a la fecha de entrada.");
            }

            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new NotFoundException("Habitación no encontrada."));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("Usuario no encontrado."));

            List<Booking> existingBookings = room.getBookings();

            if (!roomIsAvailable(booking, existingBookings)) {
                throw new InvalidInputException("La habitación no está disponible en las fechas seleccionadas.");
            }

            String confirmationCode = Utils.generateRandomConfirmationCode(10);
            booking.setRoom(room);
            booking.setUser(user);
            booking.setConfirmationCode(confirmationCode);
            bookingRepository.save(booking);

            return Utils.convertBookingEntityToBookingDTOWithBookedRooms(booking, false);
        } catch (InvalidInputException | NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Error inesperado al guardar la reserva. ");
        }

    }

    @Override
    public BookingDTO findBookingByConfirmationCode(String confirmationCode) {
        return bookingRepository.findByConfirmationCode(confirmationCode)
                .map(booking -> Utils.convertBookingEntityToBookingDTOWithBookedRooms(booking, true)) 
                .orElseThrow(() -> new NotFoundException(
                        "La reserva con el código " + confirmationCode + " no fue encontrada."));
    }

    @Override
    public List<BookingDTO> getAllBookings() {
        try {
            List<Booking> bookings = bookingRepository.findAll();
            List<BookingDTO> bookingsDTO = Utils.convertBookingListToBookingDTOList(bookings);

            return bookingsDTO;
        } catch (Exception e) {
            throw new CustomException("Error inesperado al obtener las reservas: ");
        }
    }

    @Override
    public void deleteBookingById(Long bookingId) {
        try {
            bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Reserva no encontrada."));
            bookingRepository.deleteById(bookingId);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Error inesperado al eliminar la reserva: ");
        }
    }

    private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking -> bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                        || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                        || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                        || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                        || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                        || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                        || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate())));
    }

}
