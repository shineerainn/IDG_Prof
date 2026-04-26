package com.example.repository;

import com.example.pojo.PgWideTable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PgWideTableRepository extends CrudRepository<PgWideTable, String> {
}
