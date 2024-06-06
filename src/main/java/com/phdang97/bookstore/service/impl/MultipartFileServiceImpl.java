package com.phdang97.bookstore.service.impl;

import com.phdang97.bookstore.enums.ImageType;
import com.phdang97.bookstore.exception.ImageException;
import com.phdang97.bookstore.service.MultipartFileService;
import jakarta.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MultipartFileServiceImpl implements MultipartFileService {
  private static final String UPLOAD_ROOT = "images/";
  @Autowired private ResourceLoader resourceLoader;

  private static void validate(MultipartFile file) {
    if (file == null) {
      throw new ImageException("No image file was uploaded.");
    }

    String contentType = file.getContentType();
    String[] allowedExtensions = {"image/jpeg", "image/jpg", "image/png"};

    if (!Arrays.asList(allowedExtensions).contains(contentType)) {
      throw new ImageException(
          String.format(
              "Invalid image format. Only %s formats are allowed.",
              String.join(", ", allowedExtensions)));
    }
  }

  public String uploadImage(MultipartFile file, ImageType type) {
    validate(file);
    String imageName = generateImageName(file.getOriginalFilename());
    String imagePath =
        switch (type) {
          case COVER -> "cover/" + imageName;
          case THUMBNAIL -> "thumbnail/" + imageName;
        };
    //    Resource resource = resourceLoader.getResource("/images/");
    //    try (InputStream inputStream = file.getInputStream()) {
    //      String fullPath = resource.getFile().getAbsolutePath();
    //      Path path = Paths.get(fullPath + "/" + imagePath);
    //      Files.createDirectories(path.getParent());
    //      System.out.println("path::" + path + path.getParent());
    //      Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
    //    } catch (IOException e) {
    //      throw new ImageException(String.format("Failed to upload image name: %s", imageName));
    //    }
    try {
      Path uploadPath = Path.of(UPLOAD_ROOT).resolve(imagePath);
      Files.createDirectories(uploadPath.getParent());
      Files.copy(file.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
      return imagePath;
    } catch (IOException e) {
      throw new ImageException(String.format("Failed to upload image name: %s", imageName));
    }
  }

  public byte[] downloadImage(String imagePath) {
    try {
      Resource resource = resourceLoader.getResource("classpath:/images/");
      String fullPath = resource.getFile().getAbsolutePath();
      String path = fullPath + "/" + imagePath;
      return Files.readAllBytes(new File(path).toPath());
    } catch (IOException e) {
      throw new EntityNotFoundException(e);
    }
  }

  public MediaType getContentType(String imagePath) {
    String extension = imagePath.substring(imagePath.lastIndexOf('.') + 1).toLowerCase();
    return switch (extension) {
      case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
      case "png" -> MediaType.IMAGE_PNG;
      default -> MediaType.APPLICATION_OCTET_STREAM;
    };
  }

  public void deleteImage(String imagePath) {
//    Resource resource = resourceLoader.getResource("classpath:/images/");
    try {
//      String fullPath = resource.getFile().getAbsolutePath();
//      Path path = Paths.get(fullPath + "/" + imagePath);
//      System.out.println("path" + path);
//      Files.delete(path);
      Path path = Path.of(UPLOAD_ROOT + imagePath);
      Files.deleteIfExists(path);
    } catch (IOException e) {
      throw new EntityNotFoundException(e);
    }
  }

  private String generateImageName(String fileName) {
    String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    String randomUUID = UUID.randomUUID().toString();
    String extension = getExtension(fileName);

    return timestamp + "_" + randomUUID + "." + extension;
  }

  private String getExtension(String filename) {
    return filename.substring(filename.lastIndexOf(".") + 1);
  }
}
