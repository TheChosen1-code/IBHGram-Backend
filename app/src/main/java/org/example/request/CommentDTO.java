package org.example.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private Long commentId;

    private String text;

    private Instant createdAt;

    private String username;

    private String fullName;

    private String profilePictureUrl;
}