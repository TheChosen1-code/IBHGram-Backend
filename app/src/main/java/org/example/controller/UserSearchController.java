package org.example.controller;

import org.example.request.UserSearchDTO;
import org.example.service.UserSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserSearchController {
    @Autowired
    private UserSearchService userSearchService;

    @GetMapping("/api/users/search")
    public ResponseEntity<List<UserSearchDTO>> searchUsers(@RequestParam("query") String query) {
        List<UserSearchDTO> users = userSearchService.searchUsers(query);
        return ResponseEntity.ok(users);
    }
}   