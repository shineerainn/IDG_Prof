package com.example.repository;

import com.example.pojo.AgentConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AgentConversationRepository extends JpaRepository<AgentConversation, Long> {

    List<AgentConversation> findByUserIdOrderByUpdatedAtDesc(String userId);

    List<AgentConversation> findByUserIdAndToolIdOrderByUpdatedAtDesc(String userId, Long toolId);

    @Query("SELECT c FROM AgentConversation c WHERE c.userId = :userId AND c.isArchived = false ORDER BY c.isPinned DESC, c.updatedAt DESC")
    List<AgentConversation> findActiveConversationsByUser(@Param("userId") String userId);
}