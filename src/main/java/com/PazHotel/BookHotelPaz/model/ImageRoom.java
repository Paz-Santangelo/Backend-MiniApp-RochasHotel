package com.PazHotel.BookHotelPaz.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "images_rooms")
public class ImageRoom extends Image{

    @ManyToOne
    @JoinColumn(name= "room_id")
    private Room room;

    public ImageRoom(String name, String imageUrl, String imageId, Room room) {
        super(name, imageUrl, imageId);
        this.room = room;
    }
    
}
