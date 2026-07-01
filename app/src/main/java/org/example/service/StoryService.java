package org.example.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.entities.Story;
import org.example.entities.UserInfo;
import org.example.repository.StoryRepository;
import org.example.repository.UserRepository;
import org.example.request.StoryDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
public class StoryService {
    private final UserRepository userRepository;

    private final StoryRepository storyRepository;

    private final CloudinaryService cloudinaryService;

    public StoryDTO uploadStory(MultipartFile image, String caption) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserInfo user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Story story = new Story();
        if (caption == null || caption.trim().isEmpty()) {
            caption = "";
        }
        if (image == null || image.isEmpty()) {
            throw new RuntimeException("Image is required");
        }
        story.setCaption(caption);
        story.setImageUrl(cloudinaryService.uploadImage(image));
        story.setUser(user);
        story.setCreatedAt(Instant.now());
        story.setExpiresAt(
                Instant.now().plus(1, ChronoUnit.DAYS)
        );

        Story savedStory = storyRepository.save(story);
        return StoryDTO.builder()
                .storyId(savedStory.getStoryId())
                .caption(savedStory.getCaption())
                .imageUrl(savedStory.getImageUrl())
                .createdAt(savedStory.getCreatedAt())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .profilePictureUrl(user.getProfilePictureUrl())
                .build();
    }

    public List<StoryDTO> getStories()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserInfo user = userRepository.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("User not found");
        }


        Set<UserInfo> following = new HashSet<>(user.getFollowing());
        following.add(user);

        List<Story> stories = storyRepository.findAllByExpiresAtAfterOrderByCreatedAtDesc(Instant.now());

        return stories.stream()
                .filter(story -> following.contains(story.getUser()))
                .map(story -> StoryDTO.builder()
                        .storyId(story.getStoryId())
                        .caption(story.getCaption())
                        .imageUrl(story.getImageUrl())
                        .createdAt(story.getCreatedAt())
                        .username(story.getUser().getUsername())
                        .fullName(story.getUser().getFullName())
                        .userId(story.getUser().getUserId())
                        .profilePictureUrl(story.getUser().getProfilePictureUrl())
                        .build())
                .toList();
    }
}