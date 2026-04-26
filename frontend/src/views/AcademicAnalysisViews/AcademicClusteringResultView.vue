<template>
  <div class="clustering-result-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">学业画像聚类结果</h1>
      <div class="header-actions">
        <el-button type="primary" @click="exportResults">导出结果</el-button>
        <el-button @click="goBack">返回列表</el-button>
      </div>
    </div>

    <!-- 模型信息卡片 -->
    <el-card class="model-info-card" v-if="modelInfo">
      <div slot="header">
        <span>模型信息</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="info-item">
            <label>模型ID:</label>
            <span>{{ modelInfo.profileId }}</span>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="info-item">
            <label>创建时间:</label>
            <span>{{ modelInfo.createTime }}</span>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="info-item">
            <label>记录数量:</label>
            <span>{{ modelInfo.recordCount }}</span>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="info-item">
            <label>聚类数量:</label>
            <span>{{ modelInfo.totalClusters }}</span>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 聚类统计图表 -->
    <el-row :gutter="20" class="charts-row">
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">
            <span>聚类分布饼图</span>
          </div>
          <div ref="pieChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">
            <span>聚类分布柱状图</span>
          </div>
          <div ref="barChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 聚类详情表格 -->
    <el-card class="detail-card">
      <div slot="header">
        <span>聚类详情</span>
        <div class="header-actions">
          <el-input
            v-model="searchText"
            placeholder="搜索学生..."
            style="width: 200px; margin-right: 10px"
            clearable
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-select
            v-model="selectedCluster"
            placeholder="选择聚类"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="cluster in clusterOptions"
              :key="cluster.value"
              :label="cluster.label"
              :value="cluster.value"
            ></el-option>
          </el-select>
        </div>
      </div>

      <el-table
        :data="filteredData"
        border
        stripe
        style="width: 100%"
        max-height="500"
      >
        <el-table-column
          prop="studentId"
          label="学号"
          width="120"
          fixed="left"
        ></el-table-column>
        <el-table-column
          prop="graduateName"
          label="姓名"
          width="100"
          fixed="left"
        ></el-table-column>
        <el-table-column
          prop="clusterResult"
          label="聚类结果"
          width="100"
          align="center"
        >
          <template #default="{ row }">
            <el-tag :type="getClusterTagType(row.clusterResult)">
              聚类{{ row.clusterResult }}
            </el-tag>
          </template>
        </el-table-column>

        <!-- 动态列：根据选择的属性显示 -->
        <el-table-column
          v-for="attr in selectedAttributes"
          :key="attr.value"
          :prop="attr.value"
          :label="attr.label"
          width="120"
          align="center"
        >
          <template #default="{ row }">
            <span>{{ formatValue(row[attr.value]) }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 聚类特征分析 -->
    <el-card class="analysis-card" v-if="clusterAnalysis.length > 0">
      <div slot="header">
        <span>聚类特征分析</span>
      </div>
      <el-row :gutter="20">
        <el-col
          :span="8"
          v-for="(analysis, index) in clusterAnalysis"
          :key="index"
        >
          <div class="cluster-analysis">
            <h4>聚类{{ analysis.clusterId }}</h4>
            <p>学生数量: {{ analysis.studentCount }}</p>
            <p>主要特征:</p>
            <ul>
              <li v-for="feature in analysis.topFeatures" :key="feature.name">
                {{ feature.name }}: {{ feature.value }}
              </li>
            </ul>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import * as echarts from "echarts";
import { getAcademicProfileDetail } from "@/api/academicAnalysis";

export default {
  name: "AcademicClusteringResultView",
  data() {
    return {
      modelInfo: null,
      clusterData: [],
      clusterStats: {},
      searchText: "",
      selectedCluster: null,
      selectedAttributes: [],
      clusterAnalysis: [],
      pieChart: null,
      barChart: null,
    };
  },
  computed: {
    clusterOptions() {
      return Object.keys(this.clusterStats).map((key) => ({
        value: key.replace("cluster_", ""),
        label: `聚类${key.replace("cluster_", "")}`,
      }));
    },
    filteredData() {
      let data = this.clusterData;

      // 按聚类筛选
      if (this.selectedCluster) {
        data = data.filter(
          (item) => item.clusterResult == this.selectedCluster,
        );
      }

      // 按搜索文本筛选
      if (this.searchText) {
        const searchLower = this.searchText.toLowerCase();
        data = data.filter(
          (item) =>
            item.studentId.toLowerCase().includes(searchLower) ||
            item.graduateName.toLowerCase().includes(searchLower),
        );
      }

      return data;
    },
  },
  async mounted() {
    await this.loadModelData();
    this.initCharts();
    this.generateClusterAnalysis();
  },
  beforeDestroy() {
    if (this.pieChart) {
      this.pieChart.dispose();
    }
    if (this.barChart) {
      this.barChart.dispose();
    }
  },
  methods: {
    async loadModelData() {
      try {
        const profileId = this.$route.params.profileId;
        const response = await getAcademicProfileDetail({ profileId });

        if (response.code === 200 && response.data) {
          this.modelInfo = {
            profileId: response.data.profileId,
            createTime: response.data.createTime,
            recordCount: response.data.recordCount,
            totalClusters: response.data.totalClusters || 0,
          };

          this.clusterData = response.data.data || [];
          this.clusterStats = response.data.clusterStats || {};
          this.selectedAttributes = this.getSelectedAttributes(
            response.data.attrList,
          );
        }
      } catch (error) {
        console.error("加载模型数据失败:", error);
        this.$message.error("加载模型数据失败");
      }
    },

    getSelectedAttributes(attrList) {
      const allAttributes = [
        { value: "weightedAvgGrade", label: "加权平均成绩" },
        { value: "coreCourseGrade", label: "核心课程成绩" },
        { value: "lastSemesterAvgGrade", label: "上学期平均成绩" },
        { value: "gpaGrowthRate", label: "GPA增长率" },
        { value: "gradeRanking", label: "成绩排名" },
        { value: "rankingLevel", label: "排名等级" },
        { value: "academicPerformanceScore", label: "学业表现得分" },
        { value: "researchProjectLevel", label: "科研项目级别" },
        { value: "projectRole", label: "项目角色" },
        { value: "projectAchievementTransformation", label: "项目成果转化" },
        { value: "researchProjectScore", label: "科研项目得分" },
        { value: "thesisDefenseGrade", label: "论文答辩成绩" },
        { value: "thesisInnovativeness", label: "论文创新性" },
        { value: "thesisPracticalValue", label: "论文实用价值" },
        { value: "thesisAchievementTransformation", label: "论文成果转化" },
        { value: "thesisScore", label: "论文得分" },
        { value: "publishedJournalConference", label: "发表期刊会议" },
        { value: "authorLevel", label: "作者级别" },
        { value: "isHighlyCited", label: "是否高被引" },
        { value: "isIfAbove", label: "是否影响因子以上" },
        { value: "isCoverPaper", label: "是否封面论文" },
        { value: "academicPaperScore", label: "学术论文得分" },
        { value: "competitionLevel", label: "竞赛级别" },
        { value: "awardLevel", label: "获奖级别" },
        { value: "competitionScope", label: "竞赛范围" },
        { value: "competitionTeamRole", label: "竞赛团队角色" },
        { value: "competitionScore", label: "竞赛得分" },
        { value: "patentSoftwareType", label: "专利软件类型" },
        { value: "legalStatusCoefficient", label: "法律状态系数" },
        { value: "technologyTransferAmount", label: "技术转让金额" },
        { value: "patentSoftwareScore", label: "专利软件得分" },
        { value: "hasStartupProject", label: "是否有创业项目" },
        { value: "startupFundingStage", label: "创业融资阶段" },
        { value: "isCompanyRegistered", label: "是否注册公司" },
        { value: "hasStartupCompetitionAward", label: "是否有创业竞赛获奖" },
        { value: "techApplicationEnterpriseCount", label: "技术应用企业数量" },
        { value: "techPromotionContractAmount", label: "技术推广合同金额" },
        { value: "otherInnovationAchievementScore", label: "其他创新成果得分" },
      ];

      return allAttributes.filter((attr) => attrList.includes(attr.value));
    },

    initCharts() {
      this.initPieChart();
      this.initBarChart();
    },

    initPieChart() {
      const chartDom = this.$refs.pieChart;
      this.pieChart = echarts.init(chartDom);

      const data = Object.entries(this.clusterStats).map(([key, value]) => ({
        name: `聚类${key.replace("cluster_", "")}`,
        value: value,
      }));

      const option = {
        title: {
          text: "聚类分布",
          left: "center",
        },
        tooltip: {
          trigger: "item",
          formatter: "{a} <br/>{b}: {c} ({d}%)",
        },
        legend: {
          orient: "vertical",
          left: "left",
        },
        series: [
          {
            name: "聚类分布",
            type: "pie",
            radius: "50%",
            data: data,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: "rgba(0, 0, 0, 0.5)",
              },
            },
          },
        ],
      };

      this.pieChart.setOption(option);
    },

    initBarChart() {
      const chartDom = this.$refs.barChart;
      this.barChart = echarts.init(chartDom);

      const categories = Object.keys(this.clusterStats).map(
        (key) => `聚类${key.replace("cluster_", "")}`,
      );
      const values = Object.values(this.clusterStats);

      const option = {
        title: {
          text: "聚类分布",
          left: "center",
        },
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "shadow",
          },
        },
        xAxis: {
          type: "category",
          data: categories,
        },
        yAxis: {
          type: "value",
        },
        series: [
          {
            name: "学生数量",
            type: "bar",
            data: values,
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: "#83bff6" },
                { offset: 0.5, color: "#188df0" },
                { offset: 1, color: "#188df0" },
              ]),
            },
          },
        ],
      };

      this.barChart.setOption(option);
    },

    generateClusterAnalysis() {
      this.clusterAnalysis = [];

      Object.entries(this.clusterStats).forEach(([clusterKey, count]) => {
        const clusterId = clusterKey.replace("cluster_", "");
        const clusterStudents = this.clusterData.filter(
          (student) => student.clusterResult == clusterId,
        );

        if (clusterStudents.length > 0) {
          const analysis = {
            clusterId: clusterId,
            studentCount: count,
            topFeatures: this.getTopFeatures(clusterStudents),
          };
          this.clusterAnalysis.push(analysis);
        }
      });
    },

    getTopFeatures(students) {
      const features = {};

      // 计算每个特征的平均值
      this.selectedAttributes.forEach((attr) => {
        const values = students
          .map((s) => parseFloat(s[attr.value]) || 0)
          .filter((v) => !isNaN(v));

        if (values.length > 0) {
          const avg = values.reduce((sum, val) => sum + val, 0) / values.length;
          features[attr.label] = avg.toFixed(2);
        }
      });

      // 返回前5个特征
      return Object.entries(features)
        .sort(([, a], [, b]) => parseFloat(b) - parseFloat(a))
        .slice(0, 5)
        .map(([name, value]) => ({ name, value }));
    },

    getClusterTagType(clusterResult) {
      const colors = ["", "success", "info", "warning", "danger"];
      return colors[clusterResult % colors.length] || "";
    },

    formatValue(value) {
      if (value === null || value === undefined) return "-";
      if (typeof value === "number") return value.toFixed(2);
      return value.toString();
    },

    exportResults() {
      // 导出功能实现
      this.$message.success("导出功能开发中...");
    },

    goBack() {
      this.$router.go(-1);
    },
  },
};
</script>

<style scoped>
.clustering-result-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-title {
  margin: 0;
  color: #2c3e50;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.model-info-card {
  margin-bottom: 20px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.info-item label {
  font-weight: bold;
  color: #666;
}

.charts-row {
  margin-bottom: 20px;
}

.chart-card {
  height: 400px;
}

.chart-container {
  height: 300px;
}

.detail-card {
  margin-bottom: 20px;
}

.analysis-card {
  margin-bottom: 20px;
}

.cluster-analysis {
  background: #f5f7fa;
  padding: 15px;
  border-radius: 8px;
  margin-bottom: 15px;
}

.cluster-analysis h4 {
  margin: 0 0 10px 0;
  color: #2c3e50;
}

.cluster-analysis p {
  margin: 5px 0;
  color: #666;
}

.cluster-analysis ul {
  margin: 10px 0;
  padding-left: 20px;
}

.cluster-analysis li {
  margin: 5px 0;
  color: #666;
}
</style>
