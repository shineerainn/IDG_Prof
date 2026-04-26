package com.example.repository;

import com.example.pojo.SupvWideTable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupvWideTableRepository extends CrudRepository<SupvWideTable, String> {
}
