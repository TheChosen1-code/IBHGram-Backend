package org.example.service;

import lombok.AllArgsConstructor;
import org.example.entities.Comment;
import org.example.entities.Post;
import org.example.entities.UserInfo;
import org.example.repository.CommentRepository;
import org.example.repository.PostRepository;
import org.example.repository.UserRepository;
import org.example.request.CommentDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public CommentDTO addComment(Long postId, String text)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        UserInfo currentUser = userRepository.findByUsername(currentUsername);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Comment comment = new Comment();

        comment.setPost(post);
        comment.setUser(currentUser);
        comment.setCreatedAt(Instant.now());
        comment.setText(text);

        Comment savedComment = commentRepository.save(comment);
        return CommentDTO.builder()
                .commentId(savedComment.getCommentId())
                .text(savedComment.getText())
                .createdAt(savedComment.getCreatedAt())
                .profilePictureUrl(savedComment.getUser().getProfilePictureUrl())
                .username(savedComment.getUser().getUsername())
                .fullName(savedComment.getUser().getFullName())
                .build();
    }

    public List<CommentDTO> getComments(Long postId)
    {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        List<Comment> comments = commentRepository.findByPostOrderByCreatedAtDesc(post);

        return comments.stream()
                .map(comment -> CommentDTO.builder()
                        .commentId(comment.getCommentId())
                        .text(comment.getText())
                        .createdAt(comment.getCreatedAt())
                        .username(comment.getUser().getUsername())
                        .fullName(comment.getUser().getFullName())
                        .profilePictureUrl(comment.getUser().getProfilePictureUrl())
                        .build())
                .toList();
    }

    public void deleteComment(Long commentId)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserInfo currentUser = userRepository.findByUsername(username);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));


        if (!comment.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new RuntimeException("You can only delete your own comments");
        }
        commentRepository.delete(comment);
    }
}   