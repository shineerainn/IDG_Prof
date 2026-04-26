package com.example.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupvProfileDto {

    @NotBlank(message = "文件路径不能为空")
    private String filePath;

    @NotBlank(message = "用户ID不能为空")
    private String userId;

    @NotBlank(message = "画像编号不能为空")
    private String profileId;

    @NotBlank(message = "算法类型不能为空")
    private String algorithmType;

    @NotNull(message = "创建时间不能为空")
    private LocalDateTime createTime;

    @NotBlank(message = "属性数量不能为空")
    private String attributeAmount;

    @NotBlank(message = "属性列表不能为空")
    private String attributeList;
}
