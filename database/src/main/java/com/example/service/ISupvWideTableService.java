package com.example.service;

import com.example.pojo.SupvWideTable;
import com.example.pojo.dto.SupvProfileDto;
import com.example.pojo.dto.SupvWideTableDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ISupvWideTableService {

    /**
     * 新增导师宽表记录
     */
    SupvWideTable add(SupvWideTableDto dto);

    /**
     * 创建画像记录
     * @param dtos 创建DTO
     * @return 保存后的实体
     */
    List<SupvWideTable> create(List<SupvWideTableDto> dtos);

    /**
     * 根据工作证号查询
     */
    SupvWideTable getByWorkId(String workId);

    /**
     * 查询全部信息
     */
    List<SupvWideTable> getAll();

    /**
     * 更新导师宽表记录
     */
    SupvWideTable edit(SupvWideTableDto dto);

    /**
     * 删除导师宽表记录
     */
    void delete(String workId);

}