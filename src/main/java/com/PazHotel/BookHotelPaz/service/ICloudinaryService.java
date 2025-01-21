package com.PazHotel.BookHotelPaz.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface ICloudinaryService {

    @SuppressWarnings("rawtypes")
    Map upload(MultipartFile multipartFile) throws IOException;

    @SuppressWarnings("rawtypes")
    Map delete(String id_image) throws IOException;

}
