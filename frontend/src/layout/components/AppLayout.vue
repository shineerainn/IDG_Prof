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
        <router-view />
      </div>
    </div>
  </div>
</template>

<script>
import AppNavbar from "@/layout/components/Navbar/AppNavbar.vue";
import AppSidebar from "@/layout/components/Sidebar/AppSidebar.vue";
export default {
  name: "AppLayout",
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
  min-height: calc(100vh - 300px); /* 调整为80px */
  width: calc(100vh - 300px);
}
</style>
