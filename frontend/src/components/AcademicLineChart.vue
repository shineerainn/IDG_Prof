<template>
  <div class="academic-line-chart">
    <div class="chart-header">
      <h3>学业能力折线图</h3>
      <div class="student-info" v-if="lineData">
        <span>学生：{{ lineData.studentName }}</span>
        <span>学号：{{ lineData.studentId }}</span>
      </div>
    </div>

    <div class="chart-container" ref="chartContainer"></div>

    <div class="legend">
      <div
        class="legend-item"
        v-for="category in categories"
        :key="category.key"
      >
        <div
          class="legend-color"
          :style="{
            backgroundColor: getColorByIndex(categories.indexOf(category)),
          }"
        ></div>
        <span>{{ category.name }}</span>
      </div>
    </div>

    <!--    <div class="score-details" v-if="lineData">-->
    <!--      <h4>详细得分对比</h4>-->
    <!--      <div class="score-table">-->
    <!--        <div class="score-row header">-->
    <!--          <div class="category">指标</div>-->
    <!--          <div class="first-score">2023春季学期</div>-->
    <!--          <div class="second-score">2023秋季学期</div>-->
    <!--          <div class="third-score">2024春季学期</div>-->
    <!--          <div class="fourth-score">2024秋季学期</div>-->
    <!--          <div class="fifth-score">2025春季学期</div>-->
    <!--          <div class="sixth-score">2025秋季学期</div>-->
    <!--        </div>-->
    <!--        <div-->
    <!--          class="score-row"-->
    <!--          v-for="category in categories"-->
    <!--          :key="category.key"-->
    <!--        >-->
    <!--          <div class="category">{{ category.name }}</div>-->
    <!--          <div class="first-score">-->
    <!--            {{ getScoreBySemester(category.key, 0) }}-->
    <!--          </div>-->
    <!--          <div class="second-score">-->
    <!--            {{ getScoreBySemester(category.key, 1) }}-->
    <!--          </div>-->
    <!--          <div class="third-score">-->
    <!--            {{ getScoreBySemester(category.key, 2) }}-->
    <!--          </div>-->
    <!--          <div class="fourth-score">-->
    <!--            {{ getScoreBySemester(category.key, 3) }}-->
    <!--          </div>-->
    <!--          <div class="fifth-score">-->
    <!--            {{ getScoreBySemester(category.key, 4) }}-->
    <!--          </div>-->
    <!--          <div class="sixth-score">-->
    <!--            {{ getScoreBySemester(category.key, 5) }}-->
    <!--          </div>-->
    <!--        </div>-->
    <!--      </div>-->
    <!--    </div>-->
  </div>
</template>

<script>
import * as echarts from "echarts";

export default {
  name: "AcademicLineChart",
  props: {
    lineData: {
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
      semesters: [
        "2023春季学期",
        "2023秋季学期",
        "2024春季学期",
        "2024秋季学期",
        "2025春季学期",
        "2025秋季学期",
      ],
      // 为每个类别定义颜色
      colors: [
        "#5470c6",
        "#91cc75",
        "#fac858",
        "#ee6666",
        "#73c0de",
        "#3ba272",
      ],
    };
  },
  watch: {
    lineData: {
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
    getColorByIndex(index) {
      return this.colors[index % this.colors.length];
    },

    initChart() {
      if (this.chart) {
        this.chart.dispose();
      }

      if (!this.lineData || !this.$refs.chartContainer) {
        return;
      }

      const semesters = this.semesters;

      this.chart = echarts.init(this.$refs.chartContainer);

      // 直接使用传入的数据
      const option = this.getDynamicChartOption(semesters);
      this.chart.setOption(option);

      // 响应式处理
      const resizeHandler = () => {
        if (this.chart) {
          this.chart.resize();
        }
      };
      window.addEventListener("resize", resizeHandler);
    },

    getDynamicChartOption(semesters) {
      if (!this.lineData) {
        return {};
      }

      // 从传入数据构建系列数据
      const seriesData = this.categories.map((category, index) => {
        const data = [
          this.lineData.semester2023Spring?.[category.key] || 0,
          this.lineData.semester2023Autumn?.[category.key] || 0,
          this.lineData.semester2024Spring?.[category.key] || 0,
          this.lineData.semester2024Autumn?.[category.key] || 0,
          this.lineData.semester2025Spring?.[category.key] || 0,
          this.lineData.semester2025Autumn?.[category.key] || 0,
        ];

        return {
          name: category.name,
          type: "line",
          data: data,
          itemStyle: { color: this.colors[index] },
          lineStyle: { width: 2 },
        };
      });

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
          data: semesters,
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
        series: seriesData,
      };
    },

    getScoreBySemester(categoryKey, semesterIndex) {
      if (!this.lineData) {
        return "0.0";
      }

      // 动态数据映射
      const semesterKeys = [
        "semester2023Spring",
        "semester2023Autumn",
        "semester2024Spring",
        "semester2024Autumn",
        "semester2025Spring",
        "semester2025Autumn",
      ];

      const semesterKey = semesterKeys[semesterIndex];
      const score = this.lineData[semesterKey]?.[categoryKey] || 0;
      return score.toFixed(1);
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
  flex-wrap: wrap;
  gap: 20px;
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
  grid-template-columns: 2fr repeat(6, 1fr);
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
.fourth-score,
.fifth-score,
.sixth-score {
  text-align: center;
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

  .legend {
    gap: 10px;
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
