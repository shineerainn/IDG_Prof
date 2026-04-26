<template>
  <!-- 主容器 -->
  <div class="home-container">
    <!-- 顶部导航 -->
    <AppNavbar />

    <!-- 内容区 -->
    <div class="main-content">
      <!-- 侧边栏 -->
      <AppSidebar class="sidebar" />

      <!-- 页面内容 -->
      <div class="content-wrapper">
        <h1>欢迎使用智慧心理平台</h1>
      </div>
    </div>
  </div>
</template>

<script>
import AppNavbar from "@/layout/components/Navbar/AppNavbar.vue";
import AppSidebar from "@/layout/components/Sidebar/AppSidebar.vue";
export default {
  name: "HomeView",
  components: {
    AppNavbar,
    AppSidebar, // 注册组件
  },
};
</script>

<style lang="scss" scoped>
.home-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.main-content {
  display: flex;
  flex: 1;
  margin-top: 0; /* 与导航栏高度一致 */
}

.content-wrapper {
  flex: 1;
  padding: 24px;
  background: #f5f7fa;
  // 调整高度计算方式
  min-height: calc(100vh - 100px); /* 保持原有逻辑 */
}
</style>
