package com.bupt.middleware.service;

import com.bupt.middleware.entity.PgWideTable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenxiao
 * @date 2025/4/8 18:30
 * @description: Classify Summarize Service
 */

@Service
public class ClassifySummarizeService implements IClassifySummarizeService {
    /**
     * 更新“健康指数”
     */
    @Override
    public List<PgWideTable> updateIndex(List<PgWideTable> pgWideTables) {
        // 数据转json
        // List<PgWideTable> 转 List<Map<String, Object>>
        if (pgWideTables==null){
            System.out.println("pgWideTables==null");
            return null;
        }
        List<Map<String, Object>> data = tableToMap(pgWideTables);

        // 交由AI计算模块进行分类汇总
        // 选择接口路径
        String url = "http://127.0.0.1:8009/summarize/getScores";
        // 构建请求体（JSON格式）
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("stu_wide_table",data);
        List<Map<String, Object>>wideTable = requestForwarding(requestBody,url);

        // 返回数据
        return mapToTable(wideTable,PgWideTable.class);
    }

    // 包装转发函数
    public List<Map<String,Object>> requestForwarding(Map<String, Object> requestBody,String url){
        // 向python后端发送post请求，获取数据

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // 发送请求并获取响应
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        // 处理python返回内容
        String jsonResponse = response.getBody();
        Gson gson = new Gson();
        Map<String, Object> temp = gson.fromJson(
                jsonResponse,
                new TypeToken<Map<String, Object>>(){}.getType()
        );
        List<Map<String, Object>> wideTable = gson.fromJson(
                temp.get("data").toString(),
                new TypeToken<List<Map<String, Object>>>(){}.getType()
        );
        return wideTable;
    }


    // 宽表转Map格式
    public <T> List<Map<String,Object>> tableToMap(List<T> tables){
        List<Map<String,Object>> data = new ArrayList<>();
        for(T table:tables){
            data.add(bean2Map(table));
        }
        return data;
    }

    // Map格式转宽表
    public <T> List<T> mapToTable(List<Map<String,Object>> jsonList,Class<T> clazz){
        List<T> data = new ArrayList<>();
        for(Map<String,Object> json:jsonList){
            data.add(map2Bean(json,clazz));
        }
        return data;
    }


    // Bean转为Map
    public static Map<String, Object> bean2Map(Object object) {
        Map<String, Object> map = new HashMap<>();
        ReflectionUtils.doWithFields(object.getClass(), field -> {
            field.setAccessible(true);
            Object value = ReflectionUtils.getField(field, object);
            if (value != null) {
                map.put(field.getName(), value);
            }
        });
        return map;
    }

    // Map转为Bean
    public static <T> T map2Bean(Map<String, Object> map, Class<T> clazz) {
        T instance = null;
        try {
            instance = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        T finalInstance = instance;
        ReflectionUtils.doWithFields(clazz, field -> {
            field.setAccessible(true);
            if (map.containsKey(field.getName())) {
                if(field.getType()== LocalDate.class)
                    ReflectionUtils.setField(field, finalInstance, LocalDate.parse((String)map.get(field.getName())));
                else if(map.get(field.getName()) instanceof Number){
                    Number number = (Number)map.get(field.getName());
                    if (field.getType() == Double.class) {
                        ReflectionUtils.setField(field, finalInstance, number.doubleValue());
                    } else if (field.getType() == Integer.class) {
                        ReflectionUtils.setField(field, finalInstance, number.intValue());
                    } else if (field.getType() == Long.class) {
                        ReflectionUtils.setField(field, finalInstance, number.longValue());
                    } else if (field.getType() == Float.class) {
                        ReflectionUtils.setField(field, finalInstance, number.floatValue());
                    }
                }
                else
                    ReflectionUtils.setField(field, finalInstance, map.get(field.getName()));
            }
        });
        return instance;
    }
}
