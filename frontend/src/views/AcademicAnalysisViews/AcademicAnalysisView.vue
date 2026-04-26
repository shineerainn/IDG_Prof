<template>
  <div class="academic-analysis">
    <!-- 背景装饰 -->
    <div class="background-decoration">
      <div class="decoration-circle circle-1"></div>
      <div class="decoration-circle circle-2"></div>
      <div class="decoration-circle circle-3"></div>
    </div>

    <div class="page-header">
      <h2 class="page-title">研究生学业分析</h2>

      <div class="header-actions">
        <!-- 年份选择框 -->
        <el-select
          v-model="selectedYear"
          placeholder="选择年份"
          class="header-select"
        >
          <el-option
            v-for="year in years"
            :key="year"
            :label="year"
            :value="year"
          />
        </el-select>

        <!-- 学期选择框 -->
        <el-select
          v-model="selectedSemester"
          placeholder="选择学期"
          class="header-select"
        >
          <el-option
            v-for="semester in semesters"
            :key="semester.value"
            :label="semester.label"
            :value="semester.value"
          />
        </el-select>

        <el-select
          v-model="selectedStudentId"
          placeholder="选择学生"
          @change="loadStudentData"
          filterable
          remote
          :remote-method="filterStudents"
          :loading="loading"
          class="header-select student-select"
        >
          <el-option
            v-for="student in filteredStudentList"
            :key="student.studentId"
            :label="`${student.graduateName} (${student.studentId})`"
            :value="student.studentId"
          />
        </el-select>

        <!-- 搜索按钮 -->
        <el-button
          type="primary"
          icon="el-icon-search"
          @click="handleSearch"
          class="search-btn"
        >
          搜索
        </el-button>
      </div>
    </div>

    <div class="content-scroll-container">
      <div class="content-wrapper">
        <!-- 雷达图展示 -->
        <div class="chart-section">
          <div class="chart-card">
            <div class="card-header">
              <h3>学业能力雷达图</h3>
              <p class="card-subtitle">多维度学业能力评估分析</p>
            </div>
            <div class="chart-content">
              <AcademicRadarChart :radarData="radarChartData" />
            </div>
          </div>
        </div>

        <!-- 折线图 -->
        <div class="chart-section">
          <div class="chart-card">
            <div class="card-header">
              <h3>学业趋势分析</h3>
              <p class="card-subtitle">各学期学业表现变化趋势</p>
            </div>
            <div class="chart-content">
              <AcademicLineChart :lineData="lineChartData" />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import AcademicRadarChart from "@/components/AcademicRadarChart.vue";
import AcademicLineChart from "@/components/AcademicLineChart.vue";
import { getAllStudents, calculateStudentScores } from "@/api/academicAnalysis";
import axios from "axios";

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
      filteredStudentList: [],
      loading: false,
      studentData: null,
      radarChartData: null,
      lineChartData: null,
      calculating: false,
      scoreItems: [
        { key: "academicPerformanceScore", name: "学业表现" },
        { key: "researchProjectScore", name: "科研项目" },
        { key: "thesisScore", name: "学位论文" },
        { key: "academicPaperScore", name: "学术论文" },
        { key: "competitionScore", name: "竞赛获奖" },
        { key: "patentSoftwareScore", name: "专利软件" },
      ],
      comparisonData: [],
      selectedYear: 2025,
      years: [2020, 2021, 2022, 2023, 2024, 2025],
      selectedSemester: 2,
      semesters: [
        { label: "春季学期", value: 1 },
        { label: "秋季学期", value: 2 },
      ],
    };
  },
  async mounted() {
    await this.loadStudentList();
  },
  methods: {
    // 添加搜索处理函数
    handleSearch() {
      if (this.selectedStudentId) {
        this.loadStudentData();
      } else {
        this.$message.warning("请先选择学生");
      }
    },
    filterStudents(query) {
      if (query !== "") {
        this.loading = true;
        setTimeout(() => {
          this.loading = false;
          this.filteredStudentList = this.studentList.filter((item) => {
            return (
              item.graduateName.toLowerCase().includes(query.toLowerCase()) ||
              item.studentId.includes(query)
            );
          });
        }, 200);
      } else {
        this.filteredStudentList = this.studentList;
      }
    },
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
          this.filteredStudentList = this.studentList;
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
        const response = await axios.get(
          `http://localhost:8088/api/student-academic-performance/radar/${this.selectedStudentId}/${this.selectedYear}/${this.selectedSemester}`,
        );
        // const response = await getStudentRadarChartData(this.selectedStudentId);
        console.log("API响应:", response);

        console.log("API返回的数据:", response.data);
        const apiData = response.data || {};

        const studentScores = {
          academicPerformanceScore:
            this.normalizeScore(apiData.scores, "学业表现", "学业表现") || 0,
          researchProjectScore:
            this.normalizeScore(apiData.scores, "科研项目", "科研项目") || 0,
          thesisScore:
            this.normalizeScore(apiData.scores, "学位论文", "学位论文") || 0,
          academicPaperScore:
            this.normalizeScore(apiData.scores, "学术论文", "学术论文") || 0,
          competitionScore:
            this.normalizeScore(apiData.scores, "竞赛获奖", "竞赛获奖") || 0,
          patentSoftwareScore:
            this.normalizeScore(apiData.scores, "专利软著", "专利软著") || 0,
        };

        const averageScores = {
          academicPerformanceScore:
            this.normalizeScore(
              apiData.avgScores,
              "平均学业表现",
              "平均学业表现",
            ) || 0,
          researchProjectScore:
            this.normalizeScore(
              apiData.avgScores,
              "平均科研项目",
              "平均科研项目",
            ) || 0,
          thesisScore:
            this.normalizeScore(
              apiData.avgScores,
              "平均学位论文",
              "平均学位论文",
            ) || 0,
          academicPaperScore:
            this.normalizeScore(
              apiData.avgScores,
              "平均学术论文",
              "平均学术论文",
            ) || 0,
          competitionScore:
            this.normalizeScore(
              apiData.avgScores,
              "平均竞赛获奖",
              "平均竞赛获奖",
            ) || 0,
          patentSoftwareScore:
            this.normalizeScore(
              apiData.avgScores,
              "平均专利软著",
              "平均专利软著",
            ) || 0,
        };

        this.radarChartData = {
          studentName: apiData.studentName || "未知学生",
          studentId: this.selectedStudentId,
          studentScores,
          averageScores,
        };
        // this.lineChartData = {
        //   semester2023Spring: {
        //     academicPerformanceScore:
        //       this.normalizeScore(
        //         apiData.semester2023Spring,
        //         "学业表现",
        //         "学业表现",
        //       ) || 0,
        //     researchProjectScore:
        //       this.normalizeScore(
        //         apiData.semester2023Spring,
        //         "科研项目",
        //         "科研项目",
        //       ) || 0,
        //     thesisScore:
        //       this.normalizeScore(
        //         apiData.semester2023Spring,
        //         "学位论文",
        //         "学位论文",
        //       ) || 0,
        //     academicPaperScore:
        //       this.normalizeScore(
        //         apiData.semester2023Spring,
        //         "学术论文",
        //         "学术论文",
        //       ) || 0,
        //     competitionScore:
        //       this.normalizeScore(
        //         apiData.semester2023Spring,
        //         "竞赛获奖",
        //         "竞赛获奖",
        //       ) || 0,
        //     patentSoftwareScore:
        //       this.normalizeScore(
        //         apiData.semester2023Spring,
        //         "专利软著",
        //         "专利软著",
        //       ) || 0,
        //   },
        //   semester2023Autumn: {
        //     academicPerformanceScore:
        //       this.normalizeScore(
        //         apiData.semester2023Autumn,
        //         "学业表现",
        //         "学业表现",
        //       ) || 0,
        //     researchProjectScore:
        //       this.normalizeScore(
        //         apiData.semester2023Autumn,
        //         "科研项目",
        //         "科研项目",
        //       ) || 0,
        //     thesisScore:
        //       this.normalizeScore(
        //         apiData.semester2023Autumn,
        //         "学位论文",
        //         "学位论文",
        //       ) || 0,
        //     academicPaperScore:
        //       this.normalizeScore(
        //         apiData.semester2023Autumn,
        //         "学术论文",
        //         "学术论文",
        //       ) || 0,
        //     competitionScore:
        //       this.normalizeScore(
        //         apiData.semester2023Autumn,
        //         "竞赛获奖",
        //         "竞赛获奖",
        //       ) || 0,
        //     patentSoftwareScore:
        //       this.normalizeScore(
        //         apiData.semester2023Autumn,
        //         "专利软著",
        //         "专利软著",
        //       ) || 0,
        //   },
        //   semester2024Spring: {
        //     academicPerformanceScore:
        //       this.normalizeScore(
        //         apiData.semester2024Spring,
        //         "学业表现",
        //         "学业表现",
        //       ) || 0,
        //     researchProjectScore:
        //       this.normalizeScore(
        //         apiData.semester2024Spring,
        //         "科研项目",
        //         "科研项目",
        //       ) || 0,
        //     thesisScore:
        //       this.normalizeScore(
        //         apiData.semester2024Spring,
        //         "学位论文",
        //         "学位论文",
        //       ) || 0,
        //     academicPaperScore:
        //       this.normalizeScore(
        //         apiData.semester2024Spring,
        //         "学术论文",
        //         "学术论文",
        //       ) || 0,
        //     competitionScore:
        //       this.normalizeScore(
        //         apiData.semester2024Spring,
        //         "竞赛获奖",
        //         "竞赛获奖",
        //       ) || 0,
        //     patentSoftwareScore:
        //       this.normalizeScore(
        //         apiData.semester2024Spring,
        //         "专利软著",
        //         "专利软著",
        //       ) || 0,
        //   },
        //   semester2024Autumn: {
        //     academicPerformanceScore:
        //       this.normalizeScore(
        //         apiData.semester2024Autumn,
        //         "学业表现",
        //         "学业表现",
        //       ) || 0,
        //     researchProjectScore:
        //       this.normalizeScore(
        //         apiData.semester2024Autumn,
        //         "科研项目",
        //         "科研项目",
        //       ) || 0,
        //     thesisScore:
        //       this.normalizeScore(
        //         apiData.semester2024Autumn,
        //         "学位论文",
        //         "学位论文",
        //       ) || 0,
        //     academicPaperScore:
        //       this.normalizeScore(
        //         apiData.semester2024Autumn,
        //         "学术论文",
        //         "学术论文",
        //       ) || 0,
        //     competitionScore:
        //       this.normalizeScore(
        //         apiData.semester2024Autumn,
        //         "竞赛获奖",
        //         "竞赛获奖",
        //       ) || 0,
        //     patentSoftwareScore:
        //       this.normalizeScore(
        //         apiData.semester2024Autumn,
        //         "专利软著",
        //         "专利软著",
        //       ) || 0,
        //   },
        //   semester2025Spring: {
        //     academicPerformanceScore:
        //       this.normalizeScore(
        //         apiData.semester2025Spring,
        //         "学业表现",
        //         "学业表现",
        //       ) || 0,
        //     researchProjectScore:
        //       this.normalizeScore(
        //         apiData.semester2025Spring,
        //         "科研项目",
        //         "科研项目",
        //       ) || 0,
        //     thesisScore:
        //       this.normalizeScore(
        //         apiData.semester2025Spring,
        //         "学位论文",
        //         "学位论文",
        //       ) || 0,
        //     academicPaperScore:
        //       this.normalizeScore(
        //         apiData.semester2025Spring,
        //         "学术论文",
        //         "学术论文",
        //       ) || 0,
        //     competitionScore:
        //       this.normalizeScore(
        //         apiData.semester2025Spring,
        //         "竞赛获奖",
        //         "竞赛获奖",
        //       ) || 0,
        //     patentSoftwareScore:
        //       this.normalizeScore(
        //         apiData.semester2025Spring,
        //         "专利软著",
        //         "专利软著",
        //       ) || 0,
        //   },
        //   semester2025Autumn: {
        //     academicPerformanceScore:
        //       this.normalizeScore(
        //         apiData.semester2025Autumn,
        //         "学业表现",
        //         "学业表现",
        //       ) || 0,
        //     researchProjectScore:
        //       this.normalizeScore(
        //         apiData.semester2025Autumn,
        //         "科研项目",
        //         "科研项目",
        //       ) || 0,
        //     thesisScore:
        //       this.normalizeScore(
        //         apiData.semester2025Autumn,
        //         "学位论文",
        //         "学位论文",
        //       ) || 0,
        //     academicPaperScore:
        //       this.normalizeScore(
        //         apiData.semester2025Autumn,
        //         "学术论文",
        //         "学术论文",
        //       ) || 0,
        //     competitionScore:
        //       this.normalizeScore(
        //         apiData.semester2025Autumn,
        //         "竞赛获奖",
        //         "竞赛获奖",
        //       ) || 0,
        //     patentSoftwareScore:
        //       this.normalizeScore(
        //         apiData.semester2025Autumn,
        //         "专利软著",
        //         "专利软著",
        //       ) || 0,
        //   },
        // };
        this.lineChartData = {
          studentName: apiData.studentName || "未知学生",
          studentId: this.selectedStudentId,
          semester2023Spring: {
            academicPerformanceScore: 0,
            researchProjectScore: 0,
            thesisScore: 0,
            academicPaperScore: 0,
            competitionScore: 0,
            patentSoftwareScore: 0,
          },
          semester2023Autumn: {
            academicPerformanceScore: 0,
            researchProjectScore: 0,
            thesisScore: 0,
            academicPaperScore: 0,
            competitionScore: 0,
            patentSoftwareScore: 0,
          },
          semester2024Spring: {
            academicPerformanceScore: 8.4,
            researchProjectScore: 0.0,
            thesisScore: 0.0,
            academicPaperScore: 3.0,
            competitionScore: 4.8,
            patentSoftwareScore: 5.6,
          },
          semester2024Autumn: {
            academicPerformanceScore: 8.4,
            researchProjectScore: 0.0,
            thesisScore: 0.0,
            academicPaperScore: 3.0,
            competitionScore: 4.8,
            patentSoftwareScore: 5.6,
          },
          semester2025Spring: {
            academicPerformanceScore: 8.4,
            researchProjectScore: 3.0,
            thesisScore: 0.0,
            academicPaperScore: 3.0,
            competitionScore: 4.8,
            patentSoftwareScore: 5.6,
          },
          semester2025Autumn: {
            academicPerformanceScore: 8.4,
            researchProjectScore: 3.0,
            thesisScore: 0.0,
            academicPaperScore: 3.0,
            competitionScore: 4.8,
            patentSoftwareScore: 9.6,
          },
        };
        console.log("设置的雷达图数据:", this.radarChartData);
        this.studentData = {
          studentId: apiData.studentId,
          graduateName: apiData.studentName,
        };
        // this.generateComparisonData();
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

    calculateTotalScore() {
      if (!this.studentData) return "0.0";
      const scores = this.scoreItems.map((item) => {
        const value = this.studentData[item.key];
        return value ? parseFloat(value) : 0;
      });
      const total = scores.reduce((sum, score) => sum + score, 0);
      return (total / scores.length).toFixed(1);
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
        },
      ];

      this.selectedStudentId = "2021001";
      this.studentData = this.studentList[0];

      this.radarChartData = {
        studentName: this.studentData.graduateName,
        studentId: this.studentData.studentId,
        studentScores: {
          academicPerformanceScore: 85.5,
          researchProjectScore: 89.2,
          thesisScore: 82.1,
          academicPaperScore: 90.3,
          competitionScore: 78.9,
          patentSoftwareScore: 85.7,
        },
        averageScores: {
          academicPerformanceScore: 80.0,
          researchProjectScore: 82.0,
          thesisScore: 78.0,
          academicPaperScore: 85.0,
          competitionScore: 75.0,
          patentSoftwareScore: 80.0,
        },
      };

      console.log("演示数据设置完成:", this.radarChartData);

      this.generateComparisonData();
      this.$message.success("演示数据加载完成");
    },
  },
};
</script>

<style lang="scss" scoped>
.academic-analysis {
  height: calc(100vh - 80px); /* 与侧边栏相同的高度 */
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e7ed 100%);
  position: relative;
  overflow: hidden;
}

.background-decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
  overflow: hidden;

  .decoration-circle {
    position: absolute;
    border-radius: 50%;
    opacity: 0.12;
  }

  .circle-1 {
    width: 400px;
    height: 400px;
    background: linear-gradient(135deg, #5a6fd8 0%, #6a42a0 100%);
    top: -150px;
    right: -150px;
  }

  .circle-2 {
    width: 300px;
    height: 300px;
    background: linear-gradient(135deg, #e081f0 0%, #e5455a 100%);
    bottom: 100px;
    left: -100px;
  }

  .circle-3 {
    width: 200px;
    height: 200px;
    background: linear-gradient(135deg, #3a9cfc 0%, #00d9e4 100%);
    top: 40%;
    right: 20%;
  }
}

.page-header {
  flex-shrink: 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 30px;
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  position: relative;
  z-index: 1;
  border-bottom: 1px solid rgba(234, 236, 239, 0.8);

  &::before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 4px;
    background: linear-gradient(90deg, #667eea, #764ba2);
  }
}

.page-title {
  margin: 0;
  color: #1e3c72;
  font-size: 24px;
  font-weight: 700;
  background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.header-actions {
  display: flex;
  gap: 12px;
  align-items: center;
}

.header-select {
  width: 140px;

  &:deep(.el-input__inner) {
    border-radius: 8px;
    border: 1px solid #e4e7ed;
    transition: all 0.3s ease;

    &:focus {
      border-color: #667eea;
      box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.1);
    }
  }
}

.student-select {
  width: 200px;
}

.search-btn {
  padding: 10px 20px;
  border-radius: 8px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  font-weight: 500;
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-1px);
    box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
  }
}

.content-scroll-container {
  flex: 1;
  overflow-y: auto;
  padding: 20px 30px;
  position: relative;
  z-index: 1;

  // 自定义滚动条
  &::-webkit-scrollbar {
    width: 6px;
  }

  &::-webkit-scrollbar-track {
    background: rgba(255, 255, 255, 0.1);
    border-radius: 3px;
  }

  &::-webkit-scrollbar-thumb {
    background: rgba(102, 126, 234, 0.3);
    border-radius: 3px;
  }

  &::-webkit-scrollbar-thumb:hover {
    background: rgba(102, 126, 234, 0.5);
  }
}

.content-wrapper {
  display: flex;
  flex-direction: column;
  gap: 20px;
  min-height: min-content;
}

.chart-section {
  .chart-card {
    background: white;
    border-radius: 12px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
    overflow: hidden;
    border: 1px solid rgba(255, 255, 255, 0.2);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 20px rgba(0, 0, 0, 0.12);
    }
  }
}

.card-header {
  padding: 20px 25px 15px;
  border-bottom: 1px solid rgba(234, 236, 239, 0.8);

  h3 {
    margin: 0 0 6px 0;
    color: #1e3c72;
    font-size: 18px;
    font-weight: 600;
  }

  .card-subtitle {
    margin: 0;
    color: #909399;
    font-size: 13px;
  }
}

.chart-content {
  padding: 20px 25px;
  min-height: 350px;
}

// 响应式设计
@media (max-width: 768px) {
  .academic-analysis {
    padding: 0;
  }

  .page-header {
    flex-direction: column;
    gap: 15px;
    padding: 15px 20px;
  }

  .page-title {
    font-size: 20px;
  }

  .header-actions {
    width: 100%;
    justify-content: center;
    flex-wrap: wrap;
  }

  .header-select {
    width: 120px;
  }

  .student-select {
    width: 180px;
  }

  .content-scroll-container {
    padding: 15px 20px;
  }

  .chart-content {
    padding: 15px 20px;
    min-height: 300px;
  }
}

@media (max-width: 480px) {
  .header-actions {
    flex-direction: column;
    width: 100%;
  }

  .header-select,
  .student-select {
    width: 100%;
  }

  .search-btn {
    width: 100%;
  }
}

// 保留原有的样式（用于其他功能）
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
