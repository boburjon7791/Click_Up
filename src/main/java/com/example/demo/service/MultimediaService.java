package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
public interface MultimediaService {
    String save(MultipartFile file);
    byte[] read(String name);
    void deleteImagesIfExists(List<String> images);

    void existsImage(String profileImage);
}
