<script>
export default {
  name: "GraduateProfileInterpretationView",
  data() {
    return {
      profileId: "",
      selectedItems: [],
    };
  },
  created() {
    if (this.$route.params.profileId) {
      this.profileId = this.$route.params.profileId;
    }
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
  },
};
</script>

<template>
  <div class="modeling-container">
    <!-- 标题 -->
    <div class="header-section">
      <h1 class="page-title">研究生画像解读</h1>
      <el-button type="primary" @click="goBack" class="back-button"
        >返回</el-button
      >
    </div>
    <router-view />
  </div>
</template>

<style scoped lang="scss">
.modeling-container {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

  .header-section {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 30px;
    position: relative;
  }

  .page-title {
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
    color: #2c3e50;
    font-size: 24px;
    margin: 0;
  }

  .back-button {
    margin-left: auto;
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
</style>
