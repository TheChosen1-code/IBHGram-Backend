package org.example.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationListResponse {

    private Long conversationId;

    private String otherUserId;

    private String otherUsername;

    private String otherFullName;

    private String otherProfilePicture;

    private String lastMessage;

    private LocalDateTime lastMessageTime;

    private Long unreadCount;
}