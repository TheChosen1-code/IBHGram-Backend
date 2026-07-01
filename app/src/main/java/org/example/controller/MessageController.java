package org.example.controller;

import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.example.request.MessageResponse;
import org.example.request.SendMessageRequest;
import org.example.request.SendMessageResult;
import org.example.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageResponse> sendMessage(
            @RequestBody SendMessageRequest request
    ) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        SendMessageResult result =
                messageService.sendMessage(request, username);

        return ResponseEntity.ok(
                result.getMessage()
        );
    }

    @GetMapping("/{conversationId}")
    public ResponseEntity<List<MessageResponse>> getMessages(
            @PathVariable Long conversationId
    ){

        return ResponseEntity.ok(
                messageService.getMessages(conversationId)
        );
    }
}

