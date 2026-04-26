<script>
import * as echarts from "echarts";
import { fetchClusters, fetchProfile } from "@/api/profileAnalysis";
import { readWideTableData } from "@/utils/excelReader";
export default {
  name: "ClusterOverview",
  data() {
    return {
      role: "",
      profileId: "",
      chartInstance: null,
      isEditing: false, // 是否处于编辑状态
      attributes: [], // 属性列表
      attributeNames: {}, // 存储属性名和中文名的映射
      isLoading: true, // 添加加载状态
      clusters: [], // 统一管理簇的数据
      clusterNames: [
        "主体群体",
        "核心群体",
        "主流群体",
        "中坚群体",
        "小众群体",
        "边缘群体",
        "微量群体",
        "零星个体",
      ],
      roleMap: {
        pg: "graduate",
        supv: "supervisor",
      },
    };
  },
  computed: {
    clusterDistributionData() {
      // 如果属性列表为空，使用默认属性
      const attrs = this.attributes;
      return this.clusters.map((cluster) => {
        const row = {
          id: cluster.id,
          cluster: cluster.name,
        };
        // 为每个属性添加值
        attrs.forEach((attr, idx) => {
          row[attr] = cluster.attributes[idx];
        });
        return row;
      });
    },
  },
  async created() {
    if (this.$route.params.role && this.$route.params.profileId) {
      this.role = this.$route.params.role;
      this.profileId = this.$route.params.profileId;
      this.isLoading = true;
      try {
        await this.readExcel();
        await this.fetchData();
      } catch (error) {
        console.error("数据加载失败:", error);
      } finally {
        this.isLoading = false;
      }
    }
  },
  methods: {
    async readExcel() {
      // 读取Excel文件获取中文名
      const tableData =
        this.role === "pg"
          ? await readWideTableData("I2", "M104")
          : await readWideTableData("B2", "F104");
      this.attributeNames = tableData.reduce((acc, item) => {
        if (item.fieldName && item.chineseName) {
          acc[item.fieldName] = item.chineseName;
        }
        return acc;
      }, {});
    },
    async fetchData() {
      try {
        const profileResponse = await fetchProfile(this.role, this.profileId);
        // 确保属性列表不为空
        if (profileResponse.data.attributeList) {
          this.attributes = profileResponse.data.attributeList
            .slice(1, -1)
            .split(",")
            .map((s) => s.trim());
        }

        const clustersResponse = await fetchClusters(this.role, this.profileId);
        if (clustersResponse.data) {
          clustersResponse.data.sort((a, b) => b.value - a.value);

          this.clusters = clustersResponse.data.map((item, idx) => ({
            id: item.id,
            name: this.clusterNames[idx],
            value: item.value,
            attributes: item.attributes.map((attr) => {
              if (typeof attr === "boolean") return attr ? "是" : "否";
              else return attr;
            }),
          }));
        }

        // 确保数据加载完成后重新渲染图表
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

      if (this.chartInstance) {
        this.chartInstance.dispose();
      }

      this.chartInstance = echarts.init(chartDom);

      // 定义绿色系的彩虹色
      const colors = [
        "#2ecc71", // 翠绿
        "#27ae60", // 森林绿
        "#20a085", // 青绿
        "#1abc9c", // 绿松石
        "#3498db", // 天蓝
      ];

      const option = {
        title: {
          text: this.role === "pg" ? "研究生画像聚类分布" : "导师画像聚类分布",
          left: "center",
          textStyle: {
            fontSize: 20,
            fontWeight: "bold",
            color: "#333",
          },
        },
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "shadow",
          },
          formatter: "{b}: {c}名",
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
          data: this.clusters.map((item) => item.name),
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
          name: "数量",
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
            data: this.clusters.map((item, index) => ({
              value: item.value,
              name: item.name,
              id: item.id,
              itemStyle: {
                color: colors[index % colors.length],
                opacity: 0.9,
              },
            })),
            type: "bar",
            barWidth: "40%",
            label: {
              show: true,
              position: "inside",
              formatter: "{c}",
              fontSize: 20,
              color: "#fff",
            },
          },
        ],
      };

      this.chartInstance.setOption(option);

      // 添加点击事件
      this.chartInstance.on("click", (params) => {
        if (params.data.id) {
          this.$router.push({
            path: `/${this.roleMap[this.role]}-profile/interpretation/${this.role}/${this.profileId}/${params.data.id}`,
          });
        }
      });

      // 响应式调整
      window.addEventListener("resize", () => {
        this.chartInstance.resize();
      });
    },
    // 处理名称修改
    handleNameChange(row) {
      const target = this.clusters.find((item) => item.id === row.id);
      if (target) {
        target.name = row.cluster;
      }
      this.drawChart();
    },

    // 切换编辑状态
    toggleEdit() {
      this.isEditing = !this.isEditing;
    },

    // 处理表格行点击
    handleTableRowClick(row) {
      if (this.isEditing) return; // 编辑状态下不触发跳转
      const cluster = this.clusters.find((item) => item.name === row.cluster);
      if (cluster && cluster.id) {
        this.$router.push({
          path: `/${this.roleMap[this.role]}-profile/interpretation/${this.role}/${this.profileId}/${cluster.id}`,
        });
      }
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
  <div class="cluster-overview-container">
    <div class="chart-section">
      <div id="chart" style="width: 100%; height: 600px"></div>
    </div>

    <div class="table-section">
      <h2 class="section-title">聚类特征分布</h2>
      <el-table
        v-loading="isLoading"
        :data="clusterDistributionData"
        border
        stripe
        style="width: 100%"
        :header-cell-style="{
          background: '#f5f7fa',
          color: '#606266',
          fontSize: '20px',
        }"
        :cell-style="{ fontSize: '20px' }"
      >
        <el-table-column
          fixed
          prop="cluster"
          label="聚类簇"
          width="200"
          align="center"
        >
          <template #header>
            <div class="header-cell">
              <span>聚类簇</span>
              <el-button
                type="text"
                class="edit-button"
                @click.stop="toggleEdit"
              >
                <i :class="isEditing ? 'el-icon-check' : 'el-icon-edit'"></i>
              </el-button>
            </div>
          </template>
          <template #default="scope">
            <div class="cluster-cell">
              <template v-if="isEditing">
                <el-input
                  v-model="scope.row.cluster"
                  @blur="handleNameChange(scope.row)"
                />
              </template>
              <template v-else>
                <el-link
                  type="primary"
                  style="font-size: 20px"
                  @click="handleTableRowClick(scope.row)"
                  >{{ scope.row.cluster }}</el-link
                >
              </template>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          v-for="(attr, index) in attributes"
          :key="index"
          :prop="attr"
          :label="attributeNames[attr]"
          align="center"
          width="150"
        ></el-table-column>
      </el-table>
    </div>
  </div>
</template>

<style scoped lang="scss">
.cluster-overview-container {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.chart-section {
  margin-bottom: 20px;
}

.table-section {
  //width: 1500px;
  //overflow-x: auto;
  margin-top: 20px;
}

.section-title {
  font-size: 20px;
  margin: 0 0 20px;
}

.header-cell {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.cluster-cell {
  display: flex;
  align-items: center;
  justify-content: center;
}

.edit-button {
  padding: 0;
  margin-left: 4px;
  font-size: 20px;
  color: #909399;

  &:hover {
    color: #409eff;
  }
}

.cluster-link {
  color: #409eff;
  cursor: pointer;
  &:hover {
    text-decoration: underline;
  }
}
</style>
