package com.bupt.middleware.service;

import com.bupt.middleware.entity.PgWideTable;
import com.bupt.middleware.entity.SupvWideTable;
import com.bupt.middleware.entity.result.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
class ClassifySummarizeServiceTest {

    @Autowired
    PgWideTableService pgWideTableService;

    @Autowired
    SupvWideTableService supvWideTableService;

    @Autowired
    ClassifySummarizeService classifySummarizeService;

    @Autowired
    SupvProfileService supvProfileService;

    @Autowired
    PgProfileService pgProfileService;

    @Autowired
    DetectionService detectionService;

    @Test
    void updateIndex() {
        Result res = pgWideTableService.getAllPg();
//        System.out.println((List<PgWideTable>) res.getData());
        List<PgWideTable>ans = classifySummarizeService.updateIndex((List<PgWideTable>) res.getData());
        System.out.println(ans.get(0));
    }

    @Test
    void profileModelingSupv() {
        Result res = supvWideTableService.getAllSupv();
//        Result res = pgWideTableService.getAllPg();

        // 获取类的 Class 对象（假设类名为 MyClass）
        Class<?> clazz = SupvWideTable.class;

        // 获取当前类的所有字段（包括私有、保护、默认访问权限的字段）[1,3](@ref)
        Field[] fields = clazz.getDeclaredFields();

        List<String> fieldNames = new ArrayList<>();
        for (Field field : fields) {
            fieldNames.add(field.getName());
        }
//        fieldNames = List.of("gender", "currentAge", "title", "isMasterSupervisor", "isDoctorSupervisor", "highestDegree",
//                "masterNumber", "doctorNumber", "masterDelayNumber", "doctorDelayNumber", "masterDelayRate",
//                "doctorDelayRate", "excellentGraduatesNumber", "excellentGraduationThesis", "prizeCount",
//                "competitionCount", "employmentRate", "highLevelPaper", "enterpriseProject", "governmentProject",
//                "averageFund", "undergraduateCourse", "graduateCourse", "undergraduateCourseCredit",
//                "graduateCourseCredit", "isUniversityLeader", "isCollegeLeader", "isDean", "isCenterDirector",
//                "firstYearRankRate", "secondYearRankRate", "thirdYearRankRate","entryDate");
//        List<Map<String, Object>>ans = supvProfileService.buildSupvProfile(List.of("gender", "currentAge", "title", "isMasterSupervisor", "isDoctorSupervisor", "highestDegree",
//                "masterNumber", "doctorNumber", "masterDelayNumber", "doctorDelayNumber", "masterDelayRate",
//                "doctorDelayRate", "excellentGraduatesNumber", "excellentGraduationThesis", "prizeCount",
//                "competitionCount", "employmentRate", "highLevelPaper", "enterpriseProject", "governmentProject",
//                "averageFund", "undergraduateCourse", "graduateCourse", "undergraduateCourseCredit",
//                "graduateCourseCredit", "isUniversityLeader", "isCollegeLeader", "isDean", "isCenterDirector",
//                "firstYearRankRate", "secondYearRankRate", "thirdYearRankRate","entryDate"),(List<SupvWideTable>) res.getData(),null);
        System.out.println("column:"+fieldNames.size());
        List<Map<String, Object>>ans = supvProfileService.buildSupvProfile(fieldNames,(List<SupvWideTable>) res.getData(),null);
        for(String str:fieldNames){
            if(!ans.get(0).containsKey(str)){
                System.out.println(str);
            }
        }
        System.out.println(ans.get(0).size());
    }
    @Test
    void profileModelingStu() {
//        Result res = pgWideTableService.getAllPg();
        Result res = pgWideTableService.getAllPg();

        // 获取类的 Class 对象（假设类名为 MyClass）
        Class<?> clazz = PgWideTable.class;

        // 获取当前类的所有字段（包括私有、保护、默认访问权限的字段）[1,3](@ref)
        Field[] fields = clazz.getDeclaredFields();

        List<String> fieldNames = new ArrayList<>();
        for (Field field : fields) {
            fieldNames.add(field.getName());
        }
//        fieldNames = List.of("enrollmentType", "currentGrade", "isStudentLeader", "individualAwardCount",
//                "improperRemarksCount", "latestScholarshipLevel", "academicIndex", "prizeCount");
//        List<Map<String, Object>>ans = pgProfileService.buildPgProfile(List.of("enrollmentType", "currentGrade", "isStudentLeader", "individualAwardCount",
//                "improperRemarksCount", "latestScholarshipLevel", "academicIndex", "prizeCount"),(List<PgWideTable>) res.getData(),null);
        List<Map<String, Object>>ans = pgProfileService.buildPgProfile(fieldNames,(List<PgWideTable>) res.getData(),null);
        System.out.println("column:"+fieldNames.size());
        System.out.println(ans.get(0));
        System.out.println(ans.get(0).size());
        for(String str:fieldNames){
            if(!ans.get(0).containsKey(str)){
                System.out.println(str);
            }
        }
    }

    @Test
    void decisionTreeClassifyTrain() {
        Result res = pgWideTableService.getAllPg();

        // 获取类的 Class 对象（假设类名为 MyClass）
        Class<?> clazz = PgWideTable.class;

        // 获取当前类的所有字段（包括私有、保护、默认访问权限的字段）[1,3](@ref)
        Field[] fields = clazz.getDeclaredFields();

        List<String> fieldNames = new ArrayList<>();
        for (Field field : fields) {
            fieldNames.add(field.getName());
        }
//        System.out.println(fieldNames);


//        String ans = detectionService.buildDetectionModel(List.of("enrollmentType", "currentGrade", "isStudentLeader", "individualAwardCount",
//                "improperRemarksCount", "latestScholarshipLevel", "academicIndex", "prizeCount"),(List<PgWideTable>) res.getData(),null);
        String ans = detectionService.buildDetectionModel(fieldNames,(List<PgWideTable>) res.getData(),null);
        System.out.println(ans);
    }

    @Test
    void decisionTreeClassifyPredict() {
        Result res = pgWideTableService.getAllPg();
        String model_name = "20250411_185629";
        List<Map<String, Object>>ans = detectionService.inferDetectionModel((List<PgWideTable>) res.getData(),model_name);
//        System.out.println();
        System.out.println(ans.get(0).size());
        int a50 = 0;
        int a75 = 0;
        for (Map<String, Object> map : ans) {
            if((Double)map.get("confidence")>0.0){
                a50++;
                if((Double)map.get("confidence")>0.5)
                    a75++;
            }
        }
        System.out.println(a50);
        System.out.println(a75);
    }
}