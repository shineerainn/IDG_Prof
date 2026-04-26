package com.example.controller;

import com.example.pojo.SupvWideTable;
import com.example.pojo.ResponseMessage;
import com.example.pojo.dto.SupvWideTableDto;
import com.example.service.ISupvWideTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/database/supvWideTable")
public class SupvWideTableController {

    @Autowired
    private ISupvWideTableService supvWideTableService;

    /**
     * 新增教师宽表记录
     * @param dto 教师宽表数据传输对象
     * @return 新增的记录
     */
    @PostMapping
    public ResponseMessage<SupvWideTable> add(@Validated @RequestBody SupvWideTableDto dto) {
        SupvWideTable entity = supvWideTableService.add(dto);
        return ResponseMessage.success(entity);
    }

    /**
     * 新增教师宽表记录
     * @param dtos 教师宽表数据传输对象
     * @return 新增的记录
     */
    @PostMapping("/all")
    public ResponseMessage<List<SupvWideTable>> create(@Validated @RequestBody List<SupvWideTableDto> dtos) {
        List<SupvWideTable> entities = supvWideTableService.create(dtos);
        return ResponseMessage.success(entities);
    }

    /**
     * 根据工作证号查询教师信息
     * @param workId 工作证号
     * @return 教师宽表记录
     */
    @GetMapping("/{workId}")
    public ResponseMessage<SupvWideTable> getByWorkId(@PathVariable String workId) {
        SupvWideTable entity = supvWideTableService.getByWorkId(workId);
        return ResponseMessage.success(entity);
    }

    /**
     * 查询全部导师信息
     * @return 导师宽表记录
     */
    @GetMapping
    public ResponseMessage<List<SupvWideTable>> getAll() {
        List<SupvWideTable> entity = supvWideTableService.getAll();
        return ResponseMessage.success(entity);
    }

    /**
     * 更新教师宽表记录
     * @param dto 更新数据
     * @return 更新后的记录
     */
    @PutMapping
    public ResponseMessage<SupvWideTable> edit(@Validated @RequestBody SupvWideTableDto dto) {
        SupvWideTable entity = supvWideTableService.edit(dto);
        return ResponseMessage.success(entity);
    }

    /**
     * 删除教师宽表记录
     * @param workId 工作证号
     * @return 操作结果
     */
    @DeleteMapping("/{workId}")
    public ResponseMessage<Void> delete(@PathVariable String workId) {
        supvWideTableService.delete(workId);
        return ResponseMessage.success();
    }
}