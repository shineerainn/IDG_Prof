<template>
  <div class="detection-container">
    <!-- 标题 -->
    <h1 class="page-title">研究生预警建模列表</h1>

    <!-- 模型表格 -->
    <el-table
      :data="
        tableData.slice((currentPage - 1) * pageSize, currentPage * pageSize)
      "
      border
      stripe
      style="width: 100%; margin-bottom: 20px"
      @row-click="handleRowClick"
      class="compact-table"
    >
      <el-table-column
        prop="modelId"
        label="模型编号"
        width="300"
        align="center"
      />
      <el-table-column prop="algorithmType" label="算法类型" width="150">
        <template #default="{ row }">
          <el-tag :type="getAlgorithmTagType(row.algorithmType)">
            {{ row.algorithmType }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="创建时间"
        width="180"
        sortable
      />
      <el-table-column
        prop="attributeCount"
        label="属性数量"
        width="100"
        align="center"
      />
      <el-table-column prop="attributes" label="属性列表">
        <template #default="{ row }">
          <el-tooltip
            effect="dark"
            :content="row.attributes.join(', ')"
            placement="top"
          >
            <span class="attribute-list">{{ row.attributes.join(", ") }}</span>
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      background
      layout="total, sizes, prev, pager, next, jumper"
      :total="tableData.length"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :current-page="currentPage"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      class="pagination-container"
    />
  </div>
</template>

<script>
import { getGraduateDetectionList } from "@/api/graduateDetection";
import { mapState } from "vuex";

export default {
  name: "GraduateDetectionAnalysisView",
  computed: {
    ...mapState({
      userId: (state) => state.user.id,
    }),
  },
  data() {
    return {
      currentPage: 1,
      pageSize: 10,
      tableData: [],
      loading: false,
    };
  },
  async mounted() {
    await this.fetchModelList();
  },
  methods: {
    async fetchModelList() {
      if (!this.userId) {
        this.$message.error("用户未登录");
        return;
      }

      this.loading = true;
      try {
        const response = await getGraduateDetectionList({
          userId: this.userId,
        });

        // 数据映射
        const responseData = response.data || [];
        const mapDataToTable = (data) => {
          // 算法类型映射表（根据实际需求扩展）
          const algorithmTypeMap = {
            id3Tree: "决策树（ID3）",
            dnn: "深度神经网络", // 示例扩展
          };

          return data.map((item) => ({
            modelId: item.detctId, // detctId → modelId
            algorithmType:
              algorithmTypeMap[item.algorithmType] || item.algorithmType, // 转换算法类型名称
            createTime: formatDateTime(item.createTime), // 格式化时间
            attributeCount: Number(item.attributeAmount), // 字符串转数字
            attributes: parseAttributeList(item.attributeList), // 字符串转数组
          }));
        };

        // 时间格式化工具函数
        const formatDateTime = (isoString) => {
          const date = new Date(isoString);
          return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(date.getHours())}:${pad(date.getMinutes())}`;
        };

        // 补零函数
        const pad = (num) => num.toString().padStart(2, "0");

        // 属性列表解析函数
        const parseAttributeList = (str) => {
          try {
            // 处理类似 "[a, b, c]" 的字符串
            return str
              .replace(/[[\]]/g, "")
              .split(",")
              .map((s) => s.trim());
          } catch {
            return [];
          }
        };

        // 应用映射结果
        this.tableData = mapDataToTable(responseData);
      } catch (error) {
        this.$message.error("获取模型列表失败：" + error.message);
        // 模拟数据
        this.tableData = [];
      } finally {
        this.loading = false;
      }
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.currentPage = 1;
    },
    handleCurrentChange(val) {
      this.currentPage = val;
    },
    getAlgorithmTagType(type) {
      const typeMap = {
        决策树: "success",
        深度神经网络: "warning",
        支持向量机: "danger",
        逻辑回归: "info",
      };
      return typeMap[type] || "";
    },
    handleRowClick(row) {
      this.$router.push({
        path: "/graduate-detection/analysis/student-overview",
        query: { modelId: row.modelId },
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

  .page-title {
    text-align: center;
    color: #2c3e50;
    margin-bottom: 20px;
    font-size: 24px;
  }

  .attribute-list {
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .pagination-container {
    display: flex;
    justify-content: flex-end;
    margin-top: 15px;
  }
}

::v-deep .compact-table {
  .el-table__header-wrapper {
    th {
      background-color: #f5f7fa !important;
      font-weight: 600;
      padding: 8px 0;
    }
  }

  .el-table__body-wrapper {
    td {
      padding: 6px 0;
    }

    tr:hover > td {
      background-color: #f0f7ff !important;
    }
  }
}
</style>
