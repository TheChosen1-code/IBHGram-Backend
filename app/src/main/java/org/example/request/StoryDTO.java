package org.example.request;

import lombok.*;

import java.time.Instant;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoryDTO {

    private Long storyId;

    private String imageUrl;

    private Instant createdAt;

    private String username;

    private String fullName;

    private String caption;

    private String userId;

    private String profilePictureUrl;
}