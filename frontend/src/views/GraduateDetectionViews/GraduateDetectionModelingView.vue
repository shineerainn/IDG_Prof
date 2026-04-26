<template>
  <div class="modeling-container">
    <!-- 标题 -->
    <h1 class="page-title">研究生预警宽表</h1>

    <el-table
      :data="tableData"
      style="width: 100%"
      border
      height="640px"
      @selection-change="handleSelectionChange"
    >
      <!-- 多选框列 -->
      <el-table-column
        type="selection"
        width="55"
        align="center"
      ></el-table-column>

      <!-- 分类 -->
      <el-table-column prop="classification" label="分类" width="100">
        <template #header>
          <span class="table-header">分类</span>
        </template>
      </el-table-column>

      <!-- 属性名 -->
      <el-table-column prop="fieldName" label="属性名" width="180">
        <template #header>
          <span class="table-header">属性名</span>
        </template>
      </el-table-column>

      <!-- 中文名 -->
      <el-table-column prop="chineseName" label="中文名" width="180">
        <template #header>
          <span class="table-header">中文名</span>
        </template>
      </el-table-column>

      <!-- 含义 -->
      <el-table-column prop="meaning" label="含义" width="785">
        <template #header>
          <span class="table-header">含义</span>
        </template>
      </el-table-column>

      <!-- 示例 -->
      <el-table-column prop="example" label="示例" width="280">
        <template #header>
          <span class="table-header">示例</span>
        </template>
      </el-table-column>
    </el-table>

    <!-- 操作栏 -->
    <div class="action-bar">
      <div class="left-actions">
        <el-button type="primary" @click="startModeling" :disabled="isModeling">
          开始建模
        </el-button>
        <el-progress
          :percentage="progress"
          :stroke-width="16"
          :text-inside="true"
          class="progress-bar"
        />
        <span class="status-text">{{ statusText }}</span>
      </div>

      <div class="right-actions">
        <el-button type="info" @click="viewModel">查看</el-button>
        <el-button type="success" @click="saveModel">保存</el-button>
        <el-button type="warning" @click="exportModel">导出</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { readWideTableData } from "@/utils/excelReader";
import { sendGraduateDetection } from "@/api/graduateDetection";
import { mapState } from "vuex";

export default {
  name: "GraduateDetectionModelingView",
  computed: {
    ...mapState({
      userId: (state) => state.user.id,
    }),
  },
  data() {
    return {
      isModeling: false,
      progress: 0,
      statusText: "未开始",
      tableData: [],
      selectedItems: [],
      modelId: "",
      modelingInterval: null,
    };
  },
  async mounted() {
    try {
      this.loading = true;
      this.tableData = await readWideTableData("I2", "M104");
    } catch (error) {
      this.$message.error(error.message);
    } finally {
      this.loading = false;
    }
  },
  methods: {
    // 处理选择变化
    handleSelectionChange(val) {
      this.selectedItems = val;
    },

    // 开始建模
    async startModeling() {
      if (!this.userId) {
        this.$message.error("用户未登录");
        return;
      }

      if (this.selectedItems.length === 0) {
        this.$message.warning("请至少选择一个字段");
        return;
      }

      this.isModeling = true;
      this.statusText = "建模进行中...";
      this.progress = 0;

      // 构建请求参数
      const params = {
        userId: this.userId,
        pgWideTableAttributes: {},
      };

      // 将选中的字段设置为true，未选中的设置为false
      this.tableData.forEach((item) => {
        params.pgWideTableAttributes[item.fieldName] = this.selectedItems.some(
          (selected) => selected.fieldName === item.fieldName,
        );
      });

      // 启动进度条
      this.startProgressBar();

      try {
        // 异步发送建模请求
        const modelingPromise = sendGraduateDetection(params);

        // 等待进度条到达90%
        await new Promise((resolve) => {
          const checkProgress = setInterval(() => {
            if (this.progress >= 90) {
              clearInterval(checkProgress);
              resolve();
            }
          }, 100);
        });

        // 检查建模是否完成
        const response = await Promise.race([
          modelingPromise,
          new Promise((_, reject) =>
            setTimeout(() => reject(new Error("建模超时")), 2000),
          ),
        ]);

        // 建模成功
        this.modelId = response.data;
        this.progress = 100;
        this.statusText = "建模完成";
        this.$message.success("建模完成");
      } catch (error) {
        this.$message.error("建模失败：" + (error.message || "未知错误"));
        this.resetModelingStatus();
      }
    },

    // 启动进度条
    startProgressBar() {
      let speed = 1;
      this.modelingInterval = setInterval(() => {
        if (this.progress >= 90) {
          // 到90%后减慢速度
          speed = 0.1;
        }
        if (this.progress >= 90 && !this.modelId) {
          // 如果到90%还没有modelId，保持在90%
          clearInterval(this.modelingInterval);
          return;
        }
        if (this.progress >= 100) {
          clearInterval(this.modelingInterval);
          return;
        }
        this.progress = Math.min(this.progress + speed, 100);
      }, 50);
    },

    // 重置建模状态
    resetModelingStatus() {
      this.isModeling = false;
      this.progress = 0;
      this.statusText = "未开始";
      this.modelId = "";
      if (this.modelingInterval) {
        clearInterval(this.modelingInterval);
      }
    },

    // 查看模型
    viewModel() {
      if (!this.modelId) {
        this.$message.warning("请先完成建模");
        return;
      }
      this.$router.push({
        path: "/graduate-detection/analysis/student-overview",
        query: { modelId: this.modelId },
      });
    },

    // 保存模型
    saveModel() {
      if (!this.modelId) {
        this.$message.warning("请先完成建模");
        return;
      }
      this.$message.success("模型保存成功");
    },

    // 导出模型
    exportModel() {
      this.$confirm("确认导出当前模型吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          if (!this.modelId) {
            this.$message.warning("请先完成建模");
            return;
          }
          this.$message.success("导出成功");
        })
        .catch(() => {
          // 用户点击取消时的处理（空函数避免报错）
        });
    },
  },
  beforeDestroy() {
    // 组件销毁前清除定时器
    if (this.modelingInterval) {
      clearInterval(this.modelingInterval);
    }
  },
};
</script>

<style lang="scss" scoped>
::v-deep .el-table__body-wrapper {
  &::-webkit-scrollbar {
    width: 8px;
    height: 8px;
  }
  &::-webkit-scrollbar-thumb {
    background: #c1c1c1;
    border-radius: 4px;
    &:hover {
      background: #a8a8a8;
    }
  }
  &::-webkit-scrollbar-track {
    background: #f5f7fa;
  }
}
.modeling-container {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

  .page-title {
    text-align: center;
    color: #2c3e50;
    margin-bottom: 30px;
    font-size: 24px;
    flex-shrink: 0;
  }
  .el-table {
    flex: 1;
    min-height: 400px; /* 最小高度保证 */
  }

  .table-header {
    font-weight: 600;
    color: #303133;
  }
}

.action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  min-height: 60px;

  .left-actions {
    display: flex;
    align-items: center;
    gap: 20px;
    flex: 1;

    .progress-bar {
      flex: 1;
      max-width: 400px;
      min-width: 250px;

      ::v-deep .el-progress-bar__inner {
        position: relative;
      }

      ::v-deep .el-progress__text {
        position: absolute;
        left: 50%;
        top: 50%;
        transform: translate(-50%, -50%);
        color: #fff;
        font-weight: bold;
        z-index: 2;
      }
    }

    .status-text {
      min-width: 80px;
      text-align: right;
    }
  }

  .right-actions {
    flex-shrink: 0;
    .el-button-group {
      display: flex;
      gap: 10px;
    }
  }
}

// 覆盖Element默认样式
::v-deep .el-table {
  th {
    background-color: #f5f7fa !important;
  }

  .el-progress-bar {
    padding-right: 70px;
  }
}
</style>
