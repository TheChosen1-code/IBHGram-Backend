package org.example.repository;

import org.example.entities.Post;
import org.example.entities.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long>
{
    List<Post> findByUserOrderByCreatedAtDesc(UserInfo user);
    long countByUser(UserInfo user);
    List<Post> findByUserInOrderByCreatedAtDesc(Set<UserInfo> users);
}
