package com.example.repository;

import com.example.pojo.AgentMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AgentMessageRepository extends JpaRepository<AgentMessage, Long> {

    List<AgentMessage> findByConversationIdOrderByCreatedAtAsc(Long conversationId);

    List<AgentMessage> findByConversationIdOrderByCreatedAtDesc(Long conversationId, Pageable pageable);

    long countByConversationId(Long conversationId);
}