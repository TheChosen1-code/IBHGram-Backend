package org.example.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProfileDTO {
    private String userId;
    private String username;
    private String fullName;
    private String branch;
    private String batchYear;
    private String bio;
    private String profilePictureUrl;

    private int postCount;
    private int followersCount;
    private int followingCount;

    private boolean ownProfile;
    private boolean following;
}