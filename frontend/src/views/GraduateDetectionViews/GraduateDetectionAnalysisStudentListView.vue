<template>
  <div class="detection-container">
    <!-- 头部导航 -->
    <div class="list-header">
      <h1 class="page-title">研究生预警学生列表</h1>
      <div class="model-info">模型Id：{{ currentModel }}</div>
      <el-button
        type="primary"
        icon="el-icon-back"
        @click="goBack"
        class="back-btn"
      >
        返回预警分布
      </el-button>
    </div>

    <!-- 学生表格 -->
    <el-table
      :data="
        tableData.slice((currentPage - 1) * pageSize, currentPage * pageSize)
      "
      border
      stripe
      style="width: 100%; margin: 20px 0; cursor: pointer"
      @row-click="handleRowClick"
    >
      <el-table-column prop="name" label="姓名" width="120" align="center" />
      <el-table-column
        prop="warningType"
        label="预警类型"
        width="120"
        align="center"
      >
        <template #default="{ row }">
          <el-tag :type="getScoreColor(row.warningScore)">
            {{
              getScoreColor(row.warningScore) === "success"
                ? "无明显异常"
                : warningTypes[row.warningType]
            }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="warningScore"
        label="预警分数"
        width="120"
        align="center"
      >
        <template #default="{ row }">
          <span class="warning-score" :class="getScoreColor(row.warningScore)">
            {{ row.warningScore }}
          </span>
        </template>
      </el-table-column>
      <el-table-column
        prop="studentId"
        label="学号"
        width="180"
        align="center"
      />
      <el-table-column prop="college" label="学院" align="center">
        <template #default="{ row }"> {{ row.college }} </template>
      </el-table-column>
      <el-table-column prop="major" label="专业" width="220" align="center">
        <template #default="{ row }"> {{ row.major }} </template>
      </el-table-column>
      <el-table-column prop="gender" label="性别" width="100" align="center" />
      <el-table-column prop="age" label="年龄" width="100" align="center" />
      <el-table-column prop="grade" label="年级" width="180" align="center">
        <template #default="{ row }"> {{ row.grade }} </template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号" width="150" align="center">
        <template #default="{ row }">
          {{ row.phone }}
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      background
      layout="total, sizes, prev, pager, next"
      :total="tableData.length"
      :page-sizes="[10, 20, 50]"
      :page-size="pageSize"
      :current-page.sync="currentPage"
      class="pagination-container"
    />
  </div>
</template>

<script>
import { getGraduateDetectionStudentList } from "@/api/graduateDetection";
export default {
  name: "GraduateDetectionAnalysisStudentListView",
  data() {
    return {
      currentPage: 1,
      pageSize: 10,
      currentModel: "",
      barIndex: 0,
      warningTypes: {
        1: "学业异常",
        2: "毕设异常",
        3: "就业异常",
        4: "生活异常",
      },
      warningTypeTag: {
        1: "danger",
        2: "warning",
        3: "primary",
        4: "info",
      },
      // 指数映射表
      indexMappings: {
        academicIndex: 1, // 学业异常
        graduationDesignIndex: 2, // 毕设异常
        employmentIndex: 3, // 就业异常
        livingIndex: 4, // 生活异常
      },
      tableData: [],
    };
  },
  mounted() {
    // 获取路由参数
    this.currentModel = this.$route.query.modelId || "";
    this.barIndex = parseInt(this.$route.query.barIndex) || 0;

    if (!this.currentModel) {
      this.$message.error("缺少模型ID参数");
      this.$router.push("/graduate-detection/analysis");
      return;
    }

    this.fetchStudentList();
  },
  methods: {
    async fetchStudentList() {
      try {
        const response = await getGraduateDetectionStudentList({
          detectionId: this.currentModel,
          listSize: 10,
          index: this.barIndex,
        });

        if (response && response.data) {
          // 转换数据格式
          this.tableData = response.data.map((item) => {
            // 获取四个指数的值
            const indices = {
              academicIndex: parseFloat(item.academicIndex) || 0,
              graduationDesignIndex:
                parseFloat(item.graduationDesignIndex) || 0,
              employmentIndex: parseFloat(item.employmentIndex) || 0,
              livingIndex: parseFloat(item.livingIndex) || 0,
            };

            // 找到最低的指数
            const minIndex = Math.min(...Object.values(indices));
            const minIndexKey = Object.keys(indices).find(
              (key) => indices[key] === minIndex,
            );

            // 设置预警类型
            const warningType = this.indexMappings[minIndexKey] || 4; // 默认为综合预警

            return {
              name: item.graduateName,
              studentId: item.studentId,
              college: item.school,
              major: item.major,
              gender: item.gender,
              age: item.currentAge,
              grade: item.currentGrade,
              phone: item.cellPhoneNumber,
              warningType: warningType,
              warningScore: Math.round(item.confidence * 100),
            };
          });

          // 按预警分数降序排序
          this.tableData.sort((a, b) => b.warningScore - a.warningScore);

          // 计算百分比
          const total = this.tableData.length;
          this.tableData = this.tableData.map((item, index) => ({
            ...item,
            percentile: (((index + 1) / total) * 100).toFixed(2),
          }));
        } else {
          this.$message.error("获取学生列表失败");
        }
      } catch (error) {
        this.$message.error("获取学生列表失败：" + error.message);
        // 如果接口调用失败，使用模拟数据
        this.useMockData();
      }
    },
    goBack() {
      this.$router.push({
        path: "/graduate-detection/analysis/student-overview",
        query: { modelId: this.currentModel },
      });
    },
    handleRowClick(row) {
      const color = this.getScoreColor(row.warningScore);
      this.$router.push({
        path: "/graduate-detection/analysis/student-detail",
        query: {
          modelId: this.currentModel,
          studentId: row.studentId,
          color: color,
          warningType: row.warningType,
          warningScore: row.warningScore,
        },
      });
    },
    getScoreColor(score) {
      if (score >= 90) return "danger"; // 处理状态 - 红色
      if (score >= 80) return "warning"; // 潜在状态 - 黄色
      if (score >= 60) return "primary"; // 浅绿
      if (score >= 40) return "info"; // 蓝绿
      if (score >= 20) return ""; // 深蓝
      return "success"; // 其他绿色
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
    }
  }

  .pagination-container {
    display: flex;
    justify-content: flex-end;
    margin-top: 20px;
  }

  .level-text {
    font-weight: bold;
    color: #606266;
  }

  .text-ellipsis {
    display: inline-block;
    max-width: 100%;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .phone-number {
    letter-spacing: 0.5px;
    font-family: monospace;
  }

  ::v-deep .el-table {
    th {
      background-color: #f5f7fa !important;
      font-weight: 600;
    }

    .el-table__body tr:hover > td {
      background-color: #f0f7ff !important;
    }

    // 调整固定列宽
    .el-table__header-wrapper .el-table__header {
      th {
        padding: 12px 0;
      }
    }
  }

  .warning-score {
    font-size: 16px;
    font-weight: bold;
    padding: 4px 8px;
    border-radius: 4px;

    &.danger {
      color: #f56c6c;
      background-color: #fef0f0;
    }

    &.warning {
      color: #e6a23c;
      background-color: #fdf6ec;
    }

    &.primary {
      color: #409eff;
      background-color: #ecf5ff;
    }

    &.success {
      color: #67c23a;
      background-color: #f0f9eb;
    }
  }
}
</style>
