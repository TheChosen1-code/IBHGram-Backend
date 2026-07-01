package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.request.CommentDTO;
import org.example.request.CommentRequestDTO;
import org.example.request.PostDTO;
import org.example.service.CommentService;
import org.example.service.LikeService;
import org.example.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
public class PostController {

    private PostService postService;
    private LikeService likeService;
    private CommentService commentService;

    @PostMapping("/api/posts")
    public ResponseEntity<PostDTO> createPost(@RequestParam("image") MultipartFile image,
                                              @RequestParam("caption") String caption) throws IOException {
        PostDTO postDTO = postService.createPost(image, caption);
        return ResponseEntity.ok(postDTO);
    }

    @GetMapping("/api/posts/user/{username}")
    public ResponseEntity<List<PostDTO>> getPostsByUser(@PathVariable("username") String username) {
        List<PostDTO> posts = postService.getPostsByUser(username);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/api/posts/feed")
    public ResponseEntity<List<PostDTO>> getFeed() {
        return ResponseEntity.ok(postService.getFeed());
    }

    @PostMapping("/api/posts/{postId}/like")
    public ResponseEntity<PostDTO> toggleLike(
            @PathVariable Long postId) {

        return ResponseEntity.ok(
                likeService.toggleLike(postId)
        );
    }

    @PostMapping("/api/posts/{postId}/comments")
    public ResponseEntity<CommentDTO> addComment(
            @PathVariable Long postId,
            @RequestBody CommentRequestDTO request) {

        return ResponseEntity.ok(
                commentService.addComment(postId, request.getText())
        );
    }

    @GetMapping("/api/posts/{postId}/comments")
    public ResponseEntity<List<CommentDTO>> getComments(
            @PathVariable Long postId) {

        return ResponseEntity.ok(
                commentService.getComments(postId)
        );
    }

    @DeleteMapping("api/posts/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {

        System.out.println("DELETE CONTROLLER HIT");

        postService.deletePost(postId);

        return ResponseEntity.ok("Post deleted successfully");
    }

    @PutMapping("/api/posts/{postId}")
    public ResponseEntity<PostDTO> editCaption(
            @PathVariable Long postId,
            @RequestBody CommentRequestDTO request) {

        return ResponseEntity.ok(
                postService.editCaption(postId, request.getText())
        );
    }

    @DeleteMapping("/api/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId)
    {
        commentService.deleteComment(commentId);

        return ResponseEntity.ok("Comment deleted Successfully");
    }
}