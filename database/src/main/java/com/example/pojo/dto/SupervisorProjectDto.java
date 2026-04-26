package com.example.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupervisorProjectDto {

    @NotBlank(message = "工号不能为空")
    private String workId; // 工号

    @NotBlank(message = "姓名不能为空")
    private String supervisorName; // 姓名

    @NotBlank(message = "单位不能为空")
    private String organization; // 单位

    @NotBlank(message = "项目名称不能为空")
    private String projectName; // 项目名称

    @NotBlank(message = "项目编号不能为空")
    private String projectNumber; // 项目编号

    @NotNull(message = "立项日期不能为空")
    private LocalDate approvalDate; // 立项日期

    @NotNull(message = "结项日期不能为空")
    private LocalDate completionDate; // 结项日期

    @NotBlank(message = "项目级别不能为空")
    private String projectLevel; // 项目级别

    @NotBlank(message = "项目来源不能为空")
    private String fundingSource; // 项目来源

    @NotBlank(message = "QTDW不能为空")
    private String rankingUnit; // QTDW

    @NotNull(message = "位次不能为空")
    private Integer rankingPosition; // 位次

    @NotNull(message = "合同经费不能为空")
    private Double contractFunding; // 合同经费

    @NotNull(message = "年度到账经费不能为空")
    private Double annualFundingReceived; // 年度到账经费

    @NotNull(message = "已到账总经费不能为空")
    private Double totalFundingReceived; // 已到账总经费

    @NotBlank(message = "项目类型不能为空")
    private String projectType; // 项目类型

    @NotBlank(message = "XMZL不能为空")
    private String projectCategory; // XMZL

    @NotBlank(message = "项目性质不能为空")
    private String projectNature; // 项目性质
}
