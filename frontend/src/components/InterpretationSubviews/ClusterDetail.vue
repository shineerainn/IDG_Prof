<script>
import * as echarts from "echarts";
import { readWideTableData } from "@/utils/excelReader";
import { fetchAttrComparison } from "@/api/profileAnalysis";
import {
  reverseStudentFieldMap,
  reverseTeacherFieldMap,
  studentFieldMap,
  teacherFieldMap,
} from "@/utils/fieldMap";
export default {
  name: "ClusterDetail",
  data() {
    return {
      role: "",
      profileId: "",
      clusterId: "",
      attributes: [], // 属性列表
      wideTableData: [], // 存储宽表数据
      tableData: [], // 属性表数据
      chartAxisData: [],
      // 图表数据
      chartData: {
        clusterValues: [], // 本簇属性平均值
        overallValues: [], // 全部样本平均值
      },
      roleMap: {
        pg: "graduate",
        supv: "supervisor",
      },
    };
  },
  async created() {
    if (this.$route.params) {
      this.role = this.$route.params.role;
      this.profileId = this.$route.params.profileId;
      this.clusterId = this.$route.params.clusterId;
      await this.fetchData();
    }
  },
  methods: {
    // 处理属性点击事件
    handleAttributeClick(row) {
      console.log(row);
      this.$router.push({
        path: `/${this.roleMap[this.role]}-profile/interpretation/${this.role}/${this.profileId}/${this.clusterId}/${row.name}`,
      });
    },
    // 从后端获取数据
    async fetchData() {
      try {
        const aresponse = await fetchAttrComparison(this.role, {
          profileId: this.profileId,
          clusterId: this.clusterId,
        });

        this.attributes = aresponse.data[0]["attributes"].map(
          (item) => item["attrName"],
        );

        const wideTableData =
          this.role === "pg"
            ? await readWideTableData("I2", "M104")
            : await readWideTableData("B2", "F104");

        this.tableData = wideTableData
          .filter((item) => this.attributes.includes(item.fieldName))
          .map((item) => ({
            id: item.id,
            name: item.fieldName,
            chineseName: item.chineseName,
            meaning: item.meaning,
            example: item.example,
          }));

        const attributeMap = Object.fromEntries(
          aresponse.data[0]["attributes"].map((attr) => [
            attr["attrName"],
            attr,
          ]),
        );
        this.tableData = wideTableData
          .filter((item) => this.attributes.includes(item.fieldName))
          .map((item) => {
            const row = attributeMap[item.fieldName];
            return {
              id: item.id,
              name: item.fieldName,
              chineseName: item.chineseName,
              meaning: item.meaning,
              example: item.example,
              clusterRowMean: row?.rowMean[0],
              allRowMean: row?.rowMean[1],
            };
          });

        for (const item of aresponse.data[0].attributes) {
          this.chartAxisData.push(
            this.role === "pg"
              ? studentFieldMap[item["attrName"]]
              : teacherFieldMap[item["attrName"]],
          );
          this.chartData.clusterValues.push(item["normValue"][0]);
          this.chartData.overallValues.push(item["normValue"][1]);
        }

        // 模拟数据加载完成
        this.$nextTick(() => {
          this.drawChart();
        });
      } catch (error) {
        console.error("获取数据失败:", error);
      }
    },
    // 绘制图表
    drawChart() {
      const chartDom = document.getElementById("chart");
      if (!chartDom) {
        console.error("Chart container not found");
        return;
      }

      const myChart = echarts.init(chartDom);
      if (!myChart) {
        console.error("Failed to initialize chart");
        return;
      }

      const option = {
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "shadow",
          },
          textStyle: {
            fontSize: 20,
          },
        },
        legend: {
          data: ["本簇样本平均值", "全部样本平均值"],
          textStyle: {
            fontSize: 20,
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
          data: this.chartAxisData,
          axisLabel: {
            interval: 0,
            rotate: 30,
            fontSize: 10,
            color: "#666",
            show: true,
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
            name: "本簇样本平均值",
            type: "bar",
            data: this.chartData.clusterValues,
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: "#83bff6" },
                { offset: 0.5, color: "#188df0" },
                { offset: 1, color: "#188df0" },
              ]),
            },
          },
          {
            name: "全部样本平均值",
            type: "bar",
            data: this.chartData.overallValues,
            itemStyle: {
              color: "#95a5a6",
              opacity: 0.7,
            },
          },
        ],
      };

      myChart.setOption(option);

      // 添加图表点击事件
      myChart.on("click", (params) => {
        if (params.componentType === "series") {
          const attributeIndex = params.dataIndex;
          const attribute = this.chartAxisData[attributeIndex];
          if (attribute) {
            this.handleAttributeClick({
              name:
                this.role === "pg"
                  ? reverseStudentFieldMap[attribute]
                  : reverseTeacherFieldMap[attribute],
            });
          }
        }
      });

      // 响应式调整
      window.addEventListener("resize", function () {
        myChart.resize();
      });
    },
  },
};
</script>

<template>
  <div class="cluster-detail-container">
    <!-- 属性表 -->
    <div class="table-section">
      <h2 class="section-title">聚类属性表</h2>
      <el-table :data="tableData" height="250" style="width: 100%" border>
        <el-table-column prop="name" label="属性名" width="235">
          <template #header>
            <span class="table-header">属性名</span>
          </template>
          <template #default="scope">
            <el-link
              v-if="
                typeof scope.row.example === 'number' &&
                !scope.row.name.includes('sn') &&
                !scope.row.name.includes('Id') &&
                !scope.row.name.includes('Number') &&
                !scope.row.name.includes('Date')
              "
              type="primary"
              @click="handleAttributeClick(scope.row)"
            >
              {{ scope.row.name }}
            </el-link>
            <span v-else>{{ scope.row.name }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="chineseName" label="中文名" width="200">
          <template #header>
            <span class="table-header">中文名</span>
          </template>
        </el-table-column>

        <el-table-column
          prop="clusterRowMean"
          label="本簇样本平均值"
          width="250"
        >
          <template #header>
            <span class="table-header">本簇样本平均值</span>
          </template>
        </el-table-column>

        <el-table-column prop="allRowMean" label="全部样本平均值" width="250">
          <template #header>
            <span class="table-header">全部样本平均值</span>
          </template>
        </el-table-column>

        <el-table-column prop="meaning" label="含义">
          <template #header>
            <span class="table-header">含义</span>
          </template>
        </el-table-column>

        <el-table-column prop="example" label="示例" width="150">
          <template #header>
            <span class="table-header">示例</span>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 对比图表 -->
    <div class="chart-section">
      <h2 class="section-title">属性对比分析</h2>
      <div
        id="chart"
        style="width: 100%; height: 500px; margin-top: 20px"
      ></div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.cluster-detail-container {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

  .section-title {
    font-size: 20px;
    margin: 0 0 20px;
  }

  .table-section {
    margin-bottom: 20px;
  }
}

::v-deep .el-table {
  th {
    background-color: #f5f7fa !important;
    font-size: 20px;
    font-weight: 600;
  }

  td {
    font-size: 20px;
  }

  .el-link {
    font-size: 20px;
  }
}
</style>
