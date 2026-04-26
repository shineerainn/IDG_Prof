package com.example.service;

import com.example.pojo.PgProfile;
import com.example.pojo.dto.PgProfileDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IPgProfileService {

    /**
     * 创建画像记录
     * @param pgProfileDto 创建DTO
     * @return 保存后的实体
     */
    PgProfile create(PgProfileDto pgProfileDto);

    /**
     * 根据用户ID获取画像记录
     * @param userId 用户ID
     * @return 画像实体
     */
    List<PgProfile> getByUserId(String userId);

    /**
     * 根据profileId获取画像记录
     * @param profileId
     * @return 画像实体
     */
    PgProfile getByProfileId(String profileId);
}