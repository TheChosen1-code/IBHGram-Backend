package org.example.repository;

import org.example.entities.ConversationParticipant;
import org.example.entities.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationParticipantRepository
        extends JpaRepository<ConversationParticipant, Long> {
    List<ConversationParticipant> findByUser(UserInfo user);
}