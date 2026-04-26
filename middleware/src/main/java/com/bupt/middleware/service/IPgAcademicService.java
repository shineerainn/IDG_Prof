package com.bupt.middleware.service;

import com.bupt.middleware.entity.PgAcademic;
import com.bupt.middleware.entity.result.Result;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface IPgAcademicService {
    /**
     * 根据studentId查询学生学业数据
     */
    Result<PgAcademic> getPg(String studentId);

    /**
     * 查询全部学业数据
     */
    Result<List<PgAcademic>> getAllPg();

    /**
     * 更新学生学业数据
     */
    Result<String> updatePg(PgAcademic dto);

    /**
     * 批量更新学生学业数据
     */
    Result<String> updatePgs(List<PgAcademic> pgAcademics);

}
