// ClassScheduleServiceImpl.java
package com.example.service;

import com.example.pojo.ClassSchedule;
import com.example.pojo.dto.ClassScheduleDto;
import com.example.repository.ClassScheduleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 班级课表服务实现类
 */
@Service
public class ClassScheduleService implements IClassScheduleService {
    
    private final ClassScheduleRepository classScheduleRepository;
    
    @Autowired
    public ClassScheduleService(ClassScheduleRepository classScheduleRepository) {
        this.classScheduleRepository = classScheduleRepository;
    }
    
    @Override
    public List<ClassSchedule> getAllClassSchedules() {
        return classScheduleRepository.findAll();
    }
    
    @Override
    public ClassSchedule getClassScheduleById(String scoreId) {
        return classScheduleRepository.findById(scoreId).orElse(null);
    }
    
    @Override
    public List<ClassSchedule> getClassSchedulesByStudentId(String studentId) {
        return classScheduleRepository.findByStudentId(studentId);
    }
    
    @Override
    public List<ClassSchedule> getClassSchedulesBySemester(String semester) {
        return classScheduleRepository.findBySemester(semester);
    }
    
    @Override
    public List<ClassSchedule> getClassSchedulesByStudentIdAndSemester(String studentId, String semester) {
        return classScheduleRepository.findByStudentIdAndSemester(studentId, semester);
    }
    
    @Override
    public ClassSchedule saveClassSchedule(ClassScheduleDto classScheduleDto) {
        ClassSchedule classSchedule = new ClassSchedule();
        BeanUtils.copyProperties(classScheduleDto, classSchedule);
        return classScheduleRepository.save(classSchedule);
    }
    
    @Override
    public ClassSchedule updateClassSchedule(String scoreId, ClassScheduleDto classScheduleDto) {
        ClassSchedule existingClassSchedule = classScheduleRepository.findById(scoreId).orElse(null);
        if (existingClassSchedule != null) {
            BeanUtils.copyProperties(classScheduleDto, existingClassSchedule);
            return classScheduleRepository.save(existingClassSchedule);
        }
        return null;
    }
    
    @Override
    public void deleteClassSchedule(String scoreId) {
        classScheduleRepository.deleteById(scoreId);
    }
}
