package com.example.service;

import com.example.pojo.PgWideTable;
import com.example.pojo.dto.PgWideTableDto;
import com.example.repository.PgWideTableRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PgWideTableService implements IPgWideTableService{

    @Autowired
    private PgWideTableRepository pgWideTableRepository;

    @Override
    public PgWideTable add(PgWideTableDto dto) {
        PgWideTable entity = new PgWideTable();
        // 使用BeanUtils复制属性
        BeanUtils.copyProperties(dto, entity);
        return pgWideTableRepository.save(entity);
    }

    @Override
    public List<PgWideTable> create(List<PgWideTableDto> dtos) {
        List<PgWideTable> entities = new ArrayList<>();

        for (PgWideTableDto dto : dtos) {
            PgWideTable entity = new PgWideTable();
            BeanUtils.copyProperties(dto, entity);
            entities.add(entity);
        }

        return (List<PgWideTable>) pgWideTableRepository.saveAll(entities);
    }

    @Override
    public PgWideTable getByStudentId(String studentId) {
        return pgWideTableRepository.findById(studentId).orElseThrow(() -> {
            throw new IllegalArgumentException("未找到学生证号为 " + studentId + " 的记录");
        });
    }

    @Override
    public List<PgWideTable> getAll() {
        List<PgWideTable> entity = (List<PgWideTable>) pgWideTableRepository.findAll();
        return entity;
    }

    @Override
    public PgWideTable edit(PgWideTableDto dto) {
        PgWideTable entity = new PgWideTable();
        // 使用BeanUtils复制属性
        BeanUtils.copyProperties(dto, entity);
        return pgWideTableRepository.save(entity);
    }

    @Override
    public void delete(String studentId) {
        // 先检查是否存在
        if (!pgWideTableRepository.existsById(studentId)) {
            throw new IllegalArgumentException("要删除的记录不存在，工作证号: " + studentId);
        }
        pgWideTableRepository.deleteById(studentId);
    }
}
