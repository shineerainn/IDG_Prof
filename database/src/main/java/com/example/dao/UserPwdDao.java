package com.example.dao;

import com.example.domain.UserPwd1;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserPwdDao {
    //根据用户id查询
    @Select("select * from user_pwd where user_id=#{user_id}")
    UserPwd1 GetUserPwd(String user_id);

}
