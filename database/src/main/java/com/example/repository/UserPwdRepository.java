package com.example.repository;

import com.example.pojo.UserPwd;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPwdRepository extends CrudRepository<UserPwd, String> {
}
