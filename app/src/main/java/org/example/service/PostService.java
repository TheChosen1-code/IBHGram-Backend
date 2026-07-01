package org.example.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.entities.Post;
import org.example.entities.UserInfo;
import org.example.repository.PostRepository;
import org.example.repository.UserRepository;
import org.example.request.PostDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class PostService
{
    private UserRepository userRepository;

    private PostRepository postRepository;

    private CloudinaryService cloudinaryService;

    public PostDTO createPost(MultipartFile image, String caption) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserInfo user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Post post = new Post();
        if(caption == null || caption.trim().isEmpty()){
            caption = "";
        }
        if(image == null || image.isEmpty()){
            throw new RuntimeException("Image is required");
        }
        post.setCaption(caption);
        post.setImageUrl(cloudinaryService.uploadImage(image));
        post.setUser(user);
        post.setCreatedAt(Instant.now());

        Post savedPost = postRepository.save(post);
        return PostDTO.builder()
                .postId(savedPost.getPostId())
                .caption(savedPost.getCaption())
                .imageUrl(savedPost.getImageUrl())
                .createdAt(savedPost.getCreatedAt())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .profilePictureUrl(user.getProfilePictureUrl())
                .likes(savedPost.getLikedUsers().size())
                .commentCount(savedPost.getComments().size())
                .likedByCurrentUser(savedPost.getLikedUsers().contains(user))
                .build();
    }

    @Transactional
    public PostDTO editCaption(Long postId, String caption) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserInfo user = userRepository.findByUsername(username);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));


        if (!post.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("You can only edit your own captions");
        }
        if(caption == null || caption.trim().isEmpty()){
            caption = "";
        }
        post.setCaption(caption);

        Post savedPost = postRepository.save(post);
        return PostDTO.builder()
                .postId(savedPost.getPostId())
                .caption(savedPost.getCaption())
                .imageUrl(savedPost.getImageUrl())
                .createdAt(savedPost.getCreatedAt())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .profilePictureUrl(user.getProfilePictureUrl())
                .likes(savedPost.getLikedUsers().size())
                .commentCount(savedPost.getComments().size())
                .likedByCurrentUser(savedPost.getLikedUsers().contains(user))
                .build();
    }

    public List<PostDTO> getPostsByUser(String username) {

        UserInfo user = userRepository.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        List<Post> posts = postRepository.findByUserOrderByCreatedAtDesc(user);

        return posts.stream()
                .map(post -> PostDTO.builder()
                        .postId(post.getPostId())
                        .caption(post.getCaption())
                        .imageUrl(post.getImageUrl())
                        .createdAt(post.getCreatedAt())
                        .username(user.getUsername())
                        .fullName(user.getFullName())
                        .profilePictureUrl(user.getProfilePictureUrl())
                        .likes(post.getLikedUsers().size())
                        .likedByCurrentUser(post.getLikedUsers().contains(user))
                        .commentCount(post.getComments().size())
                        .build())
                .toList();
    }

    @Transactional
    public void deletePost(long postId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserInfo currentUser = userRepository.findByUsername(username);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));


        if (!post.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new RuntimeException("You can only delete your own posts");
        }
        postRepository.delete(post);
    }

    public List<PostDTO> getFeed()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserInfo user = userRepository.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("User not found");
        }


        Set<UserInfo> following = new HashSet<>(user.getFollowing());
        following.add(user);

        List<Post> posts = postRepository.findByUserInOrderByCreatedAtDesc(following);
        System.out.println("Posts found = " + posts.size());

        for (Post post : posts) {
            System.out.println(post.getPostId() + " -> " + post.getUser().getUsername());
        }
        return posts.stream()
                .map(post -> PostDTO.builder()
                        .postId(post.getPostId())
                        .caption(post.getCaption())
                        .imageUrl(post.getImageUrl())
                        .createdAt(post.getCreatedAt())
                        .username(post.getUser().getUsername())
                        .fullName(post.getUser().getFullName())
                        .profilePictureUrl(post.getUser().getProfilePictureUrl())
                        .likes(post.getLikedUsers().size())
                        .commentCount(post.getComments().size())
                        .likedByCurrentUser(post.getLikedUsers().contains(user))
                        .build())
                .toList();
    }
}   