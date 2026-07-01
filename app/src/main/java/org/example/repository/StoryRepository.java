package org.example.repository;

import org.example.entities.Story;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Long> {

    List<Story> findAllByExpiresAtAfterOrderByCreatedAtDesc(Instant now);

}