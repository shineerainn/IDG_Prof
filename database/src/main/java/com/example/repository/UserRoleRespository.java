package com.example.repository;

import com.example.pojo.UserRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRespository extends CrudRepository<UserRole, String> {
}
