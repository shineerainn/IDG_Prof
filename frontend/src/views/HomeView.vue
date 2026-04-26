<template>
  <div class="welcome-section">
    <div class="welcome-header">
      <h1 class="welcome-title">欢迎使用智慧心理平台</h1>
      <p class="welcome-subtitle">智能分析 · 精准预警 · 科学干预</p>
    </div>

    <div class="feature-cards">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card class="feature-card" shadow="hover">
            <div class="card-icon">
              <i class="el-icon-data-analysis"></i>
            </div>
            <h3>智能分析</h3>
            <p>基于大数据和AI技术，提供精准的心理健康分析</p>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="feature-card" shadow="hover">
            <div class="card-icon">
              <i class="el-icon-warning"></i>
            </div>
            <h3>精准预警</h3>
            <p>及时发现潜在风险，提前检测心理危机</p>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="feature-card" shadow="hover">
            <div class="card-icon">
              <i class="el-icon-edit"></i>
            </div>
            <h3>科学干预</h3>
            <p>提供个性化的干预建议，助力学生健康成长</p>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  name: "HomeView",
  components: {},
  data() {
    return {
      isSidebarCollapsed: false,
    };
  },
  methods: {
    toggleSidebar() {
      this.isSidebarCollapsed = !this.isSidebarCollapsed;
    },
  },
};
</script>

<style lang="scss" scoped>
.welcome-section {
  max-width: 1200px;
  margin: 0 auto;
}

.welcome-header {
  text-align: center;
  margin-bottom: 60px;

  .welcome-title {
    font-size: 36px;
    color: #303133;
    margin-bottom: 16px;
    font-weight: 600;
  }

  .welcome-subtitle {
    font-size: 18px;
    color: #606266;
  }
}

.feature-cards {
  margin-bottom: 60px;

  .feature-card {
    height: 100%;
    text-align: center;
    transition: all 0.3s;

    &:hover {
      transform: translateY(-5px);
    }

    .card-icon {
      font-size: 48px;
      color: #409eff;
      margin: 20px 0;
    }

    h3 {
      font-size: 20px;
      color: #303133;
      margin-bottom: 16px;
    }

    p {
      color: #606266;
      line-height: 1.6;
    }
  }
}

.quick-actions {
  text-align: center;

  h2 {
    font-size: 24px;
    color: #303133;
    margin-bottom: 30px;
  }

  .action-button {
    width: 100%;
    height: 50px;
    font-size: 16px;
    margin-bottom: 20px;

    i {
      margin-right: 8px;
    }
  }
}
</style>
