package com.PazHotel.BookHotelPaz.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomType;
    private Double roomPrice;
    private String roomDescription;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ImageRoom> imagesRooms = new ArrayList<>();

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Booking> bookings;

    public void addImageRoom(ImageRoom imageRoom) {
        imagesRooms.add(imageRoom);
        imageRoom.setRoom(this);
    }

    public void removeImageRoom(ImageRoom imageRoom) {
        imagesRooms.remove(imageRoom);
        imageRoom.setRoom(null);
    }
}
