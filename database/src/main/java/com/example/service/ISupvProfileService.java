package com.example.service;

import com.example.pojo.SupvProfile;
import com.example.pojo.dto.SupvProfileDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ISupvProfileService {

    /**
     * 创建画像记录
     * @param SupvProfileDto 创建DTO
     * @return 保存后的实体
     */
    SupvProfile create(SupvProfileDto SupvProfileDto);

    /**
     * 根据用户ID获取画像记录
     * @param userId 用户ID
     * @return 画像实体
     */
    List<SupvProfile> getByUserId(String userId);

    /**
     * 根据profileId获取画像记录
     * @param profileId
     * @return 画像实体
     */
    SupvProfile getByProfileId(String profileId);
}