package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.CloudinaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final CloudinaryService cloudinaryService;

    @PostMapping("/api/test/upload")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file) {

        try {
            System.out.println("Upload endpoint hit");

            String imageUrl =
                    cloudinaryService.uploadImage(file);

            System.out.println(imageUrl);

            return ResponseEntity.ok(imageUrl);

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }
}