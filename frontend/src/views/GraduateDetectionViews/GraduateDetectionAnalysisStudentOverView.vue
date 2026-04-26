<template>
  <div class="detection-container">
    <!-- 头部导航 -->
    <div class="list-header">
      <h1 class="page-title">研究生预警分数分布</h1>
      <div class="model-info">模型Id：{{ currentModel }}</div>
      <el-button
        type="primary"
        icon="el-icon-back"
        @click="goBack"
        class="back-btn"
      >
        返回建模列表
      </el-button>
    </div>

    <!-- 分数分布柱状图 -->
    <div class="chart-container">
      <div ref="scoreChart" style="width: 100%; height: 600px"></div>
    </div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import { getGraduateDetectionDistribution } from "@/api/graduateDetection";

export default {
  name: "GraduateDetectionAnalysisStudentOverView",
  data() {
    return {
      currentModel: "graduate_detection_20240402_001",
      chartInstance: null,
      scoreData: [],
    };
  },
  mounted() {
    this.currentModel = this.$route.query.modelId || "";
    this.initChart();
    this.processData();
  },
  beforeDestroy() {
    if (this.chartInstance) {
      this.chartInstance.dispose();
    }
  },
  methods: {
    initChart() {
      this.chartInstance = echarts.init(this.$refs.scoreChart);
    },

    async processData() {
      try {
        // 调用接口获取分布数据
        const response = await getGraduateDetectionDistribution({
          detectionId: this.currentModel,
          listSize: 10,
        });

        if (response && response.data) {
          // 准备图表数据，包含0-10分数段
          this.scoreData = response.data.map((count, index) => ({
            range: `${index * 10}-${(index + 1) * 10}`,
            count: count,
            minValue: index * 10,
          }));

          this.renderChart();
        } else {
          this.$message.error("获取分布数据失败");
        }
      } catch (error) {
        this.$message.error("获取分布数据失败：" + error.message);
        // 如果接口调用失败，使用模拟数据
        this.useMockData();
      }
    },

    useMockData() {
      // 生成分数段统计数据，跳过0-10分数段
      const scoreRanges = Array.from({ length: 9 }, (_, i) => ({
        min: (i + 1) * 10,
        max: (i + 2) * 10,
        count: Math.floor(Math.random() * 200),
      }));

      // 准备图表数据
      this.scoreData = scoreRanges.map((range) => ({
        range: `${range.min}-${range.max}`,
        count: range.count,
        minValue: range.min,
      }));

      this.renderChart();
    },

    renderChart() {
      const option = {
        title: {
          text: "预警分数分布柱状图",
          left: "center",
          top: 20,
        },
        tooltip: {
          trigger: "item",
          formatter: function (params) {
            if (params.data.minValue >= 95) {
              return `{special|${params.name}：${params.value}人}`;
            } else if (params.data.minValue >= 90) {
              return `{warning|${params.name}：${params.value}人}`;
            }
            return `${params.name}：${params.value}人`;
          },
          rich: {
            special: {
              color: "#c23531",
              fontWeight: "bold",
            },
            warning: {
              color: "#ffeb3b",
              fontWeight: "bold",
            },
          },
        },
        grid: {
          left: "5%",
          right: "5%",
          bottom: "15%",
          containLabel: true,
        },
        xAxis: {
          type: "category",
          data: this.scoreData.map((item) => item.range),
          axisLabel: {
            rotate: 30,
            margin: 10,
            interval: 0,
            formatter: function (value) {
              if (value === "90-100") {
                return `{special|待处理异常}`;
              } else if (value === "80-90") {
                return `{warning|潜在异常}`;
              }
              return value;
            },
            rich: {
              special: {
                color: "#c23531",
                fontWeight: "bold",
              },
              warning: {
                color: "#ffeb3b",
                fontWeight: "bold",
              },
            },
          },
          name: "分数区间",
          axisTick: {
            alignWithLabel: true,
          },
        },
        yAxis: {
          type: "value",
          name: "学生数量",
          nameLocation: "middle",
          nameGap: 40,
          min: 0,
          max: 100, // 设置最大值为100
          interval: 20, // 设置刻度间隔
          axisLabel: {
            formatter: function (value) {
              return value.toLocaleString();
            },
          },
          splitLine: {
            show: true,
          },
          axisTick: {
            show: true,
          },
        },
        series: [
          {
            data: this.scoreData.map((item) => ({
              value: item.count, // 如果超过100，显示为100
              itemStyle: {
                color: this.getRangeColor(item.minValue),
              },
              label: {
                show: true,
                position: "top",
                formatter: function (params) {
                  return params.data.count; // 显示原始值
                },
              },
            })),
            type: "bar",
            barWidth: "95%",
            barGap: "0%",
            barCategoryGap: "0%",
          },
        ],
        dataZoom: [
          {
            type: "inside",
            start: 0,
            end: 100,
          },
          {
            start: 0,
            end: 100,
          },
        ],
      };

      this.chartInstance.setOption(option);

      // 添加点击事件
      this.chartInstance.on("click", (params) => {
        if (params.componentType === "series") {
          this.goToStudentList(params.dataIndex);
        }
      });
    },

    getRangeColor(value) {
      // 根据分数段设置渐变色
      if (value >= 90) return "#c23531"; // 处理状态 - 红色
      if (value >= 80) return "#ffeb3b"; // 潜在状态 - 黄色
      if (value >= 60) return "#91c7ae"; // 浅绿
      if (value >= 40) return "#61a0a8"; // 蓝绿
      if (value >= 20) return "#2f4554"; // 深蓝
      return "#2f4554"; // 深蓝
    },

    goBack() {
      this.$router.push("/graduate-detection/analysis");
    },
    goToStudentList(barIndex) {
      this.$router.push({
        path: "/graduate-detection/analysis/student-list",
        query: {
          modelId: this.currentModel,
          barIndex: barIndex,
        },
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.detection-container {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

  .list-header {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
    padding: 0 20px;

    .page-title {
      flex: 1;
      font-size: 24px;
      color: #2c3e50;
      margin: 0;
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
      margin-right: 10px;
    }
  }

  .chart-container {
    margin-top: 40px;
    border: 1px solid #ebeef5;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  }
}
</style>
