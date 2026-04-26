package com.example.service;

import com.example.pojo.PgWideTable;
import com.example.pojo.dto.PgWideTableDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IPgWideTableService {
    /**
     * 新增学生宽表记录
     */
    PgWideTable add(PgWideTableDto dto);

    /**
     * 批量新增学生宽表记录
     */
    List<PgWideTable> create(List<PgWideTableDto> dtos);

    /**
     * 根据工作证号查询
     */
    PgWideTable getByStudentId(String studentId);

    /**
     * 查询全部信息
     */
    List<PgWideTable> getAll();

    /**
     * 更新学生宽表记录
     */
    PgWideTable edit(PgWideTableDto dto);

    /**
     * 删除学生宽表记录
     */
    void delete(String studentId);
}
