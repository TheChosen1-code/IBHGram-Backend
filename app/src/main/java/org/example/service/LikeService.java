package org.example.service;

import lombok.AllArgsConstructor;
import org.example.entities.Post;
import org.example.entities.UserInfo;
import org.example.repository.PostRepository;
import org.example.repository.UserRepository;
import org.example.request.PostDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LikeService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    public PostDTO toggleLike(Long postId)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        UserInfo currentUser = userRepository.findByUsername(currentUsername);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));


        if(post.getLikedUsers().contains(currentUser))
            post.getLikedUsers().remove(currentUser);
        else
            post.getLikedUsers().add(currentUser);
        postRepository.save(post);

        return PostDTO.builder()
                .postId(post.getPostId())
                .caption(post.getCaption())
                .imageUrl(post.getImageUrl())
                .createdAt(post.getCreatedAt())
                .username(post.getUser().getUsername())
                .fullName(post.getUser().getFullName())
                .profilePictureUrl(post.getUser().getProfilePictureUrl())
                .likes(post.getLikedUsers().size())
                .likedByCurrentUser(post.getLikedUsers().contains(currentUser))
                .build();
    }
}   