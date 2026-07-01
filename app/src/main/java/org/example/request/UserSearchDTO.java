package org.example.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserSearchDTO {

    private String username;
    private String fullName;
    private String profilePictureUrl;
}