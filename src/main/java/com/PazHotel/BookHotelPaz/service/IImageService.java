package com.PazHotel.BookHotelPaz.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.PazHotel.BookHotelPaz.model.ImageRoom;
import com.PazHotel.BookHotelPaz.model.ImageUser;
import com.PazHotel.BookHotelPaz.model.Room;

public interface IImageService {

    public ImageUser uploadImageUser(MultipartFile file) throws IOException;



    public void deleteImageUser(ImageUser imageUser) throws IOException;

    public List<ImageRoom> uploadImagesRooms(List<MultipartFile> files, Room room) throws IOException; 

    public void deleteImagesRooms(Long roomId);

}
