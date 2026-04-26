<template>
  <div class="header-navbar">
    <el-row class="navbar-container" type="flex" align="middle">
      <!-- 左侧 Logo 和系统名称 -->
      <el-col :span="6" class="left-section">
        <div class="brand" @click="goToHome">
          <img src="@/assets/buptcslogo2.png" alt="北邮Logo" class="logo" />
          <span class="system-name">智邮心语</span>
        </div>
      </el-col>

      <!-- 右侧图标区 -->
      <el-col :span="6" class="right-section">
        <div class="icon-group">
          <!-- 通知图标 -->
          <el-tooltip effect="dark" content="通知" placement="bottom">
            <el-button type="text" class="icon-btn">
              <img src="@/assets/notice.png" alt="通知" class="custom-icon" />
            </el-button>
          </el-tooltip>

          <!-- 个人信息图标 -->
          <el-tooltip effect="dark" content="个人信息" placement="bottom">
            <el-button type="text" class="icon-btn" @click="handleProfile">
              <img src="@/assets/user.png" alt="个人信息" class="custom-icon" />
            </el-button>
          </el-tooltip>

          <!-- 独立退出按钮 -->
          <el-tooltip effect="dark" content="退出" placement="bottom">
            <el-button type="text" class="icon-btn" @click="handleLogout">
              <img src="@/assets/logout.png" alt="退出" class="custom-icon" />
            </el-button>
          </el-tooltip>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "AppNavbar",
  methods: {
    handleLogout() {
      // 清除Vuex中的用户状态
      // this.$store.commit('clearUserData');

      // 跳转到登录页
      this.$router.push("/login");
    },
    handleProfile() {
      this.$router.push("/userInfo");
    },
    goToHome() {
      this.$router.push("/home");
    },
  },
};
</script>

<style lang="scss" scoped>
.header-navbar {
  height: 100px;
  background: rgba(139, 175, 86, 0.1);
  color: #8baf56;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
  position: relative;

  &::after {
    content: "";
    position: absolute;
    left: 0;
    bottom: -4px;
    width: 100%;
    height: 15px;
    background: #8baf56;
    box-shadow: 0 2px 4px rgba(139, 175, 86, 0.2);
  }

  .navbar-container {
    height: 100%;
    padding: 0 24px;
    z-index: 1;
  }

  .left-section {
    display: flex;
    align-items: center;
    flex-shrink: 0;
    min-width: 600px;

    .brand {
      display: flex;
      align-items: center;
      white-space: nowrap;
      overflow: hidden;
      cursor: pointer;

      .logo {
        height: 72px;
        filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.2)) brightness(1.1)
          contrast(1.2) sepia(0.1) saturate(1.5);
        background: rgba(250, 248, 244, 0.1);
        padding: 4px;
        border-radius: 8px;

        &:hover {
          filter: drop-shadow(0 4px 8px rgba(0, 0, 0, 0.3)) brightness(1.2)
            contrast(1.3) sepia(0.2) saturate(1.8);
          transform: scale(1.05);
          background: rgba(250, 248, 244, 0.2);
        }
      }

      .system-name {
        font-size: 36px;
        font-weight: 600;
        text-overflow: ellipsis;
        overflow: hidden;
        transition: font-size 0.3s;
        color: #8baf56;
      }
    }
  }

  .right-section {
    display: flex !important;
    justify-content: flex-end;
    align-items: center;
    height: 100%;

    .icon-group {
      position: absolute;
      right: 0;
      top: 0;
      display: flex;
      align-items: center;
      margin-top: 0;
      transform: translateY(12%);
      min-height: 32px;
      background: #8baf56;
      border-radius: 16px 0 0 0;
      padding: 12px 32px;
      gap: 24px;

      .custom-icon {
        width: 32px;
        height: 32px;
        filter: brightness(0) invert(1)
          drop-shadow(0 2px 4px rgba(0, 0, 0, 0.2));
        transition: all 0.3s;
      }

      .icon-btn {
        padding: 12px;
        &:hover {
          background: rgba(250, 248, 244, 0.2);
          .custom-icon {
            transform: scale(1.1);
            filter: brightness(0) invert(1)
              drop-shadow(0 0 8px rgba(250, 248, 244, 0.5));
          }
        }
      }
    }
  }
}
</style>
