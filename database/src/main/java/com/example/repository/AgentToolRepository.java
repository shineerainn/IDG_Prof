package com.example.repository;

import com.example.pojo.AgentTool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AgentToolRepository extends JpaRepository<AgentTool, Long> {

    List<AgentTool> findByIsActiveTrue();

    List<AgentTool> findByCategory(String category);

    @Query("SELECT t FROM AgentTool t WHERE t.isActive = true AND (t.category = :category OR :category IS NULL)")
    List<AgentTool> findActiveToolsByCategory(@Param("category") String category);
}