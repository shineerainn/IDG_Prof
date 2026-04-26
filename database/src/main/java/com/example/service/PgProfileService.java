package com.example.service;

import com.example.pojo.PgProfile;
import com.example.pojo.dto.PgProfileDto;
import com.example.repository.PgProfileRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PgProfileService implements IPgProfileService{

    @Autowired
    private PgProfileRepository pgProfileRepository;

    @Override
    public PgProfile create(PgProfileDto pgProfileDto) {

        // 创建新实体
        PgProfile pgProfile = new PgProfile();
        BeanUtils.copyProperties(pgProfileDto, pgProfile);

        // 保存到数据库
        return pgProfileRepository.save(pgProfile);
    }

    @Override
    public List<PgProfile> getByUserId(String userId) {
        return pgProfileRepository.findByUserId(userId);
    }

    @Override
    public PgProfile getByProfileId(String profileId) {
        return pgProfileRepository.findByProfileId(profileId);
    }
}
