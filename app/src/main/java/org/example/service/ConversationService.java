package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entities.*;
import org.example.repository.ConversationParticipantRepository;
import org.example.repository.ConversationRepository;
import org.example.repository.MessageRepository;
import org.example.repository.UserRepository;
import org.example.request.ConversationListResponse;
import org.example.request.ConversationResponse;
import org.example.request.CreateConversationRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ConversationService {

    private final ConversationRepository conversationRepository;

    private final ConversationParticipantRepository participantRepository;

    private final UserRepository userRepository;

    private final MessageRepository messageRepository;

    public ConversationResponse createOrGetPrivateConversation(
            CreateConversationRequest request
    )
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String senderId = userRepository.findByUsername(authentication.getName()).getUserId();
        String receiverId = request.getReceiverId();
        if(senderId.equals(receiverId)){
            throw new RuntimeException("You cannot message yourself.");
        }

        UserInfo sender = userRepository.findById(senderId)
                .orElseThrow(() ->
                        new RuntimeException("Sender not found"));

        UserInfo receiver = userRepository.findById(receiverId)
                .orElseThrow(() ->
                        new RuntimeException("Receiver not found"));

        Optional<Conversation> existingConversation =
                conversationRepository.findPrivateConversation(
                        senderId,
                        receiverId
                );

        if(existingConversation.isPresent()){
            Conversation conversation = existingConversation.get();

            return ConversationResponse.builder()
                    .conversationId(conversation.getId())
                    .type(conversation.getType())
                    .lastMessage(conversation.getLastMessage())
                    .lastMessageTime(conversation.getLastMessageTime())
                    .build();
        }

        Conversation conversation = Conversation.builder()
                .type(ConversationType.PRIVATE)
                .build();

        conversation = conversationRepository.save(conversation);

        ConversationParticipant senderParticipant =
                ConversationParticipant.builder()
                        .conversation(conversation)
                        .user(sender)
                        .build();

        ConversationParticipant receiverParticipant =
                ConversationParticipant.builder()
                        .conversation(conversation)
                        .user(receiver)
                        .build();

        participantRepository.saveAll(
                List.of(senderParticipant, receiverParticipant)
        );

        return ConversationResponse.builder()
                .conversationId(conversation.getId())
                .type(conversation.getType())
                .lastMessage(conversation.getLastMessage())
                .lastMessageTime(conversation.getLastMessageTime())
                .build();
    }

    public List<ConversationListResponse> getConversations() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String currentUsername = authentication.getName();

        UserInfo currentUser = userRepository.findByUsername(currentUsername);

        List<ConversationParticipant> participants =
                participantRepository.findByUser(currentUser);

        List<ConversationListResponse> response = new ArrayList<>();

        for (ConversationParticipant participant : participants) {

            Conversation conversation = participant.getConversation();

            UserInfo otherUser = null;

            for (ConversationParticipant p : conversation.getParticipants()) {

                if (!p.getUser().getUserId()
                        .equals(currentUser.getUserId())) {

                    otherUser = p.getUser();
                    break;
                }
            }

            if (otherUser != null) {

                response.add(
                        ConversationListResponse.builder()
                                .conversationId(conversation.getId())
                                .otherUserId(otherUser.getUserId())
                                .otherUsername(otherUser.getUsername())
                                .otherFullName(otherUser.getFullName())
                                .otherProfilePicture(otherUser.getProfilePictureUrl())
                                .lastMessage(conversation.getLastMessage())
                                .lastMessageTime(conversation.getLastMessageTime())
                                .unreadCount(0L)
                                .build()
                );
            }
        }

        response.sort(
                Comparator.comparing(
                        ConversationListResponse::getLastMessageTime,
                        Comparator.nullsLast(Comparator.reverseOrder())
                )
        );

        return response;
    }

    public ConversationListResponse createConversation(
            CreateConversationRequest request,
            String username
    )
    {
        UserInfo currentUser =
                userRepository.findByUsername(username);

        UserInfo receiver =
                userRepository.findById(request.getReceiverId())
                        .orElseThrow(() ->
                                new RuntimeException("User not found"));

        if (currentUser.getUserId().equals(receiver.getUserId())) {
            throw new RuntimeException("You cannot create a conversation with yourself.");
        }

        Optional<Conversation> existingConversation =
                conversationRepository.findPrivateConversation(
                        currentUser.getUserId(),
                        receiver.getUserId()
                );

        if(existingConversation.isPresent()) {

            return buildConversationResponse(
                    existingConversation.get(),
                    receiver
            );

        }

        Conversation conversation =
                Conversation.builder()
                        .type(ConversationType.PRIVATE)
                        .build();

        conversation =
                conversationRepository.save(conversation);


        ConversationParticipant participant1 =
                ConversationParticipant.builder()
                        .conversation(conversation)
                        .user(currentUser)
                        .build();


        ConversationParticipant participant2 =
                ConversationParticipant.builder()
                        .conversation(conversation)
                        .user(receiver)
                        .build();

        participantRepository.save(participant1);

        participantRepository.save(participant2);

        return buildConversationResponse(
                conversation,
                receiver
        );
    }

    private ConversationListResponse buildConversationResponse(
            Conversation conversation,
            UserInfo otherUser
    ) {

        return ConversationListResponse.builder()
                .conversationId(conversation.getId())
                .otherUserId(otherUser.getUserId())
                .otherUsername(otherUser.getUsername())
                .otherFullName(otherUser.getFullName())
                .otherProfilePicture(otherUser.getProfilePictureUrl())
                .lastMessage(conversation.getLastMessage())
                .lastMessageTime(conversation.getLastMessageTime())
                .unreadCount(0L)
                .build();
    }

}

