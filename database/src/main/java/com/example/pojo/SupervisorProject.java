package com.example.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Table(name = "supervisor_project")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupervisorProject {

    @Id
    @Column(name = "work_id")
    private String workId; // 工号

    @Column(name = "supervisor_name")
    private String supervisorName; // 姓名

    @Column(name = "organization")
    private String organization; // 单位

    @Column(name = "project_name")
    private String projectName; // 项目名称

    @Column(name = "project_number")
    private String projectNumber; // 项目编号

    @Column(name = "approval_date")
    private LocalDate approvalDate; // 立项日期

    @Column(name = "completion_date")
    private LocalDate completionDate; // 结项日期

    @Column(name = "project_level")
    private String projectLevel; // 项目级别

    @Column(name = "funding_source")
    private String fundingSource; // 项目来源

    @Column(name = "ranking_unit")
    private String rankingUnit; // QTDW

    @Column(name = "ranking_position")
    private Integer rankingPosition; // 位次

    @Column(name = "contract_funding")
    private Double contractFunding; // 合同经费

    @Column(name = "annual_funding_received")
    private Double annualFundingReceived; // 年度到账经费

    @Column(name = "total_funding_received")
    private Double totalFundingReceived; // 已到账总经费

    @Column(name = "project_type")
    private String projectType; // 项目类型

    @Column(name = "project_category")
    private String projectCategory; // XMZL

    @Column(name = "project_nature")
    private String projectNature; // 项目性质
}