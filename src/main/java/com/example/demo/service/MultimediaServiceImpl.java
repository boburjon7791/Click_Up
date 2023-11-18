package com.example.demo.service;

import com.example.demo.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MultimediaServiceImpl implements MultimediaService {
    private static final Path root = Path.of(System.getProperty("user.home")+"/Desktop/click");
    static {
        if (!root.toFile().exists()) {
            System.out.println(root.toFile().mkdirs());
        }
    }
    @Override
    public String save(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String fileName =  (UUID.randomUUID() + "." + extension);
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            int type = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
            BufferedImage resized = resize(bufferedImage, type);
            File newFile = new File(root + "/" + fileName);
            if (extension==null) {
                throw new NullPointerException();
            }
            ImageIO.write(resized,extension,newFile);
//            Files.copy(inputStream,Path.of(root + "/"+fileName));
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static BufferedImage resize(BufferedImage originalImage, int type){
        double with=500;
        double value = originalImage.getWidth() / with;
        double heightValue = originalImage.getHeight() / value;
        int height = (int)Math.round(heightValue);
        BufferedImage resizedImage = new BufferedImage(500,height,type);
        Graphics graphics = resizedImage.getGraphics();
        graphics.drawImage(originalImage,0,0,500,height,null);
        graphics.dispose();
        return resizedImage;
    }

    @SneakyThrows
    @Override
    public byte[] read(String name) {
        return Files.readAllBytes(Path.of(root + "/" + name));
    }

    @Async
    @Override
    public void deleteImagesIfExists(List<String> images) {
        images.forEach(image -> new File(root+"/"+image).deleteOnExit());
    }

    @Override
    public void existsImage(String profileImage) {
        if (!new File(root+"/"+profileImage).exists()) {
            throw new NotFoundException("Image not found");
        }
    }
}
