package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.request.StoryDTO;
import org.example.service.StoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/stories")
public class StoryController {

    private final StoryService storyService;

    @PostMapping
    public ResponseEntity<StoryDTO> uploadStory(
            @RequestParam("image") MultipartFile image,
            @RequestParam(value = "caption", required = false) String caption
    ) throws IOException {

        return ResponseEntity.ok(
                storyService.uploadStory(image, caption)
        );
    }

    @GetMapping
    public ResponseEntity<List<StoryDTO>> getStories() {

        return ResponseEntity.ok(
                storyService.getStories()
        );
    }
}