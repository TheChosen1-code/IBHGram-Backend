package org.example.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.entities.Conversation;
import org.example.entities.ConversationParticipant;
import org.example.entities.Message;
import org.example.entities.UserInfo;
import org.example.repository.ConversationRepository;
import org.example.repository.MessageRepository;
import org.example.repository.UserRepository;
import org.example.request.MessageResponse;
import org.example.request.SendMessageRequest;
import org.example.request.SendMessageResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;

    private final ConversationRepository conversationRepository;

    private final UserRepository userRepository;

    public SendMessageResult sendMessage(
            SendMessageRequest request,
            String username
    )
    {
        Conversation conversation =
                conversationRepository.findById(
                        request.getConversationId()
                ).orElseThrow(() ->
                        new RuntimeException("Conversation not found"));

        UserInfo sender = userRepository.findByUsername(username);

        Message message =
                Message.builder()
                        .conversation(conversation)
                        .sender(sender)
                        .content(request.getContent())
                        .type(request.getType())
                        .build();

        message = messageRepository.save(message);

        conversation.setLastMessage(
                message.getContent()
        );

        conversation.setLastMessageTime(
                message.getCreatedAt()
        );

        conversationRepository.save(conversation);

        MessageResponse response =
                MessageResponse.builder()
                        .messageId(message.getId())
                        .senderId(sender.getUserId())
                        .senderUsername(sender.getUsername())
                        .content(message.getContent())
                        .type(message.getType())
                        .createdAt(message.getCreatedAt())
                        .build();

        UserInfo receiver = null;

        for (ConversationParticipant participant : conversation.getParticipants()) {

            if (!participant.getUser().getUserId()
                    .equals(sender.getUserId())) {

                receiver = participant.getUser();
                break;
            }
        }

        return SendMessageResult.builder()
                .message(response)
                .receiverUsername(receiver.getUsername())
                .build();
    }

    public List<MessageResponse> getMessages(Long conversationId)
    {
        Conversation conversation =
                conversationRepository.findById(conversationId)
                        .orElseThrow(() ->
                                new RuntimeException("Conversation not found"));

        List<Message> messages =
                messageRepository.findByConversationOrderByCreatedAtAsc(
                        conversation
                );

        return messages.stream()
                .map(message ->
            MessageResponse.builder()
                    .messageId(message.getId())
                    .senderId(message.getSender().getUserId())
                    .senderUsername(
                            message.getSender().getUsername()
                    )
                    .content(message.getContent())
                    .type(message.getType())
                    .createdAt(message.getCreatedAt())
                    .build()
                ).toList();
    }
}