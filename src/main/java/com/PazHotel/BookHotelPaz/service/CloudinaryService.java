package com.PazHotel.BookHotelPaz.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService implements ICloudinaryService{

    private final Cloudinary cloudinary;

    public CloudinaryService() {
        Map<String, String> valuesMap = new HashMap<>();
        valuesMap.put("cloud_name", "dnw0mx8qr");
        valuesMap.put("api_key", "587472376832978");
        valuesMap.put("api_secret", "ohiWJ_i5w3Jwa0BlkY-1nHOxsGc");
        cloudinary = new Cloudinary(valuesMap);
    }

    private File convert(MultipartFile multipartFile) throws IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(multipartFile.getBytes());
        fileOutputStream.close();
        return file;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Map upload(MultipartFile multipartFile) throws IOException {
        File file = convert(multipartFile);
        Map result = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        if(!Files.deleteIfExists(file.toPath())) {
            throw new IOException("No se pudo eliminar el archivo temporal" + file.getAbsolutePath());
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Map delete(String id_image) throws IOException {
        return cloudinary.uploader().destroy(id_image, ObjectUtils.emptyMap());
    }


}
