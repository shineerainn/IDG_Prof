<template>
  <div class="academic-info-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="title-group">
          <h1 class="page-title">
            <i class="el-icon-document"></i>
            研究生学业详细信息
          </h1>
          <p class="page-subtitle">全面展示学生的学术表现与科研成果</p>
        </div>
        <el-button type="primary" class="back-btn" @click="goBack">
          <i class="el-icon-arrow-left"></i>
          返回列表
        </el-button>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="content-wrapper">
      <el-card class="main-card" shadow="never">
        <!-- 基本信息区域 -->
        <div class="info-section">
          <div class="section-header">
            <div class="section-title">
              <div class="title-icon">
                <i class="el-icon-user"></i>
              </div>
              <h3>基本信息</h3>
            </div>
          </div>
          <div class="info-grid">
            <div class="info-item">
              <div class="item-icon">
                <i class="el-icon-user-solid"></i>
              </div>
              <div class="item-content">
                <span class="item-label">姓名</span>
                <span class="item-value">{{ academicInfo.graduateName }}</span>
              </div>
            </div>
            <div class="info-item">
              <div class="item-icon">
                <i class="el-icon-s-management"></i>
              </div>
              <div class="item-content">
                <span class="item-label">学号</span>
                <span class="item-value">{{ academicInfo.studentId }}</span>
              </div>
            </div>
            <div class="info-item">
              <div class="item-icon">
                <i class="el-icon-s-custom"></i>
              </div>
              <div class="item-content">
                <span class="item-label">学生类型</span>
                <span class="item-value">{{ academicInfo.studentType }}</span>
              </div>
            </div>
            <div class="info-item">
              <div class="item-icon">
                <i class="el-icon-s-marketing"></i>
              </div>
              <div class="item-content">
                <span class="item-label">学分完成率</span>
                <span class="item-value highlight">
                  {{
                    academicInfo.creditCompletionRate
                      ? (academicInfo.creditCompletionRate * 100).toFixed(2) +
                        "%"
                      : "-"
                  }}
                </span>
              </div>
            </div>
          </div>
        </div>

        <!-- 学业表现评分 -->
        <div class="info-section">
          <div class="section-header">
            <div class="section-title">
              <div class="title-icon">
                <i class="el-icon-medal"></i>
              </div>
              <h3>学业表现评分</h3>
            </div>
          </div>
          <div class="score-grid">
            <div class="score-item">
              <div class="score-icon">
                <i class="el-icon-trophy"></i>
              </div>
              <div class="score-content">
                <span class="score-label">学业表现评分</span>
                <span class="score-value primary">
                  {{
                    academicInfo.academic_performance_score ||
                    academicInfo.academicPerformanceScore
                  }}
                </span>
              </div>
            </div>
            <div class="score-item">
              <div class="score-icon">
                <i class="el-icon-s-opportunity"></i>
              </div>
              <div class="score-content">
                <span class="score-label">科研项目评分</span>
                <span class="score-value primary">
                  {{
                    academicInfo.research_project_score ||
                    academicInfo.researchProjectScore
                  }}
                </span>
              </div>
            </div>
            <div class="score-item">
              <div class="score-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="score-content">
                <span class="score-label">学位论文评分</span>
                <span class="score-value primary">
                  {{ academicInfo.thesis_score || academicInfo.thesisScore }}
                </span>
              </div>
            </div>
            <div class="score-item">
              <div class="score-icon">
                <i class="el-icon-reading"></i>
              </div>
              <div class="score-content">
                <span class="score-label">学术论文评分</span>
                <span class="score-value primary">
                  {{
                    academicInfo.academic_paper_score ||
                    academicInfo.academicPaperScore
                  }}
                </span>
              </div>
            </div>
            <div class="score-item">
              <div class="score-icon">
                <i class="el-icon-medal"></i>
              </div>
              <div class="score-content">
                <span class="score-label">竞赛获奖评分</span>
                <span class="score-value primary">
                  {{
                    academicInfo.competition_score ||
                    academicInfo.competitionScore
                  }}
                </span>
              </div>
            </div>
            <div class="score-item">
              <div class="score-icon">
                <i class="el-icon-copy-document"></i>
              </div>
              <div class="score-content">
                <span class="score-label">专利软著评分</span>
                <span class="score-value primary">
                  {{
                    academicInfo.patent_software_score ||
                    academicInfo.patentSoftwareScore
                  }}
                </span>
              </div>
            </div>
            <div class="score-item total-score">
              <div class="score-icon">
                <i class="el-icon-star-on"></i>
              </div>
              <div class="score-content">
                <span class="score-label">综合评分</span>
                <span class="score-value success">
                  {{ academicInfo.academicEvaluationScore }}
                </span>
              </div>
            </div>
          </div>
        </div>

        <!-- 学业基础数据 -->
        <div class="info-section">
          <div class="section-header">
            <div class="section-title">
              <div class="title-icon">
                <i class="el-icon-data-analysis"></i>
              </div>
              <h3>学业基础数据</h3>
            </div>
          </div>
          <div class="data-grid">
            <div class="data-item">
              <div class="data-icon">
                <i class="el-icon-coin"></i>
              </div>
              <div class="data-content">
                <span class="data-label">应修学分</span>
                <span class="data-value">{{
                  academicInfo.requiredCredits || "-"
                }}</span>
              </div>
            </div>
            <div class="data-item">
              <div class="data-icon">
                <i class="el-icon-success"></i>
              </div>
              <div class="data-content">
                <span class="data-label">已修学分</span>
                <span class="data-value">{{
                  academicInfo.earnedCredits || "-"
                }}</span>
              </div>
            </div>
            <div class="data-item">
              <div class="data-icon">
                <i class="el-icon-coin"></i>
              </div>
              <div class="data-content">
                <span class="data-label">总应修学分</span>
                <span class="data-value">{{
                  academicInfo.totalRequiredCredits || "-"
                }}</span>
              </div>
            </div>
            <div class="data-item">
              <div class="data-icon">
                <i class="el-icon-success"></i>
              </div>
              <div class="data-content">
                <span class="data-label">总已修学分</span>
                <span class="data-value">{{
                  academicInfo.totalEarnedCredits || "-"
                }}</span>
              </div>
            </div>
            <div class="data-item">
              <div class="data-icon">
                <i class="el-icon-data-line"></i>
              </div>
              <div class="data-content">
                <span class="data-label">总学分完成率</span>
                <span class="data-value highlight">
                  {{
                    academicInfo.totalCreditCompletionRate
                      ? (academicInfo.totalCreditCompletionRate * 100).toFixed(
                          2,
                        ) + "%"
                      : "-"
                  }}
                </span>
              </div>
            </div>
          </div>
        </div>

        <!-- 科研项目详情 -->
        <div class="info-section">
          <div class="section-header">
            <div class="section-title">
              <div class="title-icon">
                <i class="el-icon-s-management"></i>
              </div>
              <h3>科研项目详情</h3>
            </div>
          </div>
          <div class="detail-grid">
            <div class="detail-item">
              <div class="detail-icon">
                <i class="el-icon-s-data"></i>
              </div>
              <div class="detail-content">
                <span class="detail-label">科研项目数量</span>
                <span class="detail-value">{{
                  academicInfo.researchProjectCount || "-"
                }}</span>
              </div>
            </div>
            <div class="detail-item">
              <div class="detail-icon">
                <i class="el-icon-s-flag"></i>
              </div>
              <div class="detail-content">
                <span class="detail-label">科研项目级别</span>
                <span class="detail-value">
                  {{
                    academicInfo.researchProjectLevel ||
                    academicInfo.research_project_level ||
                    "-"
                  }}
                </span>
              </div>
            </div>
            <div class="detail-item">
              <div class="detail-icon">
                <i class="el-icon-sort"></i>
              </div>
              <div class="detail-content">
                <span class="detail-label">科研排名</span>
                <span class="detail-value">{{
                  academicInfo.researchProjectRanking || "-"
                }}</span>
              </div>
            </div>
            <div class="detail-item">
              <div class="detail-icon">
                <i class="el-icon-s-custom"></i>
              </div>
              <div class="detail-content">
                <span class="detail-label">项目角色</span>
                <span class="detail-value">{{
                  academicInfo.projectRole || "-"
                }}</span>
              </div>
            </div>
            <div class="detail-item">
              <div class="detail-icon">
                <i class="el-icon-s-promotion"></i>
              </div>
              <div class="detail-content">
                <span class="detail-label">成果转化</span>
                <span class="detail-value">{{
                  academicInfo.projectOutcomeTransformation || "-"
                }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 学位论文详情 -->
        <div class="info-section">
          <div class="section-header">
            <div class="section-title">
              <div class="title-icon">
                <i class="el-icon-document"></i>
              </div>
              <h3>学位论文详情</h3>
            </div>
          </div>
          <div class="detail-grid">
            <div class="detail-item">
              <div class="detail-icon">
                <i class="el-icon-edit-outline"></i>
              </div>
              <div class="detail-content">
                <span class="detail-label">开题状态</span>
                <span class="detail-value">{{
                  academicInfo.proposalStatus || "-"
                }}</span>
              </div>
            </div>
            <div class="detail-item">
              <div class="detail-icon">
                <i class="el-icon-time"></i>
              </div>
              <div class="detail-content">
                <span class="detail-label">中期状态</span>
                <span class="detail-value">{{
                  academicInfo.midtermStatus || "-"
                }}</span>
              </div>
            </div>
            <div class="detail-item">
              <div class="detail-icon">
                <i class="el-icon-s-check"></i>
              </div>
              <div class="detail-content">
                <span class="detail-label">评审状态</span>
                <span class="detail-value">{{
                  academicInfo.reviewStatus || "-"
                }}</span>
              </div>
            </div>
            <div class="detail-item">
              <div class="detail-icon">
                <i class="el-icon-s-claim"></i>
              </div>
              <div class="detail-content">
                <span class="detail-label">答辩成绩</span>
                <span class="detail-value">{{
                  academicInfo.defenseGrade || "-"
                }}</span>
              </div>
            </div>
            <div class="detail-item">
              <div class="detail-icon">
                <i class="el-icon-star-on"></i>
              </div>
              <div class="detail-content">
                <span class="detail-label">优秀论文</span>
                <span class="detail-value">{{
                  academicInfo.outstandingThesis || "-"
                }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 学术论文详情 -->
        <div class="info-section">
          <div class="section-header">
            <div class="section-title">
              <div class="title-icon">
                <i class="el-icon-reading"></i>
              </div>
              <h3>学术论文详情</h3>
            </div>
          </div>
          <div class="detail-grid">
            <div class="detail-item">
              <div class="detail-icon">
                <i class="el-icon-document-copy"></i>
              </div>
              <div class="detail-content">
                <span class="detail-label">论文数量</span>
                <span class="detail-value">{{
                  academicInfo.academicPaperCount || "-"
                }}</span>
              </div>
            </div>
            <div class="detail-item">
              <div class="detail-icon">
                <i class="el-icon-s-platform"></i>
              </div>
              <div class="detail-content">
                <span class="detail-label">发表 venue 级别</span>
                <span class="detail-value">{{
                  academicInfo.publicationVenueLevel || "-"
                }}</span>
              </div>
            </div>
            <div class="detail-item">
              <div class="detail-icon">
                <i class="el-icon-s-custom"></i>
              </div>
              <div class="detail-content">
                <span class="detail-label">作者角色</span>
                <span class="detail-value">{{
                  academicInfo.authorRole || "-"
                }}</span>
              </div>
            </div>
            <div class="detail-item">
              <div class="detail-icon">
                <i class="el-icon-s-opportunity"></i>
              </div>
              <div class="detail-content">
                <span class="detail-label">高被引论文</span>
                <span class="detail-value">{{
                  academicInfo.highlyCited || "-"
                }}</span>
              </div>
            </div>
            <div class="detail-item">
              <div class="detail-icon">
                <i class="el-icon-s-marketing"></i>
              </div>
              <div class="detail-content">
                <span class="detail-label">影响因子10以上</span>
                <span class="detail-value">{{
                  academicInfo.impactFactorAbove10 || "-"
                }}</span>
              </div>
            </div>
            <div class="detail-item">
              <div class="detail-icon">
                <i class="el-icon-picture-outline"></i>
              </div>
              <div class="detail-content">
                <span class="detail-label">封面论文</span>
                <span class="detail-value">{{
                  academicInfo.coverPaper || "-"
                }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 竞赛详情 -->
        <div class="info-section">
          <div class="section-header">
            <div class="section-title">
              <div class="title-icon">
                <i class="el-icon-s-flag"></i>
              </div>
              <h3>竞赛详情</h3>
            </div>
          </div>
          <div class="detail-grid">
            <div class="detail-item">
              <div class="detail-icon">
                <i class="el-icon-medal-1"></i>
              </div>
              <div class="detail-content">
                <span class="detail-label">获奖数量</span>
                <span class="detail-value">{{
                  academicInfo.competitionAwardCount || "-"
                }}</span>
              </div>
            </div>
            <div class="detail-item">
              <div class="detail-icon">
                <i class="el-icon-s-flag"></i>
              </div>
              <div class="detail-content">
                <span class="detail-label">竞赛级别</span>
                <span class="detail-value">{{
                  academicInfo.competitionLevel || "-"
                }}</span>
              </div>
            </div>
            <div class="detail-item">
              <div class="detail-icon">
                <i class="el-icon-trophy-1"></i>
              </div>
              <div class="detail-content">
                <span class="detail-label">获奖级别</span>
                <span class="detail-value">{{
                  academicInfo.awardLevel || "-"
                }}</span>
              </div>
            </div>
            <div class="detail-item">
              <div class="detail-icon">
                <i class="el-icon-s-promotion"></i>
              </div>
              <div class="detail-content">
                <span class="detail-label">竞赛范围</span>
                <span class="detail-value">{{
                  academicInfo.competitionScope || "-"
                }}</span>
              </div>
            </div>
            <div class="detail-item">
              <div class="detail-icon">
                <i class="el-icon-s-custom"></i>
              </div>
              <div class="detail-content">
                <span class="detail-label">团队角色</span>
                <span class="detail-value">{{
                  academicInfo.competitionTeamRole || "-"
                }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 专利软著详情 -->
        <div class="info-section">
          <div class="section-header">
            <div class="section-title">
              <div class="title-icon">
                <i class="el-icon-copy-document"></i>
              </div>
              <h3>专利软著详情</h3>
            </div>
          </div>
          <div class="detail-grid">
            <div class="detail-item">
              <div class="detail-icon">
                <i class="el-icon-files"></i>
              </div>
              <div class="detail-content">
                <span class="detail-label">专利数量</span>
                <span class="detail-value">{{
                  academicInfo.patentSoftwareCount || "-"
                }}</span>
              </div>
            </div>
            <div class="detail-item">
              <div class="detail-icon">
                <i class="el-icon-s-grid"></i>
              </div>
              <div class="detail-content">
                <span class="detail-label">专利类型</span>
                <span class="detail-value">{{
                  academicInfo.patentSoftwareType || "-"
                }}</span>
              </div>
            </div>
            <div class="detail-item">
              <div class="detail-icon">
                <i class="el-icon-location"></i>
              </div>
              <div class="detail-content">
                <span class="detail-label">专利范围</span>
                <span class="detail-value">{{
                  academicInfo.patentSoftwareScope || "-"
                }}</span>
              </div>
            </div>
            <div class="detail-item">
              <div class="detail-icon">
                <i class="el-icon-s-custom"></i>
              </div>
              <div class="detail-content">
                <span class="detail-label">团队角色</span>
                <span class="detail-value">{{
                  academicInfo.patentSoftwareTeamRole || "-"
                }}</span>
              </div>
            </div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
// import { getStudentAcademic } from "@/api/academicAnalysis";

export default {
  name: "AcademicInformationDetailView",
  data() {
    return {
      academicInfo: {},
      loading: false,
    };
  },
  computed: {
    // 统一详细数据字段，兼容不同命名与后端/路由来源
    detailGpa() {
      return this.academicInfo.gpa ?? this.academicInfo.GPA ?? null;
    },
    detailTotalCredits() {
      return (
        this.academicInfo.totalCredits ??
        this.academicInfo.total_credits ??
        null
      );
    },
    detailResearchProjects() {
      return (
        this.academicInfo.researchProjects ??
        this.academicInfo.research_projects ??
        this.academicInfo.researchProjectCount ??
        null
      );
    },
    detailPapers() {
      return (
        this.academicInfo.papers ??
        this.academicInfo.paperCount ??
        this.academicInfo.papers_count ??
        null
      );
    },
    detailCompetitions() {
      return (
        this.academicInfo.competitions ??
        this.academicInfo.competitionCount ??
        this.academicInfo.competitions_count ??
        null
      );
    },
    detailPatents() {
      return (
        this.academicInfo.patents ??
        this.academicInfo.patentCount ??
        this.academicInfo.patents_count ??
        null
      );
    },
  },
  mounted() {
    this.loadAcademicData();
  },
  methods: {
    // 统一字段解析：优先驼峰，其次下划线；若都无则返回 "-"
    resolveField(camelKey, snakeKey) {
      const v = this.academicInfo[camelKey];
      if (v !== undefined && v !== null && v !== "") return v;
      const s = this.academicInfo[snakeKey];
      if (s !== undefined && s !== null && s !== "") return s;
      return "-";
    },
    async loadAcademicData() {
      const studentId = this.$route.params.studentId;
      if (!studentId) {
        this.$message.error("学生ID不能为空");
        return;
      }

      this.loading = true;
      try {
        // 调用新接口获取学业数据
        const response = await fetch(
          `http://localhost:8088/api/student-academic-performance/${studentId}/2025/2`,
        );
        if (response.ok) {
          const data = await response.json();
          this.academicInfo = data;
          console.log("从新接口加载学业数据:", this.academicInfo);
        } else {
          console.log("新接口调用失败，尝试从路由参数获取数据");
          // API失败时，尝试从路由参数获取数据
          const studentData = this.$route.query.studentData;
          if (studentData) {
            try {
              this.academicInfo = JSON.parse(studentData);
              console.log("从路由参数加载学生数据:", this.academicInfo);
            } catch (error) {
              console.error("解析路由参数数据失败:", error);
              this.loadDemoData();
            }
          } else {
            this.$message.error("加载学业信息失败");
            this.loadDemoData();
          }
        }
      } catch (error) {
        console.error("加载学业详情失败:", error);
        // API调用失败时，尝试从路由参数获取数据
        const studentData = this.$route.query.studentData;
        if (studentData) {
          try {
            this.academicInfo = JSON.parse(studentData);
            console.log("从路由参数加载学生数据:", this.academicInfo);
          } catch (parseError) {
            console.error("解析路由参数数据失败:", parseError);
            this.loadDemoData();
          }
        } else {
          this.loadDemoData();
        }
      } finally {
        this.loading = false;
      }
    },
    loadDemoData() {
      // 模拟PgAcademic数据库中的数据
      this.academicInfo = {
        graduateName: "张三",
        studentId: this.$route.params.studentId,
        gpa: 3.85,
        totalCredits: 45,
        researchProjects: 3,
        papers: 2,
        competitions: 1,
        patents: 0,
        // 学业表现评分字段 - 使用与列表页面一致的字段名
        academicPerformanceScore: 85.5,
        researchProjectScore: 78.2,
        thesisScore: 82.1,
        academicPaperScore: 76.8,
        competitionScore: 88.3,
        patentSoftwareScore: 65.4,
        otherInnovationAchievementScore: 72.9,
        // 同时保留下划线格式以兼容现有显示
        academic_performance_score: 85.5,
        research_project_score: 78.2,
        thesis_score: 82.1,
        academic_paper_score: 76.8,
        competition_score: 88.3,
        patent_software_score: 65.4,
        other_innovation_achievement_score: 72.9,
      };
      console.log("加载演示数据:", this.academicInfo);
    },
    calculateTotalScore() {
      // 支持两种字段格式
      const scores = [
        this.academicInfo.academic_performance_score ||
          this.academicInfo.academicPerformanceScore,
        this.academicInfo.research_project_score ||
          this.academicInfo.researchProjectScore,
        this.academicInfo.thesis_score || this.academicInfo.thesisScore,
        this.academicInfo.academic_paper_score ||
          this.academicInfo.academicPaperScore,
        this.academicInfo.competition_score ||
          this.academicInfo.competitionScore,
        this.academicInfo.patent_software_score ||
          this.academicInfo.patentSoftwareScore,
        this.academicInfo.other_innovation_achievement_score ||
          this.academicInfo.otherInnovationAchievementScore,
      ];
      const total = scores.reduce((sum, score) => sum + (score || 0), 0);
      return (total / 7).toFixed(1);
    },
    getGrade() {
      const totalScore = parseFloat(this.calculateTotalScore());
      if (totalScore >= 90) return "优秀";
      if (totalScore >= 80) return "良好";
      if (totalScore >= 70) return "中等";
      if (totalScore >= 60) return "及格";
      return "不及格";
    },
    getGradeClass() {
      const grade = this.getGrade();
      const classMap = {
        优秀: "grade-excellent",
        良好: "grade-good",
        中等: "grade-average",
        及格: "grade-pass",
        不及格: "grade-fail",
      };
      return classMap[grade] || "";
    },

    goBack() {
      this.$router.push({ path: "/academic-analysis/information" });
    },
  },
};
</script>

<style scoped lang="scss">
.academic-info-container {
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e7ed 100%);
  height: 100vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;

  .page-header {
    background: white;
    padding: 20px 24px;
    border-bottom: 1px solid #e4e7ed;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
    flex-shrink: 0;

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .title-group {
        .page-title {
          margin: 0 0 8px 0;
          font-size: 24px;
          font-weight: 600;
          color: #303133;
          display: flex;
          align-items: center;
          gap: 12px;

          i {
            color: #409eff;
            font-size: 28px;
          }
        }

        .page-subtitle {
          margin: 0;
          color: #909399;
          font-size: 14px;
        }
      }

      .back-btn {
        padding: 10px 20px;
        border-radius: 8px;
        font-weight: 500;

        i {
          margin-right: 6px;
        }
      }
    }
  }

  .content-wrapper {
    flex: 1;
    overflow-y: auto;
    padding: 24px;

    // 自定义滚动条样式，与sidebar保持一致
    &::-webkit-scrollbar {
      width: 6px;
    }

    &::-webkit-scrollbar-track {
      background: rgba(255, 255, 255, 0.1);
      border-radius: 3px;
    }

    &::-webkit-scrollbar-thumb {
      background: rgba(64, 158, 255, 0.3);
      border-radius: 3px;
    }

    &::-webkit-scrollbar-thumb:hover {
      background: rgba(64, 158, 255, 0.5);
    }

    .main-card {
      border-radius: 12px;
      border: none;
      background: #ffffff;
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
      padding: 24px;

      .info-section {
        margin-bottom: 32px;
        padding: 24px;
        border-radius: 8px;
        background: #fafbfc;
        border: 1px solid #ebeef5;

        &:last-child {
          margin-bottom: 0;
        }

        .section-header {
          margin-bottom: 24px;

          .section-title {
            display: flex;
            align-items: center;
            gap: 12px;
            margin-bottom: 16px;

            .title-icon {
              width: 40px;
              height: 40px;
              background: linear-gradient(135deg, #409eff, #67c23a);
              border-radius: 8px;
              display: flex;
              align-items: center;
              justify-content: center;

              i {
                color: white;
                font-size: 20px;
              }
            }

            h3 {
              margin: 0;
              font-size: 20px;
              font-weight: 600;
              color: #303133;
            }
          }
        }

        // 基本信息网格布局
        .info-grid {
          display: grid;
          grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
          gap: 20px;

          .info-item {
            display: flex;
            align-items: center;
            gap: 16px;
            padding: 20px;
            background: white;
            border-radius: 8px;
            border: 1px solid #e4e7ed;
            transition: all 0.3s ease;

            &:hover {
              transform: translateY(-2px);
              box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
              border-color: #409eff;
            }

            .item-icon {
              width: 48px;
              height: 48px;
              background: linear-gradient(135deg, #f0f9ff, #e6f7ff);
              border-radius: 8px;
              display: flex;
              align-items: center;
              justify-content: center;

              i {
                color: #409eff;
                font-size: 24px;
              }
            }

            .item-content {
              flex: 1;

              .item-label {
                display: block;
                font-size: 14px;
                color: #909399;
                margin-bottom: 4px;
              }

              .item-value {
                display: block;
                font-size: 18px;
                font-weight: 600;
                color: #303133;

                &.highlight {
                  color: #67c23a;
                  font-weight: 700;
                }
              }
            }
          }
        }

        // 评分网格布局
        .score-grid {
          display: grid;
          grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
          gap: 16px;

          .score-item {
            display: flex;
            align-items: center;
            gap: 16px;
            padding: 20px;
            background: white;
            border-radius: 8px;
            border: 1px solid #e4e7ed;
            transition: all 0.3s ease;

            &:hover {
              transform: translateY(-2px);
              box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            }

            &.total-score {
              background: linear-gradient(135deg, #f0f9ff, #e6f7ff);
              border-color: #409eff;

              .score-icon {
                background: linear-gradient(135deg, #409eff, #67c23a);
              }
            }

            .score-icon {
              width: 48px;
              height: 48px;
              background: linear-gradient(135deg, #409eff, #79bbff);
              border-radius: 8px;
              display: flex;
              align-items: center;
              justify-content: center;

              i {
                color: white;
                font-size: 24px;
              }
            }

            .score-content {
              flex: 1;

              .score-label {
                display: block;
                font-size: 14px;
                color: #909399;
                margin-bottom: 6px;
              }

              .score-value {
                display: block;
                font-size: 24px;
                font-weight: 700;

                &.primary {
                  color: #409eff;
                }

                &.success {
                  color: #67c23a;
                }
              }
            }
          }
        }

        // 数据网格布局
        .data-grid {
          display: grid;
          grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
          gap: 16px;

          .data-item {
            display: flex;
            align-items: center;
            gap: 12px;
            padding: 16px;
            background: white;
            border-radius: 8px;
            border: 1px solid #e4e7ed;

            .data-icon {
              width: 40px;
              height: 40px;
              background: linear-gradient(135deg, #f0f9ff, #e6f7ff);
              border-radius: 6px;
              display: flex;
              align-items: center;
              justify-content: center;

              i {
                color: #409eff;
                font-size: 20px;
              }
            }

            .data-content {
              .data-label {
                display: block;
                font-size: 13px;
                color: #909399;
                margin-bottom: 4px;
              }

              .data-value {
                display: block;
                font-size: 16px;
                font-weight: 600;
                color: #303133;

                &.highlight {
                  color: #67c23a;
                  font-weight: 700;
                }
              }
            }
          }
        }

        // 详情网格布局
        .detail-grid {
          display: grid;
          grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
          gap: 16px;

          .detail-item {
            display: flex;
            align-items: center;
            gap: 16px;
            padding: 16px 20px;
            background: white;
            border-radius: 8px;
            border: 1px solid #e4e7ed;
            transition: all 0.2s ease;

            &:hover {
              border-color: #409eff;
              background: #f0f9ff;
            }

            .detail-icon {
              width: 40px;
              height: 40px;
              background: linear-gradient(135deg, #f0f9ff, #e6f7ff);
              border-radius: 6px;
              display: flex;
              align-items: center;
              justify-content: center;

              i {
                color: #409eff;
                font-size: 20px;
              }
            }

            .detail-content {
              flex: 1;
              display: flex;
              justify-content: space-between;
              align-items: center;

              .detail-label {
                font-size: 14px;
                color: #606266;
                font-weight: 500;
              }

              .detail-value {
                font-size: 14px;
                color: #303133;
                font-weight: 600;
              }
            }
          }
        }
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .academic-info-container {
    .page-header {
      padding: 16px;

      .header-content {
        flex-direction: column;
        gap: 16px;
        align-items: flex-start;

        .title-group {
          .page-title {
            font-size: 20px;
          }
        }
      }
    }

    .content-wrapper {
      padding: 16px;

      .main-card {
        padding: 16px;

        .info-section {
          padding: 16px;

          .info-grid,
          .score-grid,
          .data-grid,
          .detail-grid {
            grid-template-columns: 1fr;
          }
        }
      }
    }
  }
}
</style>
