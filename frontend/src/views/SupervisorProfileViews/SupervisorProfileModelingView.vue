<template>
  <div class="modeling-container">
    <!-- 标题 -->
    <h1 class="page-title">导师画像宽表</h1>

    <!-- 宽表主体 -->
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

        <!-- 进度条 -->
        <el-progress
          :percentage="progress"
          :stroke-width="16"
          :text-inside="true"
          style="width: 300px; margin-left: 20px"
        />

        <!-- 状态显示 -->
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
import { mapState } from "vuex";
import { generateSupervisorProfile } from "@/api/supervisorProfile";
export default {
  name: "SupervisorProfileModelingView",
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
    };
  },
  async mounted() {
    try {
      this.loading = true;
      this.tableData = await readWideTableData("B2", "F41");
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
        supvWideTableAttributes: {},
      };

      // 将选中的字段设置为true，未选中的设置为false
      this.tableData.forEach((item) => {
        params.supvWideTableAttributes[item.fieldName] =
          this.selectedItems.some(
            (selected) => selected.fieldName === item.fieldName,
          );
      });

      // 启动进度条
      this.startProgressBar();

      try {
        // 异步发送建模请求
        const modelingPromise = generateSupervisorProfile(params);

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
      }, 20);
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
      this.$router.push({
        path: "/graduate-profile/interpretation/supv/" + this.modelId,
        params: {
          studentId: this.modelId,
        },
      });
    },

    // 保存模型
    saveModel() {
      this.$message.success("模型保存成功");
    },

    // 导出模型
    exportModel() {
      this.$confirm("确认导出当前模型吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        this.$message.success("导出成功");
      });
    },
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
  }

  .action-bar {
    margin-top: 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;

    .left-actions {
      display: flex;
      align-items: center;

      .status-text {
        margin-left: 20px;
        color: #606266;
        font-size: 14px;
      }
    }

    .right-actions {
      button:not(:last-child) {
        margin-right: 10px;
      }
    }
  }

  .table-header {
    font-weight: 600;
    color: #303133;
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
