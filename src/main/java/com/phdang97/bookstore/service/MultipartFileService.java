package com.phdang97.bookstore.service;

import com.phdang97.bookstore.enums.ImageType;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

public interface MultipartFileService {
    String uploadImage(MultipartFile file, ImageType type);

    byte[] downloadImage(String imagePath);

    MediaType getContentType(String imagePath);

    void deleteImage(String imagePath);
}
