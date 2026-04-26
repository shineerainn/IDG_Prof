package com.example.controller;

import com.example.pojo.PgAcademic;
import com.example.pojo.ResponseMessage;
import com.example.pojo.dto.PgAcademicDto;
import com.example.service.IPgAcademicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/database/pgAcademic")
@Slf4j
public class PgAcademicController {

    @Autowired
    private IPgAcademicService pgAcademicService;

    /**
     * 新增学生学业数据
     * @param dto 学生学业数据传输对象
     * @return 新增的记录
     */
    @PostMapping
    public ResponseMessage<PgAcademic> add(@Validated @RequestBody PgAcademicDto dto) {
        PgAcademic entity = pgAcademicService.add(dto);
        return ResponseMessage.success(entity);
    }

    /**
     * 批量新增学生学业数据
     * @param dtos 学生学业数据传输对象列表
     * @return 新增的记录列表
     */
    @PostMapping("/all")
    public ResponseMessage<List<PgAcademic>> create(@Validated @RequestBody List<PgAcademicDto> dtos) {
        List<PgAcademic> entities = pgAcademicService.create(dtos);
        return ResponseMessage.success(entities);
    }

    /**
     * 根据学生学号查询学业信息
     * @param studentId 学生学号
     * @return 学生学业记录
     */
    @GetMapping("/{studentId}")
    public ResponseMessage<PgAcademic> getByStudentId(@PathVariable String studentId) {
        PgAcademic entity = pgAcademicService.getByStudentId(studentId);
        return ResponseMessage.success(entity);
    }

    /**
     * 查询全部学生学业信息
     * @return 学生学业记录列表
     */
    @GetMapping
    public ResponseMessage<List<PgAcademic>> getAll() {
        List<PgAcademic> entities = pgAcademicService.getAll();
        return ResponseMessage.success(entities);
    }

    /**
     * 根据学生学号更新学业信息
     * @param dto 学生学业数据传输对象
     * @return 更新后的记录
     */
    @PutMapping
    public ResponseMessage<PgAcademic> update(@Validated @RequestBody PgAcademicDto dto) {
        PgAcademic entity = pgAcademicService.update(dto);
        return ResponseMessage.success(entity);
    }

    /**
     * 批量更新学生学业信息
     * @param dtos 学生学业数据传输对象列表
     * @return 更新后的记录列表
     */
    @PutMapping("/batch")
    public ResponseMessage<List<PgAcademic>> batchUpdate(@Validated @RequestBody List<PgAcademicDto> dtos) {
        List<PgAcademic> entities = pgAcademicService.batchUpdate(dtos);
        return ResponseMessage.success(entities);
    }

    /**
     * 根据学生学号删除学业信息
     * @param studentId 学生学号
     * @return 操作结果
     */
    @DeleteMapping("/{studentId}")
    public ResponseMessage<Void> delete(@PathVariable String studentId) {
        pgAcademicService.delete(studentId);
        return ResponseMessage.success();
    }
}
