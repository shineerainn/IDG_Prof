package com.example.service;

import com.example.pojo.PgAcademic;
import com.example.pojo.dto.PgAcademicDto;
import com.example.repository.PgAcademicRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PgAcademicService implements IPgAcademicService {

    @Autowired
    private PgAcademicRepository pgAcademicRepository;

    @Override
    public PgAcademic add(PgAcademicDto dto) {
        // 检查学号是否已存在
        if (pgAcademicRepository.existsByStudentId(dto.getStudentId())) {
            throw new IllegalArgumentException("学号 " + dto.getStudentId() + " 已存在，无法重复添加");
        }
        
        PgAcademic entity = new PgAcademic();
        // 使用BeanUtils复制属性
        BeanUtils.copyProperties(dto, entity);
        return pgAcademicRepository.save(entity);
    }

    @Override
    public List<PgAcademic> create(List<PgAcademicDto> dtos) {
        List<PgAcademic> entities = new ArrayList<>();
        List<String> existingStudentIds = new ArrayList<>();

        // 检查是否有重复的学号
        for (PgAcademicDto dto : dtos) {
            if (pgAcademicRepository.existsByStudentId(dto.getStudentId())) {
                existingStudentIds.add(dto.getStudentId());
            }
        }
        
        if (!existingStudentIds.isEmpty()) {
            throw new IllegalArgumentException("以下学号已存在，无法重复添加: " + String.join(", ", existingStudentIds));
        }

        // 批量创建实体
        for (PgAcademicDto dto : dtos) {
            PgAcademic entity = new PgAcademic();
            BeanUtils.copyProperties(dto, entity);
            entities.add(entity);
        }

        return (List<PgAcademic>) pgAcademicRepository.saveAll(entities);
    }

    @Override
    public PgAcademic getByStudentId(String studentId) {
        Optional<PgAcademic> optional = pgAcademicRepository.findByStudentId(studentId);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new IllegalArgumentException("未找到学号为 " + studentId + " 的学业信息记录");
        }
    }

    @Override
    public List<PgAcademic> getAll() {
        List<PgAcademic> entities = (List<PgAcademic>) pgAcademicRepository.findAll();
        return entities;
    }

    @Override
    public void delete(String studentId) {
        // 先检查是否存在
        if (!pgAcademicRepository.existsByStudentId(studentId)) {
            throw new IllegalArgumentException("要删除的学业信息记录不存在，学号: " + studentId);
        }
        pgAcademicRepository.deleteByStudentId(studentId);
    }

    @Override
    public PgAcademic update(PgAcademicDto dto) {
        PgAcademic entity = new PgAcademic();
        // 使用BeanUtils复制属性
        BeanUtils.copyProperties(dto, entity);
        return pgAcademicRepository.save(entity);
    }

    @Override
    public List<PgAcademic> batchUpdate(List<PgAcademicDto> dtos) {
        List<PgAcademic> entities = new ArrayList<>();
        
        for (PgAcademicDto dto : dtos) {
            // 检查学号是否存在
            if (!pgAcademicRepository.existsByStudentId(dto.getStudentId())) {
                throw new IllegalArgumentException("学号 " + dto.getStudentId() + " 不存在，无法更新");
            }
            
            PgAcademic entity = new PgAcademic();
            BeanUtils.copyProperties(dto, entity);
            entities.add(entity);
        }
        
        return (List<PgAcademic>) pgAcademicRepository.saveAll(entities);
    }
}
