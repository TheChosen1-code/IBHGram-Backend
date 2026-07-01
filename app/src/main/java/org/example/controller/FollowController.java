package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.request.FollowDTO;
import org.example.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class FollowController {

    private FollowService followService;
    @PostMapping("/api/follow/{username}")
    public ResponseEntity<FollowDTO> followOrUnfollow(
            @PathVariable String username) {

        return ResponseEntity.ok(
                followService.followOrUnfollow(username)
        );
    }
}   