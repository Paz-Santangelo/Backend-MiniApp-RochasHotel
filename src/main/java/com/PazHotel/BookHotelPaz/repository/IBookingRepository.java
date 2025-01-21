package com.PazHotel.BookHotelPaz.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PazHotel.BookHotelPaz.model.Booking;

@Repository
public interface IBookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByConfirmationCode(String confirmationCode);

}
