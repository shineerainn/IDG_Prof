package com.example.repository;

import com.example.pojo.PgProfile;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PgProfileRepository extends CrudRepository<PgProfile, Long> {

    @Query("SELECT p FROM PgProfile p WHERE p.userId = :userId ORDER BY p.createTime DESC")
    List<PgProfile> findByUserId(@Param("userId") String userId);

    PgProfile findByProfileId(String profileId);
}
