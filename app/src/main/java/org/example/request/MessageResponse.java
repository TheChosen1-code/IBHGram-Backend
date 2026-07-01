package org.example.request;

import lombok.*;
import org.example.entities.MessageType;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    private Long messageId;

    private String senderId;

    private String content;

    private MessageType type;

    private LocalDateTime createdAt;

    private String senderUsername;

}