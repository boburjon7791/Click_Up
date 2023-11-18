package com.example.demo.controller;

import com.example.demo.service.MultimediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@PreAuthorize("permitAll()")
@RequestMapping("/api.multimedia")
public class MultimediaController {
    private final MultimediaService multimediaService;
    @PostMapping(value = "/save",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String save(@RequestParam(name = "file")MultipartFile file){
        return multimediaService.save(file);
    }

    @GetMapping(value = "/read/{name}",produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] read(@PathVariable String name){
        return multimediaService.read(name);
    }
}
