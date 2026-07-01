package org.example.service;

import org.example.entities.UserInfo;
import org.example.repository.UserRepository;
import org.example.request.PostDTO;
import org.example.request.UserSearchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserSearchService {
    @Autowired
    private UserRepository userRepository;

    public List<UserSearchDTO> searchUsers(String query)
    {
        List<UserInfo> users = userRepository.findByUsernameContainingIgnoreCaseOrFullNameContainingIgnoreCase(
                query,
                query
        );;



        return users.stream()
                .map(user -> UserSearchDTO.builder()
                        .username(user.getUsername())
                        .fullName(user.getFullName())
                        .profilePictureUrl(user.getProfilePictureUrl())
                        .build())
                .toList();
    }
}   