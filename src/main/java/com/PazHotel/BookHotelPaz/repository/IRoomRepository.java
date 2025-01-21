package com.PazHotel.BookHotelPaz.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.PazHotel.BookHotelPaz.model.Room;

@Repository
public interface IRoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT DISTINCT room.roomType FROM Room room")
    List<String> findDistinctRoomTypes();

    @Query("SELECT r FROM Room r WHERE r.id NOT IN (SELECT b.room.id FROM Booking b)")
    List<Room> getAllAvailableRooms();

    @Query("SELECT r FROM Room r WHERE r.roomType LIKE %:roomType% AND r.id NOT IN (SELECT bk.room.id FROM Booking bk WHERE"
            +
            "(bk.checkInDate <= :checkOutDate) AND (bk.checkOutDate >= :checkInDate))")
    List<Room> findAvailableRoomsByDateAndTypes(LocalDate checkInDate, LocalDate checkOutDate, String roomType);

}
