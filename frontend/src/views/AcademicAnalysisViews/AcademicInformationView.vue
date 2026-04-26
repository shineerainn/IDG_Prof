<template>
  <div class="modeling-container">
    <div
      style="
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;
        padding-left: 14px;
      "
    >
      <h1 class="page-title">研究生学业信息</h1>
      <div class="search-container">
        <!-- 年份选择框 -->
        <el-select
          v-model="selectedYear"
          placeholder="选择年份"
          style="width: 120px; margin-right: 10px"
        >
          <el-option
            v-for="year in years"
            :key="year"
            :label="year"
            :value="year"
          ></el-option>
        </el-select>

        <!-- 学期选择框 -->
        <el-select
          v-model="selectedSemester"
          placeholder="选择学期"
          style="width: 120px; margin-right: 10px"
        >
          <el-option
            v-for="semester in semesters"
            :key="semester.value"
            :label="semester.label"
            :value="semester.value"
          ></el-option>
        </el-select>

        <!-- 搜索按钮 -->
        <el-button
          type="primary"
          icon="el-icon-search"
          @click="loadStudentsByYearAndSemester"
          style="margin-right: 10px"
        >
          搜索
        </el-button>

        <el-select
          v-model="searchCategory"
          placeholder="选择搜索类别"
          style="width: 150px; margin-right: 10px"
        >
          <el-option
            v-for="column in searchableColumns"
            :key="column.prop"
            :label="column.label"
            :value="column.prop"
          ></el-option>
        </el-select>
        <el-input
          v-model="search"
          placeholder="搜索..."
          style="width: 400px"
          @keyup.enter.native="handleSearch"
        >
          <el-button
            slot="append"
            icon="el-icon-search"
            @click="handleSearch"
          ></el-button>
        </el-input>
      </div>
    </div>

    <!-- 数据表格 -->
    <el-table
      :data="paginatedStudents"
      style="width: 100%"
      v-loading="loading"
      border
      stripe
    >
      <el-table-column
        type="index"
        label="序号"
        width="65"
        align="center"
      ></el-table-column>
      <el-table-column prop="graduateName" label="姓名" width="120">
        <template slot-scope="scope">
          <el-button type="text" @click.stop="viewDetail(scope.row)">
            {{ scope.row.graduateName }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        prop="studentId"
        label="学号"
        width="120"
      ></el-table-column>
      <el-table-column
        prop="academicPerformanceScore"
        label="学业表现"
        min-width="145"
      >
        <template slot-scope="scope">
          <span class="score">{{ scope.row.academicPerformanceScore }}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="researchProjectScore"
        label="科研项目"
        min-width="145"
      >
        <template slot-scope="scope">
          <span class="score">{{ scope.row.researchProjectScore }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="thesisScore" label="学位论文" min-width="145">
        <template slot-scope="scope">
          <span class="score">{{ scope.row.thesisScore }}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="academicPaperScore"
        label="学术论文"
        min-width="145"
      >
        <template slot-scope="scope">
          <span class="score">{{ scope.row.academicPaperScore }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="competitionScore" label="竞赛获奖" min-width="145">
        <template slot-scope="scope">
          <span class="score">{{ scope.row.competitionScore }}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="patentSoftwareScore"
        label="专利软著"
        min-width="145"
      >
        <template slot-scope="scope">
          <span class="score">{{ scope.row.patentSoftwareScore }}</span>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      background
      layout="total, sizes, prev, pager, next"
      :total="filteredStudents.length"
      :page-sizes="[10, 20, 50]"
      :page-size="pageSize"
      :current-page.sync="currentPage"
      class="pagination-container"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "AcademicInformationView",
  data() {
    return {
      students: [],
      loading: false,
      search: "",
      searchCategory: "graduateName",
      currentPage: 1,
      pageSize: 10,
      searchableColumns: [
        { prop: "graduateName", label: "姓名" },
        { prop: "studentId", label: "学号" },
      ],
      // 年份和学期筛选相关数据
      selectedYear: 2025,
      years: [2020, 2021, 2022, 2023, 2024, 2025],
      selectedSemester: 2,
      semesters: [
        { label: "春季学期", value: 1 },
        { label: "秋季学期", value: 2 },
      ],
    };
  },
  computed: {
    filteredStudents() {
      let filtered = this.students;

      if (this.search && this.searchCategory) {
        filtered = filtered.filter((item) => {
          const value = String(item[this.searchCategory] || "").toLowerCase();
          return value.includes(this.search.toLowerCase());
        });
      }

      return filtered;
    },
    paginatedStudents() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.filteredStudents.slice(start, end);
    },
  },
  // mounted() {
  //   this.loadStudents();
  // },
  async mounted() {
    this.loadStudentsByYearAndSemester();
    // this.loading = true;
    // try {
    //   // 优先尝试加载学期数据
    //   try {
    //     const response = await axios.get(
    //       "http://localhost:8088/api/student-academic-performance/2025/2",
    //     );
    //     this.students = response.data || [];
    //     console.log("成功加载2024年第1学期数据");
    //   } catch (semesterError) {
    //     console.warn("学期数据接口不可用，回退到原接口:", semesterError);
    //     // 如果学期数据接口失败，则使用原接口
    //     const response = await getAllStudents();
    //     this.students = response.data || [];
    //   }
    // } catch (error) {
    //   console.error("加载学生数据失败:", error);
    //   this.$message.error("加载学生数据失败");
    // } finally {
    //   this.loading = false;
    // }
  },
  methods: {
    // async loadStudents() {
    //   this.loading = true;
    //   try {
    //     const response = await getAllStudents();
    //     this.students = response.data || [];
    //   } catch (error) {
    //     console.error("加载学生数据失败:", error);
    //     this.$message.error("加载学生数据失败");
    //   } finally {
    //     this.loading = false;
    //   }
    // },
    async loadStudentsByYearAndSemester() {
      this.loading = true;
      try {
        const response = await axios.get(
          `http://localhost:8088/api/student-academic-performance/${this.selectedYear}/${this.selectedSemester}`,
        );
        this.students = response.data || [];
        console.log(
          `成功加载${this.selectedYear}年第${this.selectedSemester}学期数据`,
        );
      } catch (error) {
        console.error("加载学生数据失败:", error);
        this.$message.error("加载学生数据失败");
        // 失败时加载演示数据
        this.loadDemoData();
      } finally {
        this.loading = false;
      }
    },
    handleSearch() {
      this.currentPage = 1;
    },
    resetSearch() {
      this.search = "";
      this.searchCategory = "graduateName";
      this.currentPage = 1;
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.currentPage = 1;
    },
    handleCurrentChange(val) {
      this.currentPage = val;
    },
    viewDetail(student) {
      this.$router.push({
        name: "AcademicInformationDetail",
        params: { studentId: student.studentId },
        query: { studentData: JSON.stringify(student) },
      });
    },
    loadDemoData() {
      this.students = [
        {
          studentId: "2019800001",
          graduateName: "文洋",
          studentType: "学术型博士",
          requiredCredits: 22,
          earnedCredits: 20,
          creditCompletionRate: 0.909090909,
          totalRequiredCredits: 30,
          totalEarnedCredits: 28,
          totalCreditCompletionRate: 0.933333333,
          academicPerformanceScore: 7.9,
          researchProjectCount: 1,
          researchProjectLevel: "横向",
          researchFunding: 71.0,
          researchProjectRanking: 81.0,
          projectRole: "核心成员",
          projectOutcomeTransformation: "无",
          researchProjectScore: 3.0,
          proposalStatus: "暂未涉及",
          midtermStatus: "暂未涉及",
          reviewStatus: "暂未涉及",
          reviewScore: "暂未涉及",
          defenseGrade: "暂未涉及",
          outstandingThesis: "否",
          thesisScore: 0.0,
          academicPaperCount: 1,
          publicationVenueLevel: "SCI",
          authorRole: "其他作者",
          highlyCited: "否",
          impactFactorAbove10: "否",
          coverPaper: "否",
          academicPaperScore: 3.0,
          competitionAwardCount: 1,
          competitionLevel: "A类",
          awardLevel: "二等奖",
          competitionScope: "国家级",
          competitionTeamRole: "一般成员",
          competitionScore: 4.8,
          patentSoftwareCount: 2,
          patentSoftwareType: "发明专利",
          patentSoftwareScope: "国内",
          patentSoftwareTeamRole: "核心成员",
          patentSoftwareScore: 9.6,
          academicEvaluationScore: 28.3,
        },
        {
          studentId: "2019800002",
          graduateName: "吕静怡",
          studentType: "学术型博士",
          requiredCredits: 25,
          earnedCredits: 21,
          creditCompletionRate: 0.84,
          totalRequiredCredits: 33,
          totalEarnedCredits: 29,
          totalCreditCompletionRate: 0.878787879,
          academicPerformanceScore: 8.0,
          researchProjectCount: 0,
          researchProjectLevel: "无",
          researchFunding: 0.0,
          researchProjectRanking: 0.0,
          projectRole: "无",
          projectOutcomeTransformation: "无",
          researchProjectScore: 0.0,
          proposalStatus: "暂未涉及",
          midtermStatus: "暂未涉及",
          reviewStatus: "暂未涉及",
          reviewScore: "暂未涉及",
          defenseGrade: "暂未涉及",
          outstandingThesis: "否",
          thesisScore: 0.0,
          academicPaperCount: 0,
          publicationVenueLevel: "无",
          authorRole: "无",
          highlyCited: "否",
          impactFactorAbove10: "否",
          coverPaper: "否",
          academicPaperScore: 0.0,
          competitionAwardCount: 0,
          competitionLevel: "无",
          awardLevel: "无",
          competitionScope: "无",
          competitionTeamRole: "无",
          competitionScore: 0.0,
          patentSoftwareCount: 1,
          patentSoftwareType: "实用新型",
          patentSoftwareScope: "国内",
          patentSoftwareTeamRole: "一般成员",
          patentSoftwareScore: 4.0,
          academicEvaluationScore: 12.0,
        },
        {
          studentId: "2019800003",
          graduateName: "金宇",
          studentType: "学术型博士",
          requiredCredits: 21,
          earnedCredits: 20,
          creditCompletionRate: 0.952380952,
          totalRequiredCredits: 29,
          totalEarnedCredits: 28,
          totalCreditCompletionRate: 0.965517241,
          academicPerformanceScore: 7.3,
          researchProjectCount: 2,
          researchProjectLevel: "纵向",
          researchFunding: 32.0,
          researchProjectRanking: 96.0,
          projectRole: "核心成员",
          projectOutcomeTransformation: "无",
          researchProjectScore: 0.0,
          proposalStatus: "通过",
          midtermStatus: "不通过",
          reviewStatus: "暂未涉及",
          reviewScore: "暂未涉及",
          defenseGrade: "暂未涉及",
          outstandingThesis: "否",
          thesisScore: 2.0,
          academicPaperCount: 2,
          publicationVenueLevel: "EI",
          authorRole: "第一作者",
          highlyCited: "否",
          impactFactorAbove10: "否",
          coverPaper: "否",
          academicPaperScore: 9.6,
          competitionAwardCount: 1,
          competitionLevel: "A类",
          awardLevel: "一等奖",
          competitionScope: "国家级",
          competitionTeamRole: "核心成员",
          competitionScore: 9.6,
          patentSoftwareCount: 1,
          patentSoftwareType: "外观设计",
          patentSoftwareScope: "国外",
          patentSoftwareTeamRole: "核心成员",
          patentSoftwareScore: 5.8,
          academicEvaluationScore: 34.3,
        },
      ];
      this.$message.success("演示数据加载成功");
    },
  },
};
</script>

<style scoped>
.modeling-container {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

  .page-title {
    text-align: center;
    color: #2c3e50;
    font-size: 24px;
  }

  .pagination-container {
    display: flex;
    justify-content: flex-end;
    margin-top: 20px;
  }

  .search-container {
    display: flex;
    align-items: center;
  }

  ::v-deep .el-table {
    th {
      background-color: #f5f7fa !important;
      font-weight: 600;
    }

    .el-table__body tr:hover > td {
      background-color: #f0f7ff !important;
    }

    .el-table__body td {
      padding: 8px 8px;
    }
  }
}

.score {
  color: #409eff;
  font-weight: bold;
}
</style>
