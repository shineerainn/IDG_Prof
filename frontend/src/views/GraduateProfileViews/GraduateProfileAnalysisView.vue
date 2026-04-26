<template>
  <div class="detection-container">
    <!-- 标题 -->
    <h1 class="page-title">研究生画像建模列表</h1>

    <!-- 模型表格 -->
    <el-table
      :data="
        tableData.slice((currentPage - 1) * pageSize, currentPage * pageSize)
      "
      border
      stripe
      style="width: 100%; margin-bottom: 20px"
      class="compact-table"
    >
      <el-table-column prop="modelId" label="模型编号" width="280">
        <template #header>
          <span class="table-header">模型编号</span>
        </template>
        <template #default="scope">
          <el-button type="text" @click="navigateToDetail(scope.$index)">{{
            scope.row.modelId
          }}</el-button>
        </template>
      </el-table-column>
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
import { fetchGraduateProfile } from "@/api/graduateProfile";
import { mapState } from "vuex";

export default {
  name: "GraduateProfileAnalysisView",
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
        const response = await fetchGraduateProfile({
          userId: this.userId,
        });

        // 数据映射
        const responseData = response.data || [];
        const mapDataToTable = (data) => {
          return data.map((item) => ({
            modelId: item.profileId, // profileId → modelId
            algorithmType: item.algorithmType, // 转换算法类型名称
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
        "k-means": "success",
        深度神经网络: "warning",
        支持向量机: "danger",
        逻辑回归: "info",
      };
      return typeMap[type] || "";
    },

    navigateToDetail(id) {
      this.$router.push({
        path:
          "/graduate-profile/interpretation/pg/" + this.tableData[id].modelId,
        params: {
          studentId: this.tableData[id].modelId,
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

  .page-title {
    text-align: center;
    color: #2c3e50;
    margin-bottom: 30px;
    font-size: 24px;
  }
  .attribute-list {
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
    .pagination-container {
      display: flex;
      justify-content: flex-end;
      margin-top: 20px;

      ::v-deep .el-table {
        th {
          background-color: #f5f7fa !important;
          font-weight: 600;
        }

        .el-table__body tr:hover > td {
          background-color: #f0f7ff !important;
        }
      }
    }
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
      padding: 6px 0; // 调整单元格内边距
    }

    tr:hover > td {
      background-color: #f0f7ff !important;
    }
  }
}
</style>
