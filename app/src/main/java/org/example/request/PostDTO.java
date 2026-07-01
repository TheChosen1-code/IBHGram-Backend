package org.example.request;

import lombok.Builder;
import lombok.Data;
import org.example.entities.UserInfo;

import java.time.Instant;

@Builder
@Data
public class PostDTO {
    private Long postId;

    private String caption;

    private String imageUrl;

    private Instant createdAt;

    private String username;

    private String fullName;

    private String profilePictureUrl;

    private long postCount;

    private int likes;

    private boolean likedByCurrentUser;

    private int commentCount;
}