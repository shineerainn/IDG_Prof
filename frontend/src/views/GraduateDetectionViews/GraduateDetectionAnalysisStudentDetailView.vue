<template>
  <div class="detail-container">
    <!-- 头部导航 -->
    <div class="header">
      <h1 class="title">预警详情</h1>
      <div class="model-info">模型Id：{{ modelId }}</div>
      <el-button
        type="primary"
        icon="el-icon-arrow-left"
        @click="goBack"
        class="back-btn"
      >
        返回列表
      </el-button>
    </div>

    <!-- 学生信息板块 -->
    <el-card class="info-card">
      <div class="student-info">
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-row">
              <span class="label">姓名：</span>{{ studentInfo.name }}
            </div>
            <div class="info-row">
              <span class="label">学号：</span>{{ studentInfo.studentId }}
            </div>
            <div class="info-row">
              <span class="label">学院：</span>{{ studentInfo.college }}
            </div>
            <div class="info-row">
              <span class="label">专业：</span>{{ studentInfo.major }}
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-row">
              <span class="label">年龄：</span>{{ studentInfo.age }}
            </div>
            <div class="info-row">
              <span class="label">年级：</span>{{ studentInfo.grade }}
            </div>
            <div class="info-row">
              <span class="label">联系方式：</span>{{ studentInfo.phone }}
            </div>
          </el-col>
          <el-col :span="24" class="photo-container">
            <img
              :src="
                require(
                  studentInfo.gender === '男'
                    ? '@/assets/PhotoBoy.png'
                    : '@/assets/PhotoGirl.png',
                )
              "
              alt="证件照"
              class="id-photo"
            />
          </el-col>
        </el-row>
      </div>
    </el-card>

    <!-- 预警信息板块 -->
    <el-card class="warning-card">
      <div class="warning-info">
        <div class="warning-level">
          <el-tag :type="color" size="large" style="margin-right: 10px">
            {{ warningTypes[warningType] }}
          </el-tag>
          <el-tag :type="color" size="large" style="margin-right: 10px">
            预警分数：{{ warningScore }}
          </el-tag>
          <span class="warning-description">
            {{ getWarningDescription(warningScore) }}
          </span>
          <div class="button-group">
            <el-button
              type="text"
              style="color: #f56c6c"
              @click="showFeedbackDialog"
            >
              <i class="el-icon-warning-outline"></i>
              误报反馈
            </el-button>
            <el-button type="primary" @click="goToSuggestion">
              <i class="el-icon-edit-outline"></i>
              查看建议
            </el-button>
          </div>
        </div>

        <div class="scroll-container">
          <el-table :data="featureContributions" border height="390px">
            <el-table-column prop="feature" label="特征项" width="200" />
            <el-table-column prop="contribution" label="个人特征值" sortable>
              <template #default="{ row }">
                <span :class="{ 'highlight-value': row.isSignificant }">
                  {{ row.contribution }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="averageScore" label="群体特征范围" sortable>
              <template #default="{ row }">
                <!-- 如果是数组且长度为2，展示为 "值1 ~ 值2" -->
                {{
                  Array.isArray(row.averageScore) &&
                  row.averageScore.length === 2
                    ? `${row.averageScore[0]} ~ ${row.averageScore[1]}`
                    : row.averageScore
                }}
              </template>
            </el-table-column>
            <el-table-column prop="description" label="特征说明" />
          </el-table>
        </div>
      </div>
    </el-card>

    <!-- 误报反馈对话框 -->
    <el-dialog
      title="误报反馈"
      :visible.sync="feedbackDialogVisible"
      width="500px"
      center
    >
      <el-form :model="feedbackForm" label-width="80px">
        <el-form-item label="反馈原因" prop="reason" required>
          <el-input
            type="textarea"
            :rows="4"
            v-model="feedbackForm.reason"
            placeholder="请详细描述您认为误报的原因"
            maxlength="500"
            show-word-limit
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="feedbackDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitFeedback">提 交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getGraduateDetectionStudentDetail } from "@/api/graduateDetection";
export default {
  name: "GraduateDetectionAnalysisStudentDetailView",
  data() {
    return {
      studentInfo: {},
      warningTypes: {
        1: "学业异常",
        2: "毕设异常",
        3: "就业异常",
        4: "生活异常",
      },
      color: "info",
      featureContributions: [],
      warningType: "",
      warningScore: "",
      feedbackDialogVisible: false,
      feedbackForm: {
        reason: "",
      },
      // 需要过滤的特征列表
      filteredAttributes: [
        "graduateName", // 研究生姓名
        "sn",
        "studentId",
        "confidence",
        "flag",
      ],
      // 不需要高亮显示的字段列表
      noHighlightAttributes: [
        "cellPhoneNumber", // 手机号
        "supervisorWorkId", // 导师工号
        "counselorWorkId", // 辅导员工号
        "citizenId", // 身份证号
      ],
      // 属性映射表
      attributeMappings: {
        // 属性名到中文名的映射
        featureNames: {
          isPovertyStudent: "经济困难",
          citizenId: "身份证号",
          undergraduateMajor: "本科专业",
          improperRemarksCount: "不当言论次数",
          academicIndex: "学业健康指数",
          credit: "学分进度",
          failedCredit: "不及格学分",
          paperCount: "发表论文数",
          eiPaperCount: "EI论文数",
          sciPaperCount: "SCI论文数",
          prizeCount: "国奖获奖次数",
          competitionCount: "竞赛获奖次数",
          academicStatus: "学业状态",
          graduationDesignIndex: "毕设健康指数",
          thesisProposal: "开题情况",
          middleCheck: "中期检查",
          duplicateCheck: "查重",
          blindReview: "盲审",
          preDefense: "预答辩",
          defense: "答辩",
          employmentIndex: "就业健康指数",
          jobIntention: "求职意向",
          sign: "签约情况",
          offer: "意向情况",
          admission: "录取情况",
          livingIndex: "生活健康指数",
          averageFee: "周平均消费",
          averageFoodFee: "食堂消费",
          averageFoodCount: "食堂消费次数",
          averageBathFee: "浴室消费",
          averageBathCount: "浴室消费次数",
          averageStoreFee: "超市消费",
          averageStoreCount: "超市消费次数",
          averageStadiumCount: "运动场进入次数",
          averageExitCount: "出校门次数",
          averageStayHour: "宿舍驻留时长",
          averageInternetHour: "上网时长",
          averageMobileHour: "移动端上网时长",
          averagePcHour: "非移动端上网时长",
          averageForumPost: "论坛发帖次数",
          averageForumReply: "论坛回帖次数",
          averageLateNightPost: "深夜发帖次数",
          averageLateNightReply: "深夜回帖次数",
          monthlyAbnormalCount: "月异常行为次数",
          latestIssueType: "问题种类",
          isOnlyChild: "独生子女",
          individualAwardCount: "个人获奖次数",
          major: "专业",
          school: "学院",
          postgraduateScore: "研究生成绩",
          nativePlace: "籍贯",
          enrollmentType: "入学方式",
          supervisorName: "导师",
          gender: "性别",
          counselorWorkId: "辅导员工号",
          latestScholarshipLevel: "最新奖学金等级",
          cellPhoneNumber: "手机号",
          supervisorWorkId: "导师工号",
          registerDate: "入学时间",
          currentAge: "当前年龄",
          isStudentLeader: "是否为学生干部",
          counselorName: "辅导员",
          graduateName: "研究生姓名",
          currentGrade: "当前年级",
          politicalStatus: "政治面貌",
          undergraduateSchool: "本科学校",
          highestPunishmentLevel: "最高处分等级",
          stdFee: "周消费标准差",
          gradientFee: "周消费梯度",
          stdFoodFee: "食堂消费标准差",
          gradientFoodFee: "食堂消费梯度",
          stdBathFee: "浴室消费标准差",
          gradientBathFee: "浴室消费梯度",
          stdStoreFee: "超市消费标准差",
          gradientStoreFee: "超市消费梯度",
          stdStayHour: "宿舍驻留时长标准差",
          stdInternetHour: "上网时长标准差",
          stdMobileHour: "移动端上网时长标准差",
          stdPcHour: "非移动端上网时长标准差",
          stdForumPost: "论坛发帖次数标准差",
          stdForumReply: "论坛回帖次数标准差",
          stdLateNightPost: "深夜发帖次数标准差",
          stdLateNightReply: "深夜回帖次数标准差",
          stdBathCount: "浴室使用次数标准差",
          gradientForumReply: "论坛回帖次数梯度",
          gradientForumPost: "论坛发帖次数梯度",
          gradientLateNightPost: "深夜发帖次数梯度",
          gradientLateNightReply: "深夜回帖次数梯度",
          gradientPcHour: "非移动端上网时长梯度",
          stdStadiumCount: "运动场进入次数标准差",
          gradientStadiumCount: "运动场进入次数梯度",
          gradientStayHour: "宿舍驻留时长梯度",
          stdFoodCount: "食堂消费次数标准差",
          gradientBathCount: "浴室使用次数梯度",
          stdExitCount: "出校门次数标准差",
          gradientMobileHour: "移动端上网时长梯度",
          stdStoreCount: "超市购物次数标准差",
          gradientExitCount: "出校门次数梯度",
          gradientStoreCount: "超市购物次数梯度",
          gradientInternetHour: "上网时长梯度",
          gradientFoodCount: "食堂消费次数梯度",
        },
        // 属性名到描述的映射
        descriptions: {
          isPovertyStudent: "是否为经济困难学生",
          citizenId: "身份证号",
          undergraduateMajor: "本科所学专业",
          improperRemarksCount: "发表不当言论的次数",
          academicIndex: "学业综合健康指数",
          credit: "当前学分完成进度",
          failedCredit: "不及格学分总数",
          paperCount: "已发表论文总数",
          eiPaperCount: "已发表EI论文数量",
          sciPaperCount: "已发表SCI论文数量",
          prizeCount: "获得国家奖学金次数",
          competitionCount: "参加竞赛获奖次数",
          academicStatus: "当前学业状态",
          graduationDesignIndex: "毕业设计健康指数",
          thesisProposal: "开题报告完成情况",
          middleCheck: "中期检查完成情况",
          duplicateCheck: "论文查重结果",
          blindReview: "盲审结果",
          preDefense: "预答辩情况",
          defense: "答辩情况",
          employmentIndex: "就业综合健康指数",
          jobIntention: "是否有明确的求职意向",
          sign: "是否已签约",
          offer: "是否收到offer",
          admission: "是否被录取",
          livingIndex: "生活综合健康指数",
          averageFee: "每周平均消费金额",
          averageFoodFee: "每周食堂消费金额",
          averageFoodCount: "每周食堂消费次数",
          averageBathFee: "每周浴室消费金额",
          averageBathCount: "每周浴室使用次数",
          averageStoreFee: "每周超市消费金额",
          averageStoreCount: "每周超市购物次数",
          averageStadiumCount: "每周进入运动场次数",
          averageExitCount: "每周出校门次数",
          averageStayHour: "每周宿舍驻留时长",
          averageInternetHour: "每周上网总时长",
          averageMobileHour: "每周移动端上网时长",
          averagePcHour: "每周非移动端上网时长",
          averageForumPost: "每周论坛发帖次数",
          averageForumReply: "每周论坛回帖次数",
          averageLateNightPost: "每周深夜发帖次数",
          averageLateNightReply: "每周深夜回帖次数",
          monthlyAbnormalCount: "每月异常行为反馈次数",
          latestIssueType: "最新问题类型",
          isOnlyChild: "是否为独生子女",
          individualAwardCount: "获个人奖次数之和",
          major: "专业",
          school: "学院",
          postgraduateScore: "研究生成绩-1为非考研入学",
          nativePlace: "籍贯",
          enrollmentType: "入学方式",
          supervisorName: "导师",
          gender: "性别",
          counselorWorkId: "辅导员工号",
          latestScholarshipLevel: "最新奖学金等级",
          cellPhoneNumber: "手机号",
          supervisorWorkId: "导师工号",
          registerDate: "入学时间",
          currentAge: "当前年龄",
          isStudentLeader: "是否为学生干部",
          counselorName: "辅导员",
          graduateName: "研究生姓名",
          currentGrade: "当前年级",
          politicalStatus: "政治面貌",
          undergraduateSchool: "本科学校",
          highestPunishmentLevel: "最高处分等级",
          stdFee: "周消费标准差",
          gradientFee: "周消费梯度",
          stdFoodFee: "食堂消费标准差",
          gradientFoodFee: "食堂消费梯度",
          stdBathFee: "浴室消费标准差",
          gradientBathFee: "浴室消费梯度",
          stdStoreFee: "超市消费标准差",
          gradientStoreFee: "超市消费梯度",
          stdStayHour: "宿舍驻留时长标准差",
          stdInternetHour: "上网时长标准差",
          stdMobileHour: "移动端上网时长标准差",
          stdPcHour: "非移动端上网时长标准差",
          stdForumPost: "论坛发帖次数标准差",
          stdForumReply: "论坛回帖次数标准差",
          stdLateNightPost: "深夜发帖次数标准差",
          stdLateNightReply: "深夜回帖次数标准差",
          stdBathCount: "浴室使用次数标准差",
          gradientForumReply: "论坛回帖次数梯度",
          gradientForumPost: "论坛发帖次数梯度",
          gradientLateNightPost: "深夜发帖次数梯度",
          gradientLateNightReply: "深夜回帖次数梯度",
          gradientPcHour: "非移动端上网时长梯度",
          stdStadiumCount: "运动场进入次数标准差",
          gradientStadiumCount: "运动场进入次数梯度",
          gradientStayHour: "宿舍驻留时长梯度",
          stdFoodCount: "食堂消费次数标准差",
          gradientBathCount: "浴室使用次数梯度",
          stdExitCount: "出校门次数标准差",
          gradientMobileHour: "移动端上网时长梯度",
          stdStoreCount: "超市购物次数标准差",
          gradientExitCount: "出校门次数梯度",
          gradientStoreCount: "超市购物次数梯度",
          gradientInternetHour: "上网时长梯度",
          gradientFoodCount: "食堂消费次数梯度",
        },
      },
    };
  },
  mounted() {
    this.modelId = this.$route.query.modelId || "";
    this.studentId = this.$route.query.studentId || "";
    this.color = this.$route.query.color || "info";
    this.warningType = this.$route.query.warningType || "未知类型";
    this.warningScore = this.$route.query.warningScore || "0";
    if (!this.modelId || !this.studentId) {
      this.$message.error("缺少必要参数");
      this.$router.push("/graduate-detection/modeling");
      return;
    }

    this.fetchStudentDetail();
  },
  methods: {
    async fetchStudentDetail() {
      try {
        const response = await getGraduateDetectionStudentDetail({
          detectionId: this.modelId,
          studentId: this.studentId,
        });

        if (response && response.data) {
          // 处理特征贡献度数据
          this.featureContributions = response.data
            .filter((item) => !this.filteredAttributes.includes(item.attribute))
            .map((item) => {
              const feature =
                this.attributeMappings.featureNames[item.attribute] ||
                item.attribute;
              const description =
                this.attributeMappings.descriptions[item.attribute] || "";

              // 根据类型处理value值
              let contribution = item.value[0];
              let averageScore = item.value[1];

              // 如果是布尔值，转换为"是"或"否"
              if (item.type === "boolean") {
                contribution = contribution ? "是" : "否";
                averageScore = averageScore ? "是" : "否";
              }

              // 如果是分类值，保持原样
              if (item.type === "categorical") {
                contribution = contribution || "未知";
                averageScore = averageScore || "未知";
              }

              // 如果是数值，保留两位小数
              if (item.type === "numeric") {
                contribution =
                  typeof contribution === "number"
                    ? contribution.toFixed(2)
                    : contribution;
                averageScore =
                  typeof averageScore === "number"
                    ? averageScore.toFixed(2)
                    : averageScore;
              }

              // 判断是否需要高亮
              let isSignificant = false;

              // 检查当前字段是否在 noHighlightAttributes 中
              if (this.noHighlightAttributes.includes(item.attribute)) {
                isSignificant = false; // 不高亮
              } else {
                // 检查 contribution 是否为 "N/A"
                if (contribution === "N/A") {
                  isSignificant = false; // 如果是 N/A，则不高亮
                } else {
                  if (item.type === "numeric") {
                    isSignificant = !item.value[2];
                  } else {
                    isSignificant = item.value[2] < 0.1;
                  }
                }
              }

              return {
                feature,
                contribution,
                averageScore,
                isSignificant,
                description,
              };
            });

          // 按照 isSignificant 和 contribution 降序排序
          this.featureContributions.sort((a, b) => {
            if (b.isSignificant !== a.isSignificant) {
              return b.isSignificant ? 1 : -1; // 高亮字段排在前面
            }

            const aValue =
              typeof a.contribution === "number"
                ? a.contribution
                : parseFloat(a.contribution);
            const bValue =
              typeof b.contribution === "number"
                ? b.contribution
                : parseFloat(b.contribution);

            return bValue - aValue; // 降序排序
          });

          // 从响应数据中提取学生信息
          const studentInfoMap = {};
          response.data.forEach((item) => {
            if (item.value && item.value[0] !== undefined) {
              studentInfoMap[item.attribute] = item.value[0];
            }
          });

          // 生成studentInfo对象
          this.studentInfo = {
            name: studentInfoMap.graduateName || "未知",
            studentId: this.studentId || "未知",
            college: studentInfoMap.school || "未知",
            major: studentInfoMap.major || "未知",
            age: studentInfoMap.currentAge || "未知",
            grade: studentInfoMap.currentGrade || "未知",
            phone: studentInfoMap.cellPhoneNumber || "未知",
            photo: require("@/assets/default-avatar.png"),
            gender: studentInfoMap.gender || "未知",
          };
        }
      } catch (error) {
        console.error("获取学生详情失败:", error);
        this.$message.error("获取学生详情失败");
      }
    },
    goBack() {
      this.$router.go(-1);
    },
    showFeedbackDialog() {
      this.feedbackForm.reason = "";
      this.feedbackDialogVisible = true;
    },
    submitFeedback() {
      if (!this.feedbackForm.reason.trim()) {
        this.$message.warning("请填写反馈原因");
        return;
      }

      // TODO: 这里添加实际提交逻辑
      console.log("提交误报反馈:", {
        studentId: this.studentId,
        modelId: this.modelId,
        reason: this.feedbackForm.reason,
      });

      this.feedbackDialogVisible = false;
      this.$message.success("反馈提交成功，我们会尽快处理！");
    },
    goToSuggestion() {
      this.$router.push({
        path: "/graduate-detection/analysis/student-detail/suggestion",
        query: {
          modelId: this.modelId,
          studentId: this.studentId,
          warningType: this.warningType,
          warningScore: this.warningScore,
          color: this.color,
          name: this.studentInfo.name,
          college: this.studentInfo.college,
          major: this.studentInfo.major,
          age: this.studentInfo.age,
          grade: this.studentInfo.grade,
          phone: this.studentInfo.phone,
          gender: this.studentInfo.gender,
        },
      });
    },
    getWarningDescription(score) {
      const numScore = parseFloat(score);
      if (numScore >= 80) {
        return "该生存在较高的风险，建议立即采取干预措施，重点关注其学业、生活和心理状态";
      }
      if (numScore >= 60) {
        return "该生存在一定的风险，建议加强关注，定期了解其学习生活情况";
      }
      if (numScore >= 40) {
        return "该生存在轻微风险，建议保持关注，适时提供必要的帮助和指导";
      }
      return "该生各项指标正常，建议继续保持关注";
    },
  },
};
</script>

<style lang="scss" scoped>
.header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding: 0 20px;

  .title {
    flex: 1;
    margin: 0;
    color: #303133;
    font-size: 24px;
    text-align: left;
  }

  .model-info {
    margin: 0 30px;
    color: #606266;
    font-size: 16px;
    min-width: 200px;
    text-align: center;
  }

  .back-btn {
    width: 140px;
    ::v-deep .el-icon-arrow-left {
      margin-right: 5px;
      transform: rotate(180deg);
    }
  }
}

.info-card {
  margin-bottom: 20px;
  .info-row {
    display: flex;
    align-items: center;
    margin: 15px 0;
    line-height: 1.6;

    .label {
      width: 80px;
      color: #909399;
      flex-shrink: 0;
    }
  }
}

.warning-card {
  .warning-level {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    gap: 10px;
    width: 100%;
    margin-bottom: 20px;

    .warning-description {
      color: #606266;
      font-size: 14px;
      margin-left: 5px;
    }

    .button-group {
      margin-left: auto;
      display: flex;
      gap: 10px;
    }

    .el-tag {
      margin: 0 !important;
      flex-shrink: 0;

      &--danger {
        color: #fff;
        background-color: #f56c6c;
        border-color: #f56c6c;
      }
      &--warning {
        color: #fff;
        background-color: #e6a23c;
        border-color: #e6a23c;
      }
      &--primary {
        color: #fff;
        background-color: #409eff;
        border-color: #409eff;
      }
    }
  }
}

.scroll-container {
  max-height: 400px;
  overflow: auto;
}

.photo-container {
  display: flex;
  justify-content: flex-end;
  margin-top: -170px;
}

.id-photo {
  width: 120px;
  height: 160px;
  object-fit: cover;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.highlight-value {
  color: #f56c6c;
  font-weight: bold;
}
</style>
