<template>
  <div class="academic-analysis">
    <div class="page-header">
      <h2>研究生学业分析</h2>
      <div class="header-actions">
        <el-select
          v-model="selectedStudentId"
          placeholder="选择学生"
          @change="loadStudentData"
        >
          <el-option
            v-for="student in studentList"
            :key="student.studentId"
            :label="`${student.graduateName} (${student.studentId})`"
            :value="student.studentId"
          />
        </el-select>
        <el-button
          type="primary"
          @click="calculateScores"
          :loading="calculating"
        >
          计算分数
        </el-button>
        <el-button type="success" @click="loadDemoData">
          加载演示数据
        </el-button>
        <el-button type="info" @click="refreshRadarChart">
          刷新雷达图
        </el-button>
        <el-button type="warning" @click="testRadarChart">
          测试雷达图
        </el-button>
      </div>
    </div>

    <div class="content-container">
      <!-- 雷达图展示 -->
      <div class="radar-chart-section">
        <AcademicRadarChart :radarData="radarChartData" />
      </div>

      <!-- 折线图 -->
      <div class="radar-chart-section">
        <AcademicLineChart :lineData="radarChartData" />
      </div>

      <!-- 详细分析 -->
      <div class="analysis-section" v-if="studentData">
        <el-card class="analysis-card">
          <template #header>
            <div class="card-header">
              <span>学生基本信息</span>
            </div>
          </template>
          <div class="student-info-grid">
            <div class="info-item">
              <label>姓名：</label>
              <span>{{ studentData.graduateName }}</span>
            </div>
            <div class="info-item">
              <label>学号：</label>
              <span>{{ studentData.studentId }}</span>
            </div>
            <div class="info-item">
              <label>专业：</label>
              <span>{{ studentData.major || "计算机科学与技术" }}</span>
            </div>
            <div class="info-item">
              <label>导师：</label>
              <span>{{ studentData.supervisor || "张教授" }}</span>
            </div>
          </div>
        </el-card>

        <!--        <el-card class="analysis-card">-->
        <!--          <template #header>-->
        <!--            <div class="card-header">-->
        <!--              <span>学业各项指标得分</span>-->
        <!--            </div>-->
        <!--          </template>-->
        <!--          <div class="score-grid">-->
        <!--            <div-->
        <!--              class="score-item"-->
        <!--              v-for="score in scoreItems"-->
        <!--              :key="score.key"-->
        <!--            >-->
        <!--              <div class="score-label">{{ score.name }}</div>-->
        <!--              <div class="score-value">{{ getScoreValue(score.key) }}</div>-->
        <!--              <div class="score-bar">-->
        <!--                <div-->
        <!--                  class="score-progress"-->
        <!--                  :style="{ width: getScorePercentage(score.key) + '%' }"-->
        <!--                ></div>-->
        <!--              </div>-->
        <!--            </div>-->
        <!--          </div>-->
        <!--        </el-card>-->

        <el-card class="analysis-card">
          <template #header>
            <div class="card-header">
              <span>学业综合评估</span>
            </div>
          </template>
          <div class="evaluation-content">
            <div class="evaluation-item">
              <label>综合得分：</label>
              <span class="total-score">{{ calculateTotalScore() }}</span>
            </div>
            <div class="evaluation-item">
              <label>学业等级：</label>
              <span class="grade-badge" :class="getGradeClass()">
                {{ getGradeText() }}
              </span>
            </div>
            <div class="evaluation-item">
              <label>评估建议：</label>
              <span>{{ getEvaluationSuggestion() }}</span>
            </div>
          </div>
        </el-card>

        <!--        <el-card class="analysis-card">-->
        <!--          <template #header>-->
        <!--            <div class="card-header">-->
        <!--              <span>学业能力分析</span>-->
        <!--            </div>-->
        <!--          </template>-->
        <!--          <div class="analysis-content">-->
        <!--            <div class="strength-section">-->
        <!--              <h4>优势能力</h4>-->
        <!--              <ul>-->
        <!--                <li v-for="strength in getStrengths()" :key="strength">-->
        <!--                  {{ strength }}-->
        <!--                </li>-->
        <!--              </ul>-->
        <!--            </div>-->
        <!--            <div class="improvement-section">-->
        <!--              <h4>改进建议</h4>-->
        <!--              <ul>-->
        <!--                <li v-for="improvement in getImprovements()" :key="improvement">-->
        <!--                  {{ improvement }}-->
        <!--                </li>-->
        <!--              </ul>-->
        <!--            </div>-->
        <!--          </div>-->
        <!--        </el-card>-->

        <!--        <el-card class="analysis-card">-->
        <!--          <template #header>-->
        <!--            <div class="card-header">-->
        <!--              <span>同年级对比分析</span>-->
        <!--            </div>-->
        <!--          </template>-->
        <!--          <div class="comparison-content">-->
        <!--            <el-table :data="comparisonData" style="width: 100%">-->
        <!--              <el-table-column prop="category" label="指标" width="150" />-->
        <!--              <el-table-column-->
        <!--                prop="studentScore"-->
        <!--                label="学生得分"-->
        <!--                width="120"-->
        <!--              />-->
        <!--              <el-table-column prop="averageScore" label="平均分" width="120" />-->
        <!--              <el-table-column prop="difference" label="差值" width="120" />-->
        <!--              <el-table-column prop="percentage" label="百分比" width="120" />-->
        <!--              <el-table-column prop="evaluation" label="评价" />-->
        <!--            </el-table>-->
        <!--          </div>-->
        <!--        </el-card>-->
      </div>
    </div>
  </div>
</template>

<script>
import AcademicRadarChart from "@/components/AcademicRadarChart.vue";
import AcademicLineChart from "@/components/AcademicLineChart.vue";
import {
  getAllStudents,
  calculateStudentScores,
  getStudentRadarChartData,
} from "@/api/academicAnalysis";

export default {
  name: "AcademicAnalysisView",
  components: {
    AcademicRadarChart,
    AcademicLineChart,
  },
  data() {
    return {
      selectedStudentId: "",
      studentList: [],
      studentData: null,
      radarChartData: null,
      calculating: false,
      scoreItems: [
        { key: "academicPerformanceScore", name: "学业表现" },
        { key: "researchProjectScore", name: "科研项目" },
        { key: "thesisScore", name: "论文质量" },
        { key: "academicPaperScore", name: "学术论文" },
        { key: "competitionScore", name: "竞赛获奖" },
        { key: "patentSoftwareScore", name: "专利软件" },
        { key: "otherInnovationAchievementScore", name: "创新成果" },
      ],
      comparisonData: [],
    };
  },
  async mounted() {
    await this.loadStudentList();
  },
  methods: {
    normalizeScore(source, camelKey, snakeKey) {
      const v = source?.[camelKey];
      if (v !== undefined && v !== null && v !== "") return Number(v) || 0;
      const s = source?.[snakeKey];
      if (s !== undefined && s !== null && s !== "") return Number(s) || 0;
      return 0;
    },
    async loadStudentList() {
      try {
        const response = await getAllStudents();
        console.log("学生列表API响应:", response);
        if (response.code === 200) {
          this.studentList = response.data || [];
          if (this.studentList.length > 0) {
            this.selectedStudentId = this.studentList[0].studentId;
            await this.loadStudentData();
          } else {
            console.log("学生列表为空，加载演示数据");
            this.loadDemoData();
          }
        } else {
          console.log("学生列表API返回错误，加载演示数据");
          this.loadDemoData();
        }
      } catch (error) {
        console.error("加载学生列表失败:", error);
        this.$message.warning("无法连接到服务器，使用演示数据");
        this.loadDemoData();
      }
    },

    async loadStudentData() {
      if (!this.selectedStudentId) return;

      try {
        const response = await getStudentRadarChartData(this.selectedStudentId);
        console.log("API响应:", response);

        if (response.code === 200) {
          const studentInfo = this.findStudentById(this.selectedStudentId);
          console.log("API返回的数据:", response.data);
          const apiData = response.data || {};
          const apiStudent = apiData.studentScores || apiData;
          const apiAvg = apiData.averageScores || {};

          const studentScores = {
            academicPerformanceScore:
              this.normalizeScore(
                apiStudent,
                "academicPerformanceScore",
                "academic_performance_score",
              ) ||
              this.normalizeScore(
                studentInfo,
                "academicPerformanceScore",
                "academic_performance_score",
              ),
            researchProjectScore:
              this.normalizeScore(
                apiStudent,
                "researchProjectScore",
                "research_project_score",
              ) ||
              this.normalizeScore(
                studentInfo,
                "researchProjectScore",
                "research_project_score",
              ),
            thesisScore:
              this.normalizeScore(apiStudent, "thesisScore", "thesis_score") ||
              this.normalizeScore(studentInfo, "thesisScore", "thesis_score"),
            academicPaperScore:
              this.normalizeScore(
                apiStudent,
                "academicPaperScore",
                "academic_paper_score",
              ) ||
              this.normalizeScore(
                studentInfo,
                "academicPaperScore",
                "academic_paper_score",
              ),
            competitionScore:
              this.normalizeScore(
                apiStudent,
                "competitionScore",
                "competition_score",
              ) ||
              this.normalizeScore(
                studentInfo,
                "competitionScore",
                "competition_score",
              ),
            patentSoftwareScore:
              this.normalizeScore(
                apiStudent,
                "patentSoftwareScore",
                "patent_software_score",
              ) ||
              this.normalizeScore(
                studentInfo,
                "patentSoftwareScore",
                "patent_software_score",
              ),
            otherInnovationAchievementScore:
              this.normalizeScore(
                apiStudent,
                "otherInnovationAchievementScore",
                "other_innovation_achievement_score",
              ) ||
              this.normalizeScore(
                studentInfo,
                "otherInnovationAchievementScore",
                "other_innovation_achievement_score",
              ),
          };

          const averageScores = {
            academicPerformanceScore:
              this.normalizeScore(
                apiAvg,
                "academicPerformanceScore",
                "academic_performance_score",
              ) || 70,
            researchProjectScore:
              this.normalizeScore(
                apiAvg,
                "researchProjectScore",
                "research_project_score",
              ) || 70,
            thesisScore:
              this.normalizeScore(apiAvg, "thesisScore", "thesis_score") || 70,
            academicPaperScore:
              this.normalizeScore(
                apiAvg,
                "academicPaperScore",
                "academic_paper_score",
              ) || 70,
            competitionScore:
              this.normalizeScore(
                apiAvg,
                "competitionScore",
                "competition_score",
              ) || 70,
            patentSoftwareScore:
              this.normalizeScore(
                apiAvg,
                "patentSoftwareScore",
                "patent_software_score",
              ) || 70,
            otherInnovationAchievementScore:
              this.normalizeScore(
                apiAvg,
                "otherInnovationAchievementScore",
                "other_innovation_achievement_score",
              ) || 70,
          };

          this.radarChartData = {
            studentName: studentInfo?.graduateName || "未知学生",
            studentId: this.selectedStudentId,
            studentScores,
            averageScores,
          };
          console.log("设置的雷达图数据:", this.radarChartData);
          this.studentData = studentInfo;
          this.generateComparisonData();
        } else {
          this.$message.error(response.message || "获取学生数据失败");
          // 如果API失败，加载演示数据
          this.loadDemoData();
        }
      } catch (error) {
        console.error("加载学生数据失败:", error);
        this.$message.error("加载学生数据失败，使用演示数据");
        // 如果API失败，加载演示数据
        this.loadDemoData();
      }
    },

    findStudentById(studentId) {
      return this.studentList.find(
        (student) => student.studentId === studentId,
      );
    },

    async calculateScores() {
      if (!this.selectedStudentId) {
        this.$message.warning("请先选择学生");
        return;
      }

      this.calculating = true;
      try {
        // 后端多数期望对象参数，这里统一传 { studentId }
        const response = await calculateStudentScores({
          studentId: this.selectedStudentId,
        });
        console.log("计算分数API响应:", response);
        const ok =
          response?.code === 200 ||
          response?.status === 200 ||
          response?.data?.success === true ||
          response?.data?.code === 200;
        if (ok) {
          this.$message.success("分数计算成功");
          await this.loadStudentData();
        } else {
          const msg =
            response?.message ||
            response?.data?.message ||
            response?.data?.msg ||
            "分数计算失败";
          console.error("计算分数失败: ", response);
          this.$message.error(msg);
        }
      } catch (error) {
        console.error("计算分数失败:", error);
        this.$message.error("计算分数失败");
      } finally {
        this.calculating = false;
      }
    },

    getScoreValue(key) {
      if (!this.studentData) return "0";
      const value = this.studentData[key];
      return value ? parseFloat(value).toFixed(1) : "0";
    },

    getScorePercentage(key) {
      if (!this.studentData) return 0;
      const value = this.studentData[key];
      return value ? Math.round((parseFloat(value) / 100) * 100) : 0;
    },

    calculateTotalScore() {
      if (!this.studentData) return "0.0";
      const scores = this.scoreItems.map((item) => {
        const value = this.studentData[item.key];
        return value ? parseFloat(value) : 0;
      });
      const total = scores.reduce((sum, score) => sum + score, 0);
      return (total / scores.length).toFixed(1);
    },

    getGradeClass() {
      const totalScore = parseFloat(this.calculateTotalScore());
      if (totalScore >= 90) return "grade-excellent";
      if (totalScore >= 80) return "grade-good";
      if (totalScore >= 70) return "grade-average";
      return "grade-poor";
    },

    getGradeText() {
      const totalScore = parseFloat(this.calculateTotalScore());
      if (totalScore >= 90) return "优秀";
      if (totalScore >= 80) return "良好";
      if (totalScore >= 70) return "中等";
      return "待改进";
    },

    getEvaluationSuggestion() {
      const totalScore = parseFloat(this.calculateTotalScore());
      if (totalScore >= 90) return "学业表现优秀，继续保持当前学习状态";
      if (totalScore >= 80) return "学业表现良好，可在某些方面进一步提升";
      if (totalScore >= 70) return "学业表现中等，需要加强薄弱环节";
      return "学业表现需要改进，建议制定详细的学习计划";
    },

    getStrengths() {
      if (!this.studentData) return [];
      const strengths = [];
      this.scoreItems.forEach((item) => {
        const value = this.studentData[item.key];
        if (value && parseFloat(value) >= 85) {
          strengths.push(`${item.name}能力突出`);
        }
      });
      return strengths.length > 0 ? strengths : ["暂无特别突出的能力"];
    },

    getImprovements() {
      if (!this.studentData) return [];
      const improvements = [];
      this.scoreItems.forEach((item) => {
        const value = this.studentData[item.key];
        if (!value || parseFloat(value) < 70) {
          improvements.push(`加强${item.name}方面的训练`);
        }
      });
      return improvements.length > 0 ? improvements : ["各项能力均衡发展"];
    },

    generateComparisonData() {
      if (!this.studentData) return;

      // 从当前学生列表计算同年级平均分（若后端未提供时使用）
      const computeAvg = (camelKey, snakeKey) => {
        if (!Array.isArray(this.studentList) || this.studentList.length === 0)
          return 0;
        const current = this.findStudentById(this.selectedStudentId) || {};
        const currentGrade =
          current.currentGrade || current.grade || current.Grade;
        const cohort = currentGrade
          ? this.studentList.filter(
              (s) => (s.currentGrade || s.grade || s.Grade) === currentGrade,
            )
          : this.studentList;
        const nums = cohort
          .map((s) => {
            const v = s?.[camelKey];
            if (v !== undefined && v !== null && v !== "") return Number(v);
            const sv = s?.[snakeKey];
            if (sv !== undefined && sv !== null && sv !== "") return Number(sv);
            return NaN;
          })
          .filter((n) => !isNaN(n));
        if (nums.length === 0) return 0;
        return Number(
          (nums.reduce((a, b) => a + b, 0) / nums.length).toFixed(1),
        );
      };

      const keyToAvg = {};
      this.scoreItems.forEach((item) => {
        keyToAvg[item.key] = computeAvg(
          item.key,
          item.key.replace(/([A-Z])/g, "_$1").toLowerCase(),
        );
      });

      this.comparisonData = this.scoreItems.map((item) => {
        const studentScore = parseFloat(this.getScoreValue(item.key));
        const averageScore = keyToAvg[item.key];
        const difference = studentScore - averageScore;
        const percentage =
          averageScore > 0
            ? Math.round((studentScore / averageScore) * 100)
            : 0;

        let evaluation = "";
        if (difference > 10) evaluation = "显著高于平均水平";
        else if (difference > 0) evaluation = "略高于平均水平";
        else if (difference > -10) evaluation = "接近平均水平";
        else evaluation = "低于平均水平";

        return {
          category: item.name,
          studentScore: studentScore.toFixed(1),
          averageScore: averageScore.toFixed(1),
          difference: difference.toFixed(1),
          percentage: percentage + "%",
          evaluation,
        };
      });

      // 将同年级对比分析的平均分同步用于雷达图绘制
      if (this.radarChartData) {
        this.radarChartData = {
          ...this.radarChartData,
          averageScores: {
            academicPerformanceScore: keyToAvg.academicPerformanceScore || 0,
            researchProjectScore: keyToAvg.researchProjectScore || 0,
            thesisScore: keyToAvg.thesisScore || 0,
            academicPaperScore: keyToAvg.academicPaperScore || 0,
            competitionScore: keyToAvg.competitionScore || 0,
            patentSoftwareScore: keyToAvg.patentSoftwareScore || 0,
            otherInnovationAchievementScore:
              keyToAvg.otherInnovationAchievementScore || 0,
          },
        };
        // 通知子组件重绘
        this.$nextTick(() => this.$forceUpdate());
      }
    },

    loadDemoData() {
      this.studentList = [
        {
          studentId: "2021001",
          graduateName: "张三",
          major: "计算机科学与技术",
          supervisor: "李教授",
          academicPerformanceScore: "85.5",
          researchProjectScore: "88.2",
          thesisScore: "82.1",
          academicPaperScore: "90.3",
          competitionScore: "78.9",
          patentSoftwareScore: "85.7",
          otherInnovationAchievementScore: "87.4",
        },
        {
          studentId: "2021002",
          graduateName: "李四",
          major: "软件工程",
          supervisor: "王教授",
          academicPerformanceScore: "92.1",
          researchProjectScore: "89.5",
          thesisScore: "91.2",
          academicPaperScore: "88.7",
          competitionScore: "94.3",
          patentSoftwareScore: "86.9",
          otherInnovationAchievementScore: "90.1",
        },
        {
          studentId: "2021003",
          graduateName: "王五",
          major: "人工智能",
          supervisor: "赵教授",
          academicPerformanceScore: "78.3",
          researchProjectScore: "82.6",
          thesisScore: "79.8",
          academicPaperScore: "81.2",
          competitionScore: "75.4",
          patentSoftwareScore: "80.1",
          otherInnovationAchievementScore: "77.9",
        },
      ];

      this.selectedStudentId = "2021001";
      this.studentData = this.studentList[0];

      this.radarChartData = {
        studentName: this.studentData.graduateName,
        studentId: this.studentData.studentId,
        studentScores: {
          academicPerformanceScore: 85.5,
          researchProjectScore: 88.2,
          thesisScore: 82.1,
          academicPaperScore: 90.3,
          competitionScore: 78.9,
          patentSoftwareScore: 85.7,
          otherInnovationAchievementScore: 87.4,
        },
        averageScores: {
          academicPerformanceScore: 80.0,
          researchProjectScore: 82.0,
          thesisScore: 78.0,
          academicPaperScore: 85.0,
          competitionScore: 75.0,
          patentSoftwareScore: 80.0,
          otherInnovationAchievementScore: 82.0,
        },
      };

      console.log("演示数据设置完成:", this.radarChartData);

      this.generateComparisonData();
      this.$message.success("演示数据加载完成");
    },

    refreshRadarChart() {
      console.log("手动刷新雷达图");
      if (this.radarChartData) {
        // 触发雷达图组件重新渲染
        this.$nextTick(() => {
          this.$forceUpdate();
          this.$message.info("雷达图已刷新");
        });
      } else {
        this.$message.warning("没有雷达图数据可刷新，请先加载数据");
      }
    },

    testRadarChart() {
      console.log("测试雷达图功能");
      // 创建简单的测试数据
      this.radarChartData = {
        studentName: "测试学生",
        studentId: "TEST001",
        studentScores: {
          academicPerformanceScore: 80,
          researchProjectScore: 75,
          thesisScore: 85,
          academicPaperScore: 70,
          competitionScore: 90,
          patentSoftwareScore: 65,
          otherInnovationAchievementScore: 78,
        },
        averageScores: {
          academicPerformanceScore: 75,
          researchProjectScore: 70,
          thesisScore: 80,
          academicPaperScore: 65,
          competitionScore: 85,
          patentSoftwareScore: 60,
          otherInnovationAchievementScore: 73,
        },
      };

      this.studentData = {
        studentId: "TEST001",
        graduateName: "测试学生",
        major: "测试专业",
        supervisor: "测试导师",
      };

      this.generateComparisonData();
      this.$message.success("测试数据已加载，雷达图应该显示");
    },
  },
};
</script>

<style scoped>
.academic-analysis {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.page-header h2 {
  margin: 0;
  color: #303133;
  font-size: 24px;
}

.header-actions {
  display: flex;
  gap: 10px;
  align-items: center;
}

.content-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.radar-chart-section {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.analysis-section {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 20px;
}

.analysis-card {
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.card-header {
  font-weight: bold;
  color: #303133;
}

.student-info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
}

.info-item {
  display: flex;
  align-items: center;
}

.info-item label {
  font-weight: bold;
  margin-right: 10px;
  color: #606266;
  min-width: 60px;
}

.score-grid {
  display: grid;
  gap: 15px;
}

.score-item {
  display: flex;
  align-items: center;
  gap: 15px;
}

.score-label {
  font-weight: bold;
  color: #606266;
  min-width: 100px;
}

.score-value {
  font-weight: bold;
  color: #409eff;
  min-width: 50px;
}

.score-bar {
  flex: 1;
  height: 8px;
  background: #f0f0f0;
  border-radius: 4px;
  overflow: hidden;
}

.score-progress {
  height: 100%;
  background: linear-gradient(90deg, #409eff, #67c23a);
  border-radius: 4px;
  transition: width 0.3s ease;
}

.evaluation-content {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.evaluation-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.evaluation-item label {
  font-weight: bold;
  color: #606266;
  min-width: 80px;
}

.total-score {
  font-size: 18px;
  font-weight: bold;
  color: #67c23a;
}

.grade-badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: bold;
  color: white;
}

.grade-excellent {
  background: #67c23a;
}

.grade-good {
  background: #409eff;
}

.grade-average {
  background: #e6a23c;
}

.grade-poor {
  background: #f56c6c;
}

.analysis-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.strength-section,
.improvement-section {
  flex: 1;
}

.strength-section h4,
.improvement-section h4 {
  margin: 0 0 10px 0;
  color: #303133;
}

.strength-section h4 {
  color: #67c23a;
}

.improvement-section h4 {
  color: #e6a23c;
}

.strength-section ul,
.improvement-section ul {
  margin: 0;
  padding-left: 20px;
}

.strength-section li,
.improvement-section li {
  margin-bottom: 5px;
  color: #606266;
}

.comparison-content {
  overflow-x: auto;
}

.el-table {
  margin-top: 10px;
}
</style>
