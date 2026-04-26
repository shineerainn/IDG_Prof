package com.example.controller;

import com.example.pojo.PgWideTable;
import com.example.pojo.ResponseMessage;
import com.example.pojo.dto.PgWideTableDto;
import com.example.service.IPgWideTableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/database/pgWideTable")
@Slf4j
public class PgWideTableController {

    @Autowired
    private IPgWideTableService pgWideTableService;

    /**
     * 新增学生宽表记录
     * @param dto 学生宽表数据传输对象
     * @return 新增的记录
     */
    @PostMapping
    public ResponseMessage<PgWideTable> add(@Validated @RequestBody PgWideTableDto dto) {
        PgWideTable entity = pgWideTableService.add(dto);
        return ResponseMessage.success(entity);
    }

    /**
     * 批量新增学生宽表记录
     * @param dtos 学生宽表数据传输对象
     * @return 新增的记录
     */
    @PostMapping("/all")
    public ResponseMessage<List<PgWideTable>> create(@Validated @RequestBody List<PgWideTableDto> dtos) {
        List<PgWideTable> entities = pgWideTableService.create(dtos);
        return ResponseMessage.success(entities);
    }

    /**
     * 根据工作证号查询学生信息
     * @param studentId 学生证号
     * @return 学生宽表记录
     */
    @GetMapping("/{studentId}")
    public ResponseMessage<PgWideTable> getByStudentId(@PathVariable String studentId) {
        PgWideTable entity = pgWideTableService.getByStudentId(studentId);
        return ResponseMessage.success(entity);
    }

    /**
     * 查询全部学生信息
     * @return 学生宽表记录
     */
    @GetMapping
    public ResponseMessage<List<PgWideTable>> getAll() {
        List<PgWideTable> entity = pgWideTableService.getAll();
        return ResponseMessage.success(entity);
    }

    /**
     * 更新学生宽表记录
     * @param dto 更新数据
     * @return 更新后的记录
     */
    @PutMapping
    public ResponseMessage<PgWideTable> edit(@Validated @RequestBody PgWideTableDto dto) {
        PgWideTable entity = pgWideTableService.edit(dto);
        return ResponseMessage.success(entity);
    }

    /**
     * 删除学生宽表记录
     * @param studentId 学生证号
     * @return 操作结果
     */
    @DeleteMapping("/{studentId}")
    public ResponseMessage<Void> delete(@PathVariable String studentId) {
        pgWideTableService.delete(studentId);
        return ResponseMessage.success();
    }
}
