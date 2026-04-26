<template>
  <div class="academic-line-chart">
    <div class="chart-header">
      <h3>学业能力折线图</h3>
      <div class="student-info" v-if="chartData">
        <span>学生：{{ chartData.studentName }}</span>
        <span>学号：{{ chartData.studentId }}</span>
      </div>
    </div>

    <div class="chart-container" ref="chartContainer"></div>

    <div class="legend">
      <div class="legend-item">
        <div class="legend-color spring-color"></div>
        <span>2024年秋季学期</span>
      </div>
      <div class="legend-item">
        <div class="legend-color autumn-color"></div>
        <span>2025年春季学期</span>
      </div>
      <div class="legend-item">
        <div class="legend-color third-color"></div>
        <span>2025年秋季学期</span>
      </div>
    </div>

    <div class="score-details" v-if="chartData">
      <h4>详细得分对比</h4>
      <div class="score-table">
        <div class="score-row header">
          <div class="category">指标</div>
          <div class="first-score">2024秋季学期</div>
          <div class="second-score">2025春季学期</div>
          <div class="third-score">2025秋季学期</div>
          <div class="difference">差异</div>
        </div>
        <div
          class="score-row"
          v-for="category in categories"
          :key="category.key"
        >
          <div class="category">{{ category.name }}</div>
          <div class="first-score">{{ getFirstScore(category.key) }}</div>
          <div class="second-score">{{ getSecondScore(category.key) }}</div>
          <div class="third-score">{{ getThirdScore(category.key) }}</div>
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
  name: "AcademicLineChart",
  props: {
    chartData: {
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
    firstScores() {
      const scores = this.chartData?.firstScores || {};
      return scores;
    },
    secondScores() {
      const scores = this.chartData?.secondScores || {};
      return scores;
    },
    thirdScores() {
      const scores = this.chartData?.thirdScores || {};
      return scores;
    },
  },
  watch: {
    chartData: {
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
      if (this.chart) {
        this.chart.dispose();
      }

      // 如果没有传入数据，使用静态数据
      const useStaticData = !this.chartData;
      const categories = this.categories.map((cat) => cat.name);
      let firstData, secondData, thirdData;

      if (useStaticData) {
        // 静态数据，确保秋季学期数值大于等于春季学期
        firstData = [3.5, 4.3, 0, 1, 2, 0];
        secondData = [8.6, 5.1, 0, 2, 3, 3.4];
        thirdData = [8.8, 6.2, 2, 3, 4, 3.4];
      } else {
        if (!this.chartData || !this.$refs.chartContainer) {
          return;
        }
        firstData = this.getFirstDataArray();
        secondData = this.getSecondDataArray();
        thirdData = this.getThirdDataArray();
      }

      this.chart = echarts.init(this.$refs.chartContainer);
      const option = this.getChartOption(
        categories,
        firstData,
        secondData,
        thirdData,
        useStaticData,
      );
      this.chart.setOption(option);

      // 响应式处理
      window.addEventListener("resize", () => {
        this.chart.resize();
      });
    },

    getChartOption(
      categories,
      firstData,
      secondData,
      thirdData,
      useStaticData,
    ) {
      const title = useStaticData ? "学业能力分析" : "学业能力分析";

      return {
        title: {
          text: title,
          left: "center",
          textStyle: {
            fontSize: 16,
            fontWeight: "bold",
          },
        },
        tooltip: {
          trigger: "axis",
        },
        legend: {
          show: false,
        },
        grid: {
          left: "3%",
          right: "4%",
          bottom: "3%",
          containLabel: true,
        },
        xAxis: {
          type: "category",
          boundaryGap: false,
          data: categories,
        },
        yAxis: {
          type: "value",
          min: 0,
          max: 12,
          interval: 2,
          axisLabel: {
            formatter: "{value}分",
          },
        },
        series: [
          {
            name: "2024年秋季学期",
            type: "line",
            data: firstData,
            smooth: false, // 使用直线而非曲线
            itemStyle: {
              color: "#AF3740FF",
            },
            // 移除了areaStyle，不显示阴影
            lineStyle: {
              width: 2,
            },
          },
          {
            name: "2025年春季学期",
            type: "line",
            data: secondData,
            smooth: false, // 使用直线而非曲线
            itemStyle: {
              color: "#5470c6",
            },
            // 移除了areaStyle，不显示阴影
            lineStyle: {
              width: 2,
            },
          },
          {
            name: "2025年秋季学期",
            type: "line",
            data: thirdData,
            smooth: false, // 使用直线而非曲线
            itemStyle: {
              color: "#91cc75",
            },
            // 移除了areaStyle，不显示阴影
            lineStyle: {
              width: 2,
            },
          },
        ],
      };
    },

    getFirstDataArray() {
      const data = this.categories.map((category) => {
        const value = this.firstScores[category.key] || 0;
        return value;
      });
      return data;
    },

    getSecondDataArray() {
      const data = this.categories.map((category) => {
        const value = this.secondScores[category.key] || 0;
        return value;
      });
      return data;
    },

    getThirdDataArray() {
      const data = this.categories.map((category) => {
        const value = this.thirdScores[category.key] || 0;
        return value;
      });
      return data;
    },

    getFirstScore(categoryKey) {
      if (!this.chartData) {
        // 静态数据映射
        const staticScores = [6, 7, 5, 8, 4, 6];
        const index = this.categories.findIndex(
          (cat) => cat.key === categoryKey,
        );
        return index !== -1 ? staticScores[index].toFixed(1) : "0.0";
      }

      const score = this.firstScores[categoryKey] || 0;
      return score.toFixed(1);
    },

    getSecondScore(categoryKey) {
      if (!this.chartData) {
        // 静态数据映射
        const staticScores = [8, 9, 7, 10, 6, 8];
        const index = this.categories.findIndex(
          (cat) => cat.key === categoryKey,
        );
        return index !== -1 ? staticScores[index].toFixed(1) : "0.0";
      }

      const score = this.secondScores[categoryKey] || 0;
      return score.toFixed(1);
    },

    getThirdScore(categoryKey) {
      if (!this.chartData) {
        // 静态数据映射
        const staticScores = [8, 9, 7, 10, 6, 8];
        const index = this.categories.findIndex(
          (cat) => cat.key === categoryKey,
        );
        return index !== -1 ? staticScores[index].toFixed(1) : "0.0";
      }

      const score = this.thirdScores[categoryKey] || 0;
      return score.toFixed(1);
    },

    getDifference(categoryKey) {
      if (!this.chartData) {
        // 静态数据映射
        const staticSpringScores = [6, 7, 5, 8, 4, 6];
        const staticAutumnScores = [8, 9, 7, 10, 6, 8];
        const index = this.categories.findIndex(
          (cat) => cat.key === categoryKey,
        );
        if (index !== -1) {
          const diff = staticAutumnScores[index] - staticSpringScores[index];
          return diff > 0 ? `+${diff.toFixed(1)}` : diff.toFixed(1);
        }
        return "0.0";
      }

      const firstScore = this.firstScores[categoryKey] || 0;
      const secondScore = this.secondScores[categoryKey] || 0;
      // const thirdScore = this.thirdScores[categoryKey] || 0;
      const diff = secondScore - firstScore;
      return diff > 0 ? `+${diff.toFixed(1)}` : diff.toFixed(1);
    },

    getDifferenceClass(categoryKey) {
      if (!this.chartData) {
        // 静态数据映射
        const staticSpringScores = [6, 7, 5, 8, 4, 6];
        const staticAutumnScores = [8, 9, 7, 10, 6, 8];
        const index = this.categories.findIndex(
          (cat) => cat.key === categoryKey,
        );
        if (index !== -1) {
          const diff = staticAutumnScores[index] - staticSpringScores[index];
          if (diff > 0) {
            return "positive";
          } else if (diff < 0) {
            return "negative";
          } else {
            return "neutral";
          }
        }
        return "neutral";
      }

      const springScore = this.firstScores[categoryKey] || 0;
      const autumnScore = this.secondScores[categoryKey] || 0;
      const diff = autumnScore - springScore;

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
.academic-line-chart {
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

.spring-color {
  background-color: #af3740ff;
}

.autumn-color {
  background-color: #5470c6;
}

.third-color {
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
.third-score,
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
