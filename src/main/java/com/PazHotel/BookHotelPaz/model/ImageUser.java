package com.PazHotel.BookHotelPaz.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "images_users")
public class ImageUser extends Image {

    public ImageUser(String name, String imageUrl, String imageId) {
        super(name, imageUrl, imageId);
    }

}
