package com.example.service;

import com.example.pojo.SupvWideTable;
import com.example.pojo.dto.SupvWideTableDto;
import com.example.repository.SupvWideTableRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupvWideTableService implements ISupvWideTableService {

    @Autowired
    private SupvWideTableRepository supvWideTableRepository;

    @Override
    public SupvWideTable add(SupvWideTableDto dto) {
        SupvWideTable entity = new SupvWideTable();
        // 使用BeanUtils复制属性
        BeanUtils.copyProperties(dto, entity);
        return supvWideTableRepository.save(entity);
    }

    @Override
    public List<SupvWideTable> create(List<SupvWideTableDto> dtos) {
        List<SupvWideTable> entities = new ArrayList<>();

        for (SupvWideTableDto dto : dtos) {
            SupvWideTable entity = new SupvWideTable();
            BeanUtils.copyProperties(dto, entity);
            entities.add(entity);
        }

        return (List<SupvWideTable>) supvWideTableRepository.saveAll(entities);
    }

    @Override
    public SupvWideTable getByWorkId(String workId) {
        return supvWideTableRepository.findById(workId).orElseThrow(() -> {
            throw new IllegalArgumentException("未找到工作证号为 " + workId + " 的记录");
        });
    }

    @Override
    public List<SupvWideTable> getAll() {
        List<SupvWideTable> entity = (List<SupvWideTable>) supvWideTableRepository.findAll();
        return entity;
    }

    @Override
    public SupvWideTable edit(SupvWideTableDto dto) {
        SupvWideTable entity = new SupvWideTable();
        // 使用BeanUtils复制属性
        BeanUtils.copyProperties(dto, entity);
        return supvWideTableRepository.save(entity);
    }

    @Override
    public void delete(String workId) {
        // 先检查是否存在
        if (!supvWideTableRepository.existsById(workId)) {
            throw new IllegalArgumentException("要删除的记录不存在，工作证号: " + workId);
        }
        supvWideTableRepository.deleteById(workId);
    }
}