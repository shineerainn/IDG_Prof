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
        <div class="info-card">
          <h2>个人信息</h2>
          <div class="info-item">
            <span>姓名</span>
            <span>导师张三</span>
          </div>
          <div class="info-item">
            <span>工号</span>
            <span>201000000001</span>
          </div>
          <div class="info-item">
            <span>工作电话</span>
            <span>15700000001</span>
          </div>
          <div class="info-item">
            <span>工作邮箱</span>
            <span>zhangsan@bupt.edu.cn</span>
          </div>
        </div>
        <div class="password-card">
          <h2>密码修改</h2>
          <div class="password-item">
            <span>原密码</span>
            <input
              type="password"
              v-model="oldPassword"
              placeholder="请输入原密码"
            />
          </div>
          <div class="password-item">
            <span>新密码</span>
            <input
              type="password"
              v-model="newPassword"
              placeholder="请输入新密码"
            />
          </div>
          <button @click="changePassword">确认修改</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import AppSidebar from "@/layout/components/Sidebar/AppSidebar.vue";
import AppNavbar from "@/layout/components/Navbar/AppNavbar.vue";
import { ref } from "vue";

export default {
  name: "UserInfoView",
  components: { AppNavbar, AppSidebar },
  setup() {
    // 定义表单绑定的响应式数据
    const oldPassword = ref("");
    const newPassword = ref("");

    // 模拟密码修改方法，实际需要和后端交互
    const changePassword = () => {
      // 这里可以添加密码验证逻辑，比如判断原密码是否正确等
      // 假设直接提交给后端接口，这里用 console.log 模拟
      console.log("原密码:", oldPassword.value);
      console.log("新密码:", newPassword.value);
      // 实际开发中应使用 axios 等库发送 POST 请求到后端接口
      // 例如：axios.post('/api/changePassword', { oldPassword: oldPassword.value, newPassword: newPassword.value })
      // 并处理响应结果，如提示修改成功或失败
    };

    return {
      oldPassword,
      newPassword,
      changePassword,
    };
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
  margin-top: 0;
}

.content-wrapper {
  flex: 1;
  padding: 24px;
  background: #f5f7fa;
  // 调整高度计算方式
  min-height: calc(100vh - 100px); /* 保持原有逻辑 */
}

.info-card,
.password-card {
  background-color: #fff;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.2);
  padding: 20px;
  border-radius: 5px;
  width: 40%;
}

.info-item,
.password-item {
  display: flex;
  gap: 10px;
  margin-bottom: 10px;
}

button {
  padding: 10px 20px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
</style>
