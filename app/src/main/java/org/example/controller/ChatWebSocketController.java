package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.request.SendMessageRequest;
import org.example.request.SendMessageResult;
import org.example.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.Payload;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final MessageService messageService;

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(
            SendMessageRequest request,
            @Payload Principal principal
    ) {
        SendMessageResult result =
                messageService.sendMessage(
                        request,
                        principal.getName()
                );

        messagingTemplate.convertAndSendToUser(
                result.getReceiverUsername(),
                "/queue/messages",
                result.getMessage()
        );

        messagingTemplate.convertAndSendToUser(
                principal.getName(),
                "/queue/messages",
                result.getMessage()
        );
    }

}