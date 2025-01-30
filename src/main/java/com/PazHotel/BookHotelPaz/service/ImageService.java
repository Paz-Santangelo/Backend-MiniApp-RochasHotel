package com.PazHotel.BookHotelPaz.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.PazHotel.BookHotelPaz.model.Image;
import com.PazHotel.BookHotelPaz.model.ImageRoom;
import com.PazHotel.BookHotelPaz.model.ImageUser;
import com.PazHotel.BookHotelPaz.model.Room;
import com.PazHotel.BookHotelPaz.repository.IImageRoomRepository;
import com.PazHotel.BookHotelPaz.repository.IImageUserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ImageService implements IImageService {

    @Autowired
    private ICloudinaryService cloudinaryService;

    @Autowired
    private IImageUserRepository imageUserRepository;

    @Autowired
    private IImageRoomRepository imageRoomRepository;

    @Override
    public ImageUser uploadImageUser(MultipartFile file) throws IOException {
        @SuppressWarnings("unchecked")
        Map<String, Object> uploadResult = cloudinaryService.upload(file);
        String imageUrl = (String) uploadResult.get("url");
        String imageId = (String) uploadResult.get("public_id");

        ImageUser imageUser = new ImageUser(file.getOriginalFilename(), imageUrl, imageId);

        return imageUserRepository.save(imageUser);
    }

    @Override
    public void deleteImageUser(ImageUser imageUser) throws IOException {
        deleteImageCloudinaryAndRepository(imageUser, imageUserRepository);
    }

    @Override
    public List<ImageRoom> uploadImagesRooms(List<MultipartFile> files, Room room) throws IOException {
        return files.stream()
                .map(file -> {
                    try {

                        @SuppressWarnings("unchecked")
                        Map<String, Object> uploadResult = cloudinaryService.upload(file);
                        String imageUrl = (String) uploadResult.get("url");
                        String imageId = (String) uploadResult.get("public_id");

                        ImageRoom imageRoom = new ImageRoom(file.getOriginalFilename(), imageUrl, imageId, room);
                        return imageRoomRepository.save(imageRoom);

                    } catch (Exception e) {
                        throw new RuntimeException("Error al subir la imagen " + file.getOriginalFilename(), e);
                    }
                }).collect(Collectors.toList());
    }

    @Override
    public void deleteImagesRooms(Long roomId) {
        List<ImageRoom> imagesRooms = this.getImagesRoomsByIdRoom(roomId);

        imagesRooms.forEach(image -> {
            try {
                deleteImageCloudinaryAndRepository(image, imageRoomRepository);
            } catch (IOException e) {
                throw new RuntimeException("Error al eliminar la imagen con ID: " + image.getId(), e.getCause());
            }
        });
    }

    public <T extends Image> void deleteImageCloudinaryAndRepository(T image, JpaRepository<T, Long> repository)
            throws IOException {
        cloudinaryService.delete(image.getImageId());
        repository.delete(image);
    }

    public List<ImageRoom> getImagesRoomsByIdRoom(Long roomId) {
        List<ImageRoom> imagesRooms = imageRoomRepository.findByRoom_Id(roomId);

        if (imagesRooms.isEmpty()) {
            throw new EntityNotFoundException("No se encontraron imágenes para la habitación con ID: " + roomId);
        }

        return imagesRooms;
    }

}