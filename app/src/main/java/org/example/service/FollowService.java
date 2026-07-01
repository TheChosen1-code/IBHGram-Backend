package org.example.service;

import lombok.AllArgsConstructor;
import org.example.entities.UserInfo;
import org.example.repository.UserRepository;
import org.example.request.FollowDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FollowService {
    private final UserRepository userRepository;
    public FollowDTO followOrUnfollow(String username)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        UserInfo currentUser = userRepository.findByUsername(currentUsername);
        UserInfo targetUser = userRepository.findByUsername(username);

        if (targetUser == null) {
            throw new RuntimeException("User not found");
        }
        if (currentUser.getUserId().equals(targetUser.getUserId()))
        {
            throw new RuntimeException("You cannot follow yourself");
        }

        if(currentUser.getFollowing().contains(targetUser))
            currentUser.getFollowing().remove(targetUser);
        else
            currentUser.getFollowing().add(targetUser);
        userRepository.save(currentUser);

        return FollowDTO.builder()
                .following(currentUser.getFollowing().contains(targetUser))
                .followersCount(targetUser.getFollowers().size())
                .followingCount(targetUser.getFollowing().size())
                .build();
    }
}   