package org.example.request;

import lombok.*;
import org.example.entities.ConversationType;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationResponse {

    private Long conversationId;

    private ConversationType type;

    private String lastMessage;

    private LocalDateTime lastMessageTime;

}