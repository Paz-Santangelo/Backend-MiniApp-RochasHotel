package com.PazHotel.BookHotelPaz.utils;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

import com.PazHotel.BookHotelPaz.dto.BookingDTO;
import com.PazHotel.BookHotelPaz.dto.ImageDTO;
import com.PazHotel.BookHotelPaz.dto.RoomDTO;
import com.PazHotel.BookHotelPaz.dto.UserDTO;
import com.PazHotel.BookHotelPaz.model.Booking;
import com.PazHotel.BookHotelPaz.model.Image;
import com.PazHotel.BookHotelPaz.model.Room;
import com.PazHotel.BookHotelPaz.model.User;

public class Utils {

    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateRandomConfirmationCode(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            stringBuilder.append(ALPHANUMERIC_STRING.charAt(randomIndex));
        }
        return stringBuilder.toString();
    }

    public static <T extends ImageDTO> T convertEntityImageToImageDTO(Image image, Class<T> clazz) {
        try {
            T classDto = clazz.getDeclaredConstructor().newInstance();
            classDto.setId(image.getId());
            classDto.setUrlImage(image.getImageUrl());
            classDto.setName(image.getName());
            return classDto;
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir la entidad Imagen a DTO: " + e.getMessage());
        }
    }

    public static RoomDTO convertEntityRoomToRoomDTO(Room room) {
        return new RoomDTO(
                room.getId(),
                room.getRoomType(),
                room.getRoomPrice(),
                room.getRoomDescription(),
                room.getImagesRooms().stream()
                        .map(imageRoom -> convertEntityImageToImageDTO(imageRoom, ImageDTO.class))
                        .toList());
    }

    public static List<RoomDTO> convertRoomListToRoomDTOList(List<Room> rooms) {
        return rooms.stream().map(Utils::convertEntityRoomToRoomDTO).toList();
    }

    public static BookingDTO convertEntityBookingToBookingDTO(Booking booking) {
        return new BookingDTO(
                booking.getId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getAdultsQuantity(),
                booking.getChildrenQuantity(),
                booking.getTotalGuests(),
                booking.getConfirmationCode(),
                null, // User
                null  // Room
        );
    }

    public static BookingDTO convertBookingEntityToBookingDTOWithBookedRooms(Booking booking, boolean mapUser) {
        BookingDTO bookingDTO = convertEntityBookingToBookingDTO(booking);

        if (mapUser) {
            bookingDTO.setUser(Utils.convertUserEntityToUserDTO(booking.getUser()));
        }
        if (booking.getRoom() != null) {
            bookingDTO.setRoom(Utils.convertEntityRoomToRoomDTO(booking.getRoom()));
        }
        return bookingDTO;
    }

    public static List<BookingDTO> convertBookingListToBookingDTOList(List<Booking> bookings) {
        return bookings.stream().map(Utils::convertEntityBookingToBookingDTO).toList();
    }

    public static UserDTO convertUserEntityToUserDTO(User user) {
        return buildUserDTO(user, null, null, false);
    }

    public static UserDTO convertUserEntityToUserDTOWithToken(User user, String token, String tokenExpirationTime) {
        return buildUserDTO(user, token, tokenExpirationTime, false);
    }

    public static UserDTO convertUserEntityToUserDTOWithBookingsAndRoom(User user) {
        return buildUserDTO(user, null, null, true);
    }

    private static UserDTO buildUserDTO(User user, String token, String tokenExpirationTime, boolean includeBookings) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());
        userDTO.setImageUser(
                user.getImageUser() != null ? Utils.convertEntityImageToImageDTO(user.getImageUser(), ImageDTO.class) : null);

        if (token != null) {
            userDTO.setToken(token);
            userDTO.setTokenExpirationTime(tokenExpirationTime);
        }

        if (includeBookings && user.getBookings() != null && !user.getBookings().isEmpty()) {
            userDTO.setBookings(user.getBookings().stream()
                    .map(booking -> convertBookingEntityToBookingDTOWithBookedRooms(booking, false))
                    .collect(Collectors.toList()));
        }

        return userDTO;
    }

    public static List<UserDTO> convertUserEntityListToUserDTOList(List<User> users) {
        return users.stream().map(Utils::convertUserEntityToUserDTO).toList();
    }
}
