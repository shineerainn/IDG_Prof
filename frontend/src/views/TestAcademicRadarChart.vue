<template>
  <div class="test-academic-radar-chart">
    <div class="test-header">
      <h1>AcademicRadarChart 组件测试页面</h1>
      <p>用于测试和验证雷达图组件的功能</p>
    </div>

    <div class="test-controls">
      <el-button @click="loadTestData1" type="primary">加载测试数据1</el-button>
      <el-button @click="loadTestData2" type="success">加载测试数据2</el-button>
      <el-button @click="loadTestData3" type="warning">加载测试数据3</el-button>
      <el-button @click="clearData" type="danger">清空数据</el-button>
    </div>

    <div class="test-content">
      <AcademicRadarChart :radarData="testRadarData" />
    </div>

    <div class="test-info">
      <h3>当前数据：</h3>
      <pre>{{ JSON.stringify(testRadarData, null, 2) }}</pre>
    </div>
  </div>
</template>

<script>
import AcademicRadarChart from "@/components/AcademicRadarChart.vue";

export default {
  name: "TestAcademicRadarChart",
  components: {
    AcademicRadarChart,
  },
  data() {
    return {
      testRadarData: null,
    };
  },
  mounted() {
    // 默认加载测试数据1
    this.loadTestData1();
  },
  methods: {
    loadTestData1() {
      this.testRadarData = {
        studentName: "张三",
        studentId: "2021001",
        studentScores: {
          academicPerformanceScore: 85.5,
          researchProjectScore: 78.2,
          thesisScore: 82.1,
          academicPaperScore: 76.8,
          competitionScore: 88.9,
          patentSoftwareScore: 65.4,
          otherInnovationAchievementScore: 72.3,
        },
        averageScores: {
          academicPerformanceScore: 78.2,
          researchProjectScore: 72.5,
          thesisScore: 75.8,
          academicPaperScore: 70.1,
          competitionScore: 73.4,
          patentSoftwareScore: 58.9,
          otherInnovationAchievementScore: 65.2,
        },
      };
    },

    loadTestData2() {
      this.testRadarData = {
        studentName: "李四",
        studentId: "2021002",
        studentScores: {
          academicPerformanceScore: 92.1,
          researchProjectScore: 85.7,
          thesisScore: 88.3,
          academicPaperScore: 82.5,
          competitionScore: 75.2,
          patentSoftwareScore: 78.9,
          otherInnovationAchievementScore: 85.1,
        },
        averageScores: {
          academicPerformanceScore: 78.2,
          researchProjectScore: 72.5,
          thesisScore: 75.8,
          academicPaperScore: 70.1,
          competitionScore: 73.4,
          patentSoftwareScore: 58.9,
          otherInnovationAchievementScore: 65.2,
        },
      };
    },

    loadTestData3() {
      this.testRadarData = {
        studentName: "王五",
        studentId: "2021003",
        studentScores: {
          academicPerformanceScore: 65.2,
          researchProjectScore: 72.8,
          thesisScore: 68.5,
          academicPaperScore: 71.3,
          competitionScore: 82.7,
          patentSoftwareScore: 55.9,
          otherInnovationAchievementScore: 63.4,
        },
        averageScores: {
          academicPerformanceScore: 78.2,
          researchProjectScore: 72.5,
          thesisScore: 75.8,
          academicPaperScore: 70.1,
          competitionScore: 73.4,
          patentSoftwareScore: 58.9,
          otherInnovationAchievementScore: 65.2,
        },
      };
    },

    clearData() {
      this.testRadarData = null;
    },
  },
};
</script>

<style scoped>
.test-academic-radar-chart {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.test-header {
  text-align: center;
  margin-bottom: 30px;
}

.test-header h1 {
  color: #333;
  margin-bottom: 10px;
}

.test-header p {
  color: #666;
  font-size: 16px;
}

.test-controls {
  display: flex;
  justify-content: center;
  gap: 15px;
  margin-bottom: 30px;
  flex-wrap: wrap;
}

.test-content {
  margin-bottom: 30px;
}

.test-info {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.test-info h3 {
  margin-top: 0;
  color: #333;
  margin-bottom: 15px;
}

.test-info pre {
  background: #fff;
  padding: 15px;
  border-radius: 4px;
  border: 1px solid #dee2e6;
  overflow-x: auto;
  font-size: 12px;
  line-height: 1.4;
}

@media (max-width: 768px) {
  .test-controls {
    flex-direction: column;
    align-items: center;
  }

  .test-controls .el-button {
    width: 200px;
  }
}
</style>
