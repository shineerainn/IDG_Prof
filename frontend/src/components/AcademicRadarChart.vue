<template>
  <div class="academic-radar-chart">
    <div class="chart-header">
      <h3>学业能力雷达图</h3>
      <div class="student-info" v-if="radarData">
        <span>学生：{{ radarData.studentName }}</span>
        <span>学号：{{ radarData.studentId }}</span>
      </div>
    </div>

    <div class="chart-container" ref="chartContainer"></div>

    <div class="legend">
      <div class="legend-item">
        <div class="legend-color student-color"></div>
        <span>个人得分</span>
      </div>
      <div class="legend-item">
        <div class="legend-color average-color"></div>
        <span>同年级平均分</span>
      </div>
    </div>

    <div class="score-details" v-if="radarData">
      <h4>详细得分对比</h4>
      <div class="score-table">
        <div class="score-row header">
          <div class="category">指标</div>
          <div class="first-score">个人得分</div>
          <div class="second-score">平均分</div>
          <div class="difference">差异</div>
        </div>
        <div
          class="score-row"
          v-for="category in categories"
          :key="category.key"
        >
          <div class="category">{{ category.name }}</div>
          <div class="first-score">{{ getStudentScore(category.key) }}</div>
          <div class="second-score">{{ getAverageScore(category.key) }}</div>
          <div class="difference" :class="getDifferenceClass(category.key)">
            {{ getDifference(category.key) }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from "echarts";

export default {
  name: "AcademicRadarChart",
  props: {
    radarData: {
      type: Object,
      default: null,
    },
  },
  data() {
    return {
      chart: null,
      categories: [
        { key: "academicPerformanceScore", name: "学业表现" },
        { key: "researchProjectScore", name: "科研项目" },
        { key: "thesisScore", name: "学位论文" },
        { key: "academicPaperScore", name: "学术论文" },
        { key: "competitionScore", name: "竞赛获奖" },
        { key: "patentSoftwareScore", name: "专利软著" },
      ],
    };
  },
  computed: {
    studentScores() {
      const scores = this.radarData?.studentScores || {};
      console.log("学生得分数据:", scores);
      return scores;
    },
    averageScores() {
      const scores = this.radarData?.averageScores || {};
      console.log("平均得分数据:", scores);
      return scores;
    },
  },
  watch: {
    radarData: {
      handler(newData) {
        if (newData) {
          this.$nextTick(() => {
            this.initChart();
          });
        }
      },
      immediate: true,
    },
  },
  mounted() {
    this.initChart();
  },
  beforeUnmount() {
    if (this.chart) {
      this.chart.dispose();
    }
  },
  methods: {
    initChart() {
      if (!this.radarData || !this.$refs.chartContainer) {
        return;
      }

      if (this.chart) {
        this.chart.dispose();
      }

      this.chart = echarts.init(this.$refs.chartContainer);
      const option = this.getChartOption();
      this.chart.setOption(option);

      // 响应式处理
      window.addEventListener("resize", () => {
        this.chart.resize();
      });
    },

    getChartOption() {
      const studentData = this.getStudentDataArray();
      const averageData = this.getAverageDataArray();

      return {
        title: {
          text: "学业能力分析",
          left: "center",
          textStyle: {
            fontSize: 16,
            fontWeight: "bold",
          },
        },
        tooltip: {
          trigger: "item",
          formatter: function (params) {
            return `${params.name}<br/>${params.seriesName}: ${params.value}`;
          },
        },
        legend: {
          show: false,
        },
        radar: {
          indicator: this.categories.map((category) => ({
            name: category.name,
            max: 12,
            min: 0,
          })),
          radius: "65%",
          center: ["50%", "55%"],
          splitNumber: 5,
          axisName: {
            color: "#333",
            fontSize: 12,
          },
          splitLine: {
            lineStyle: {
              color: "#ddd",
            },
          },
          splitArea: {
            show: true,
            areaStyle: {
              color: ["rgba(250, 250, 250, 0.3)", "rgba(200, 200, 200, 0.1)"],
            },
          },
          axisLine: {
            lineStyle: {
              color: "#ddd",
            },
          },
        },
        series: [
          {
            name: "个人得分",
            type: "radar",
            data: [
              {
                value: studentData,
                name: "个人得分",
                itemStyle: {
                  color: "#5470c6",
                },
                areaStyle: {
                  color: "rgba(84, 112, 198, 0.3)",
                },
                lineStyle: {
                  width: 2,
                },
              },
            ],
          },
          {
            name: "同年级平均分",
            type: "radar",
            data: [
              {
                value: averageData,
                name: "同年级平均分",
                itemStyle: {
                  color: "#91cc75",
                },
                areaStyle: {
                  color: "rgba(145, 204, 117, 0.3)",
                },
                lineStyle: {
                  width: 2,
                },
              },
            ],
          },
        ],
      };
    },

    getStudentDataArray() {
      const data = this.categories.map((category) => {
        const value = this.studentScores[category.key] || 0;
        console.log(`学生数据 ${category.name}: ${value}`);
        return value;
      });
      console.log("学生数据数组:", data);
      return data;
    },

    getAverageDataArray() {
      const data = this.categories.map((category) => {
        const value = this.averageScores[category.key] || 0;
        console.log(`平均数据 ${category.name}: ${value}`);
        return value;
      });
      console.log("平均数据数组:", data);
      return data;
    },

    getStudentScore(categoryKey) {
      const score = this.studentScores[categoryKey] || 0;
      return score.toFixed(1);
    },

    getAverageScore(categoryKey) {
      const score = this.averageScores[categoryKey] || 0;
      return score.toFixed(1);
    },

    getDifference(categoryKey) {
      const studentScore = this.studentScores[categoryKey] || 0;
      const averageScore = this.averageScores[categoryKey] || 0;
      const diff = studentScore - averageScore;
      return diff > 0 ? `+${diff.toFixed(1)}` : diff.toFixed(1);
    },

    getDifferenceClass(categoryKey) {
      const studentScore = this.studentScores[categoryKey] || 0;
      const averageScore = this.averageScores[categoryKey] || 0;
      const diff = studentScore - averageScore;

      if (diff > 0) {
        return "positive";
      } else if (diff < 0) {
        return "negative";
      } else {
        return "neutral";
      }
    },
  },
};
</script>

<style scoped>
.academic-radar-chart {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.chart-header h3 {
  margin: 0;
  color: #333;
  font-size: 18px;
}

.student-info {
  display: flex;
  gap: 20px;
  font-size: 14px;
  color: #666;
}

.chart-container {
  width: 100%;
  height: 400px;
  margin: 20px 0;
}

.legend {
  display: flex;
  justify-content: center;
  gap: 30px;
  margin: 20px 0;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
}

.legend-color {
  width: 16px;
  height: 16px;
  border-radius: 2px;
}

.student-color {
  background-color: #5470c6;
}

.average-color {
  background-color: #91cc75;
}

.score-details {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.score-details h4 {
  margin: 0 0 15px 0;
  color: #333;
  font-size: 16px;
}

.score-table {
  border: 1px solid #eee;
  border-radius: 4px;
  overflow: hidden;
}

.score-row {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1fr;
  padding: 12px 15px;
  border-bottom: 1px solid #eee;
}

.score-row:last-child {
  border-bottom: none;
}

.score-row.header {
  background-color: #f8f9fa;
  font-weight: bold;
  color: #333;
}

.category {
  font-weight: 500;
}

.first-score,
.second-score,
.difference {
  text-align: center;
}

.difference.positive {
  color: #52c41a;
  font-weight: bold;
}

.difference.negative {
  color: #ff4d4f;
  font-weight: bold;
}

.difference.neutral {
  color: #666;
}

@media (max-width: 768px) {
  .chart-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .student-info {
    flex-direction: column;
    gap: 5px;
  }

  .chart-container {
    height: 300px;
  }

  .score-row {
    grid-template-columns: 1fr;
    gap: 5px;
  }

  .score-row.header {
    display: none;
  }

  .category {
    font-weight: bold;
    color: #333;
  }
}
</style>
