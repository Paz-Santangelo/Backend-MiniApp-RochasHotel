package com.PazHotel.BookHotelPaz.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.PazHotel.BookHotelPaz.Exception.CustomException;
import com.PazHotel.BookHotelPaz.Exception.ImageUploadException;
import com.PazHotel.BookHotelPaz.Exception.InvalidInputException;
import com.PazHotel.BookHotelPaz.Exception.NotFoundException;
import com.PazHotel.BookHotelPaz.dto.RoomDTO;
import com.PazHotel.BookHotelPaz.model.ImageRoom;
import com.PazHotel.BookHotelPaz.model.Room;
import com.PazHotel.BookHotelPaz.repository.IRoomRepository;
import com.PazHotel.BookHotelPaz.utils.Utils;

@Service
public class RoomService implements IRoomService {

    @Autowired
    private IRoomRepository roomRepository;

    @Autowired
    private IImageService imageService;

    @Override
    public RoomDTO createRoom(List<MultipartFile> files, String roomType, Double roomPrice, String roomDescription) {

        try {
            if (files == null || files.isEmpty()) {
                throw new InvalidInputException("La lista de archivos no puede estar vacía");
            }
            if (roomType == null || roomType.isBlank()) {
                throw new InvalidInputException("El tipo de habitación es obligatorio");
            }
            if (roomPrice == null) {
                throw new InvalidInputException("El precio de la habitación es obligatorio");
            }
            if (roomDescription == null || roomDescription.isBlank()) {
                throw new InvalidInputException("La descripción de la habitación es obligatoria");
            }

            Room room = new Room();
            room.setRoomType(roomType);
            room.setRoomPrice(roomPrice);
            room.setRoomDescription(roomDescription);
            Room roomSaved = roomRepository.save(room);

            List<ImageRoom> imagesRooms;
            imagesRooms = imageService.uploadImagesRooms(files, roomSaved);

            roomSaved.setImagesRooms(imagesRooms);
            Room roomFinal = roomRepository.save(roomSaved);

            RoomDTO roomDTO = Utils.convertEntityRoomToRoomDTO(roomFinal);

            return roomDTO;

        } catch (InvalidInputException | ImageUploadException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Error inesperado al crear la habitación: ");
        }
    }

    @Override
    public List<RoomDTO> getAllRooms() {
        try {
            List<Room> rooms = roomRepository.findAll();
            List<RoomDTO> roomsDTO = Utils.convertRoomListToRoomDTOList(rooms);

            return roomsDTO;
        } catch (Exception e) {
            throw new CustomException("Error al obtener las habitaciones: ");
        }
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public RoomDTO updateRoom(Long idRoom, List<MultipartFile> files, String roomType, Double roomPrice,
            String roomDescription) {
        try {
            if (idRoom == null) {
                throw new InvalidInputException("El ID de la habitación es obligatorio");
            }
            if (roomType == null || roomType.isBlank()) {
                throw new InvalidInputException("El tipo de habitación es obligatorio");
            }
            if (roomPrice == null) {
                throw new InvalidInputException("El precio de la habitación es obligatorio");
            }
            if (roomDescription == null || roomDescription.isBlank()) {
                throw new InvalidInputException("La descripción de la habitación es obligatoria");
            }

            Room room = roomRepository.findById(idRoom)
                    .orElseThrow(() -> new NotFoundException("Habitación no encontrada"));

            if (files != null && !files.isEmpty()) {
                imageService.deleteImagesRooms(idRoom); // Borra las imágenes existentes
                List<ImageRoom> imagesRooms = imageService.uploadImagesRooms(files, room); // Sube nuevas imágenes
                room.setImagesRooms(imagesRooms);
            }

            room.setRoomType(roomType);
            room.setRoomPrice(roomPrice);
            room.setRoomDescription(roomDescription);

            Room roomUpdated = roomRepository.save(room);

            return Utils.convertEntityRoomToRoomDTO(roomUpdated);

        } catch (NotFoundException | InvalidInputException | ImageUploadException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Error inesperado al modificar la habitación: ");
        }
    }

    @Override
    public RoomDTO getRoomById(Long idRoom) {
        return roomRepository.findById(idRoom)
                .map(Utils::convertEntityRoomToRoomDTO)
                .orElseThrow(() -> new NotFoundException("La habitación con ID " + idRoom + " no fue encontrada."));
    }

    @Override
    public void deleteRoomById(Long idRoom) {
        try {
            Room roomFound = roomRepository.findById(idRoom)
                    .orElseThrow(() -> new NotFoundException("La habitación con ID " + idRoom + " no fue encontrada."));
            imageService.deleteImagesRooms(roomFound.getId());
            roomRepository.deleteById(roomFound.getId());
        } catch (NotFoundException e) {
           throw e;
        } catch (Exception e) {
            throw new CustomException("Error inesperado al eliminar la habitación: ");
        }
    }

    @Override
    public List<RoomDTO> getAllAvailableRooms() {
        try {
            List<Room> rooms = roomRepository.getAllAvailableRooms();
            List<RoomDTO> roomsDTO = Utils.convertRoomListToRoomDTOList(rooms);

            return roomsDTO;
        } catch (Exception e) {
            throw new CustomException("Error al obtener las habitaciones disponibles: ");
        }
    }

    @Override
    public List<RoomDTO> getAllAvailableRoomsByDateAndTypes(LocalDate checkInDate, LocalDate checkOutDate,
            String roomType) {
        try {
            if (checkInDate == null || checkOutDate == null || roomType == null || roomType.isBlank()) {
                throw new InvalidInputException("Se requieren todos los datos: fecha de entrada, fecha de salida y tipo de habitación");
            }
            
            List<Room> rooms = roomRepository.findAvailableRoomsByDateAndTypes(checkInDate, checkOutDate, roomType);

            if (rooms.isEmpty()) {
                throw new NotFoundException("No se encontraron habitaciones disponibles del tipo " + roomType
                        + " para las fechas seleccionadas.");
            }

            List<RoomDTO> roomsDTO = Utils.convertRoomListToRoomDTOList(rooms);

            return roomsDTO;
        } catch (InvalidInputException |NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Error al obtener las habitaciones disponibles: ");
        }
    }

}
