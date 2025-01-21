package com.PazHotel.BookHotelPaz.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.PazHotel.BookHotelPaz.dto.RoomDTO;

public interface IRoomService {

    public RoomDTO createRoom(List<MultipartFile> files, String roomType, Double roomPrice, String roomDescription);

    public List<RoomDTO> getAllRooms();

    public List<String> getAllRoomTypes();

    public void deleteRoomById(Long id);

    public RoomDTO updateRoom(Long idRoom, List<MultipartFile> files, String roomType, Double roomPrice, String roomDescription);

    public RoomDTO getRoomById(Long idRoom);

    public List<RoomDTO> getAllAvailableRooms();

    public List<RoomDTO> getAllAvailableRoomsByDateAndTypes(LocalDate checkInDate, LocalDate checkOutDate, String roomType);

}
