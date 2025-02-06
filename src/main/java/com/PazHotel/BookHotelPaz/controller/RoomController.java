package com.PazHotel.BookHotelPaz.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.PazHotel.BookHotelPaz.dto.RoomDTO;
import com.PazHotel.BookHotelPaz.service.IRoomService;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private IRoomService roomService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createRoom(
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            @RequestParam(value = "roomType", required = false) String roomType,
            @RequestParam(value = "roomPrice", required = false) Double roomPrice,
            @RequestParam(value = "roomDescription", required = false) String roomDescription) {

        roomService.createRoom(files, roomType, roomPrice, roomDescription);
        return ResponseEntity.ok().body("Habitación creada con éxito");
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllRooms() {
        List<RoomDTO> rooms = roomService.getAllRooms();
        return ResponseEntity.ok().body(rooms);
    }

    @GetMapping("/types")
    public List<String> getAllRoomsTypes() {
        return roomService.getAllRoomTypes();
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?> getRoomById(@PathVariable Long roomId) {
        RoomDTO roomDTO = roomService.getRoomById(roomId);
        return ResponseEntity.ok().body(roomDTO);
    }

    @PutMapping("/update/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateRoom(@PathVariable Long roomId,
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            @RequestParam(value = "roomType", required = false) String roomType,
            @RequestParam(value = "roomPrice", required = false) Double roomPrice,
            @RequestParam(value = "roomDescription", required = false) String roomDescription) {

        RoomDTO roomDTO = roomService.updateRoom(roomId, files, roomType, roomPrice, roomDescription);
        return ResponseEntity.ok().body(roomDTO);

    }

    @DeleteMapping("/delete/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoomById(roomId);
        return ResponseEntity.status(HttpStatus.OK).body("Habitación eliminada con éxito.");
    }

    @GetMapping("/all/availables")
    public ResponseEntity<?> getAllAvailableRooms() {
        List<RoomDTO> availableRooms = roomService.getAllAvailableRooms();
        return ResponseEntity.ok().body(availableRooms);
    }

    @GetMapping("/availables/datetypes")
    public ResponseEntity<?> getAllAvailableRoomsByDateAndTypes(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam(required = false) String roomType) {

        List<RoomDTO> availableRoomsByDateAndTypes = roomService.getAllAvailableRoomsByDateAndTypes(checkInDate,
                checkOutDate, roomType);
        return ResponseEntity.ok().body(availableRoomsByDateAndTypes);

    }
}
