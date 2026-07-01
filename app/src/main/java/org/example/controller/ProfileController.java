package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.repository.PostRepository;
import org.springframework.security.core.Authentication;
import org.example.entities.UserInfo;
import org.example.request.ProfileDTO;
import org.example.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.example.service.CloudinaryService;

import java.io.IOException;

@AllArgsConstructor
@RestController
public class ProfileController {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CloudinaryService cloudinaryService;

    @GetMapping("/api/profile")
    public ResponseEntity<?> getMyProfile(Authentication authentication) {

        String username = authentication.getName();

        UserInfo user = userRepository.findByUsername(username);

        if(user == null){
            return ResponseEntity.notFound().build();
        }

        ProfileDTO profileResponseDTO =
                ProfileDTO.builder()
                        .userId(user.getUserId())
                        .username(user.getUsername())
                        .fullName(user.getFullName())
                        .branch(user.getBranch())
                        .batchYear(user.getBatchYear())
                        .bio(user.getBio())
                        .profilePictureUrl(user.getProfilePictureUrl())
                        .postCount((int) postRepository.countByUser(user))
                        .followersCount(user.getFollowers().size())
                        .followingCount(user.getFollowing().size())
                        .ownProfile(true)
                        .following(user.getFollowing().contains(user))
                        .build();

        return ResponseEntity.ok(profileResponseDTO);
    }

    @PostMapping("/api/profile/image")
    public ResponseEntity<?> uploadProfileImage(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) throws IOException {

        String username = authentication.getName();

        UserInfo user = userRepository.findByUsername(username);

        if(user == null){
            return ResponseEntity.notFound().build();
        }

        String imageUrl =
                cloudinaryService.uploadImage(file);

        user.setProfilePictureUrl(imageUrl);

        userRepository.save(user);

        return ResponseEntity.ok(imageUrl);
    }

    @GetMapping("/api/profile/{username}")
    public ResponseEntity<?> getUserProfile(
            @PathVariable String username,
            Authentication authentication) {

        UserInfo user = userRepository.findByUsername(username);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        UserInfo currentUser =
                userRepository.findByUsername(authentication.getName());

        ProfileDTO profileResponseDTO =
                ProfileDTO.builder()
                        .userId(user.getUserId())
                        .username(user.getUsername())
                        .fullName(user.getFullName())
                        .branch(user.getBranch())
                        .batchYear(user.getBatchYear())
                        .bio(user.getBio())
                        .profilePictureUrl(user.getProfilePictureUrl())
                        .postCount((int) postRepository.countByUser(user))
                        .followersCount(user.getFollowers().size())
                        .followingCount(user.getFollowing().size())
                        .ownProfile(user.getUsername().equals(currentUser.getUsername()))
                        .following(currentUser.getFollowing().contains(user))
                        .build();

        return ResponseEntity.ok(profileResponseDTO);
    }
}