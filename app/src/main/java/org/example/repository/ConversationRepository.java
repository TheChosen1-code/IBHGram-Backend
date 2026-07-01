package org.example.repository;

import org.example.entities.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    @Query("""
SELECT c
FROM Conversation c
JOIN c.participants p1
JOIN c.participants p2
WHERE c.type = org.example.entities.ConversationType.PRIVATE
AND SIZE(c.participants) = 2
AND p1.user.userId = :user1
AND p2.user.userId = :user2
""")
    Optional<Conversation> findPrivateConversation(
            @Param("user1") String user1,
            @Param("user2") String user2
    );
}