<script>
import * as echarts from "echarts";
import { fetchAttrBinning } from "@/api/profileAnalysis";
import { studentFieldMap, teacherFieldMap } from "@/utils/fieldMap";
export default {
  name: "AttributeDetail",
  data() {
    return {
      role: "",
      profileId: "",
      clusterId: "",
      attributeId: "",
      chartData: [], // 存储三元组数据：[分箱名字, 本簇占比, 总体占比]
      chartInstance: null,
      attributeName: "",
    };
  },
  created() {
    if (this.$route.params) {
      this.role = this.$route.params.role;
      this.profileId = this.$route.params.profileId;
      this.clusterId = this.$route.params.clusterId;
      this.attributeId = this.$route.params.attributeId;
      this.attributeName =
        this.role === "pg"
          ? studentFieldMap[this.attributeId]
          : teacherFieldMap[this.attributeId]; // 存储属性名和中文名的映射
      console.log(this.attributeId);
      this.fetchData();
    }
  },
  methods: {
    async fetchData() {
      console.log({
        profileId: this.profileId,
        clusterId: this.clusterId,
        attributeId: this.attributeId,
      });
      try {
        const response = await fetchAttrBinning(this.role, {
          profileId: this.profileId,
          clusterId: this.clusterId,
          attributeId: this.attributeId,
        });
        response.data.map((item) => {
          this.chartData.push([item.range, item.count]);
        });

        this.$nextTick(() => {
          this.drawChart();
        });
      } catch (error) {
        console.error("获取数据失败:", error);
      }
    },
    drawChart() {
      const chartDom = document.getElementById("chart");
      if (!chartDom) {
        console.error("Chart container not found");
        return;
      }

      // 如果已经存在实例，先销毁
      if (this.chartInstance) {
        this.chartInstance.dispose();
      }

      // 创建新的实例
      this.chartInstance = echarts.init(chartDom);

      const option = {
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "shadow",
          },
          textStyle: {
            fontSize: 20,
          },
          formatter: function (params) {
            if (!params || params.length === 0) return "";

            const binName = params[0].name;
            let tooltipContent = `${binName}<br/>`;

            // 检查本簇占比数据是否存在
            if (params[0] && params[0].value !== undefined) {
              tooltipContent += `本簇数量: ${params[0].value}<br/>`;
            }

            // 检查总体占比数据是否存在
            if (params[1] && params[1].value !== undefined) {
              tooltipContent += `总体数量: ${params[1].value}`;
            }

            return tooltipContent;
          },
        },
        grid: {
          left: "3%",
          right: "4%",
          bottom: "3%",
          containLabel: true,
        },
        xAxis: {
          type: "category",
          data: this.chartData.map((item) => item[0]),
          axisLabel: {
            interval: 0,
            rotate: 0,
            fontSize: 20,
            color: "#666",
          },
          axisLine: {
            lineStyle: {
              color: "#ddd",
            },
          },
        },
        yAxis: {
          type: "value",
          name: "属性值",
          nameTextStyle: {
            fontSize: 20,
            color: "#666",
          },
          axisLabel: {
            fontSize: 20,
            color: "#666",
          },
          axisLine: {
            lineStyle: {
              color: "#ddd",
            },
          },
          splitLine: {
            lineStyle: {
              color: "#eee",
            },
          },
        },
        series: [
          {
            name: "本簇占比",
            type: "bar",
            data: this.chartData.map((item) => item[1]),
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: "#83bff6" },
                { offset: 0.5, color: "#188df0" },
                { offset: 1, color: "#188df0" },
              ]),
            },
            emphasis: {
              itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: "#2378f7" },
                  { offset: 0.7, color: "#2378f7" },
                  { offset: 1, color: "#83bff6" },
                ]),
              },
            },
          },
        ],
      };

      this.chartInstance.setOption(option);

      // 响应式调整
      window.addEventListener("resize", () => {
        this.chartInstance.resize();
      });
    },
  },
  beforeDestroy() {
    // 组件销毁前清理图表实例
    if (this.chartInstance) {
      this.chartInstance.dispose();
      this.chartInstance = null;
    }
  },
};
</script>

<template>
  <div class="attribute-detail-container">
    <div class="chart-section">
      <h2 class="section-title">{{ attributeName }}属性分布对比分析</h2>
      <div
        id="chart"
        style="width: 100%; height: 600px; margin-top: 20px"
      ></div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.attribute-detail-container {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

  .section-title {
    font-size: 20px;
    margin: 0 0 20px;
  }
}
</style>
