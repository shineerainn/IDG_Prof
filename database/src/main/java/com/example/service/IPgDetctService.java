package com.example.service;

import com.example.pojo.PgDetct;
import com.example.pojo.dto.PgDetctDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IPgDetctService {

    /**
     * 创建预警检测记录
     * @param pgDetctDto 创建DTO
     * @return 保存后的实体
     */
    PgDetct create(PgDetctDto pgDetctDto);

    /**
     * 根据userId获取预警检测记录
     * @param userId
     * @return 预警检测实体数组
     */
    List<PgDetct> getByUserId(String userId);

    /**
     * 根据detctId获取预警检测记录
     * @param detctId
     * @return 预警检测实体
     */
    PgDetct getByDetctId(String detctId);
}