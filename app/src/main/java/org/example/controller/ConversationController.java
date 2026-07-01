package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.request.ConversationListResponse;
import org.example.request.ConversationResponse;
import org.example.request.CreateConversationRequest;
import org.example.request.MessageResponse;
import org.example.service.ConversationService;
import org.example.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;


    @PostMapping("/private")
    public ResponseEntity<ConversationResponse> createPrivateConversation(
            @RequestBody CreateConversationRequest request
    ) {
        return ResponseEntity.ok(
                conversationService.createOrGetPrivateConversation(request)
        );
    }

    @GetMapping
    public ResponseEntity<List<ConversationListResponse>> getConversations() {
        return ResponseEntity.ok(
                conversationService.getConversations()
        );
    }

    @PostMapping
    public ResponseEntity<ConversationListResponse> createConversation(
            @RequestBody CreateConversationRequest request
    ) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(
                conversationService.createConversation(
                        request,
                        authentication.getName()
                )
        );
    }
}

