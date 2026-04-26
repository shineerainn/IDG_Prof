package com.example.repository;

import com.example.pojo.SupvProfile;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupvProfileRepository extends CrudRepository<SupvProfile, Long> {

    @Query("SELECT s FROM SupvProfile s WHERE s.userId = :userId ORDER BY s.createTime DESC")
    List<SupvProfile> findByUserId(@Param("userId") String userId);

    SupvProfile findByProfileId(String profileId);
}
