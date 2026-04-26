package com.example.repository;

import com.example.pojo.PgDetct;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PgDetctRepository extends CrudRepository<PgDetct, Long> {
    @Query("SELECT p FROM PgDetct p WHERE p.userId = :userId ORDER BY p.createTime DESC")
    List<PgDetct> getByUserId(@Param("userId") String userId);

    PgDetct getByDetctId(String detctId);
}
