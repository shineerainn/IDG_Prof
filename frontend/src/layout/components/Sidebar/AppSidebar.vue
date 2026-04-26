<template>
  <div class="left-sidebar">
    <el-row class="tac">
      <el-col :span="24" class="sidebar-col">
        <el-menu
          router
          :default-active="activeMenu"
          class="el-menu-vertical-demo"
          @select="handleMenuItemClick"
          background-color="#8BAF56"
          active-text-color="#FFEB3B"
          text-color="#2e0e36"
        >
          <!-- 研究生画像一级菜单 -->
          <el-submenu
            index="/graduate-profile"
            v-if="hasPermission('graduate-profile')"
          >
            <template slot="title">
              <i class="el-icon-user"></i>
              <span>研究生画像</span>
            </template>
            <el-menu-item index="/graduate-profile/information">
              <i class="el-icon-document"></i>
              研究生信息
            </el-menu-item>
            <el-menu-item index="/graduate-profile/modeling">
              <i class="el-icon-data-analysis"></i>
              研究生画像生成
            </el-menu-item>
            <el-menu-item index="/graduate-profile/analysis">
              <i class="el-icon-reading"></i>
              研究生画像解读
            </el-menu-item>
          </el-submenu>
          <!-- 导师画像一级菜单 -->
          <el-submenu
            index="/supervisor-profile"
            v-if="hasPermission('supervisor-profile')"
          >
            <template slot="title">
              <i class="el-icon-s-custom"></i>
              <span>导师画像</span>
            </template>
            <el-menu-item index="/supervisor-profile/information">
              <i class="el-icon-document"></i>
              导师信息
            </el-menu-item>
            <el-menu-item index="/supervisor-profile/modeling">
              <i class="el-icon-data-analysis"></i>
              导师画像生成
            </el-menu-item>
            <el-menu-item index="/supervisor-profile/analysis">
              <i class="el-icon-reading"></i>
              导师画像解读
            </el-menu-item>
          </el-submenu>

          <!-- 辅导员画像一级菜单 -->
          <el-submenu
            index="/counser-profile"
            v-if="hasPermission('counser-profile')"
          >
            <template slot="title">
              <i class="el-icon-s-custom"></i>
              <span>辅导员画像</span>
            </template>
            <el-menu-item index="/counser-profile/information">
              <i class="el-icon-document"></i>
              辅导员信息
            </el-menu-item>
            <el-menu-item index="/counser-profile/modeling">
              <i class="el-icon-data-analysis"></i>
              辅导员画像生成
            </el-menu-item>
            <el-menu-item index="/counser-profile/analysis">
              <i class="el-icon-reading"></i>
              辅导员画像解读
            </el-menu-item>
          </el-submenu>

          <!-- 研究生预警一级菜单 -->
          <el-submenu
            index="/graduate-detection"
            v-if="hasPermission('graduate-detection')"
          >
            <template slot="title">
              <i class="el-icon-warning"></i>
              <span>研究生预警</span>
            </template>
            <el-menu-item index="/graduate-detection/modeling">
              <i class="el-icon-data-analysis"></i>
              研究生预警生成
            </el-menu-item>
            <el-menu-item index="/graduate-detection/analysis">
              <i class="el-icon-reading"></i>
              研究生预警解读
            </el-menu-item>
          </el-submenu>

          <!-- 智能体对话一级菜单 -->
          <el-submenu index="4" v-if="hasPermission('agent-chat')">
            <template slot="title">
              <i class="el-icon-chat-dot-round"></i>
              <span>智能体对话</span>
            </template>
            <el-menu-item index="/agent-chat/tools">
              <i class="el-icon-service"></i>
              {{ userRole === "pg" ? "智能工具" : "工具列表" }}
            </el-menu-item>
            <el-menu-item index="/agent-chat/chat">
              <i class="el-icon-first-aid-kit"></i>
              {{ userRole === "pg" ? "智能对话" : "AI助手" }}
            </el-menu-item>
          </el-submenu>

          <!-- 系统管理菜单 -->
          <el-submenu index="5" v-if="hasPermission('system-management')">
            <template slot="title">
              <i class="el-icon-setting"></i>
              <span>系统管理</span>
            </template>
            <el-menu-item index="5-1">
              <i class="el-icon-s-tools"></i>
              系统配置
            </el-menu-item>
            <el-menu-item index="5-2">
              <i class="el-icon-lock"></i>
              权限配置
            </el-menu-item>
            <el-menu-item index="5-3">
              <i class="el-icon-cpu"></i>
              算法配置
            </el-menu-item>
          </el-submenu>
        </el-menu>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapState } from "vuex";

export default {
  data() {
    return {
      isNavigating: false, // 导航状态锁
      // 权限菜单映射
      permissionMenus: {
        admin: [
          "graduate-profile",
          "supervisor-profile",
          "counser-profile",
          "graduate-detection",
          "agent-chat",
          "system-management",
        ],
        pg: ["agent-chat"],
        supv: [
          "graduate-profile",
          "supervisor-profile",
          "graduate-detection",
          "agent-chat",
        ],
        coun: [
          "graduate-profile",
          "supervisor-profile",
          "counser-profile",
          "graduate-detection",
          "agent-chat",
        ],
      },
    };
  },
  computed: {
    // 动态计算激活菜单项
    activeMenu() {
      return this.$route.path;
    },
    // 从Vuex获取用户角色
    ...mapState({
      userRole: (state) => state.user.role,
    }),
  },
  created() {
    // 在组件创建时输出调试信息
    console.log("当前用户角色:", this.userRole);
    console.log("权限菜单映射:", this.permissionMenus);
  },
  methods: {
    handleMenuItemClick(key, path) {
      if (this.isNavigating) return;

      this.isNavigating = true;
      this.$router
        .push(path)
        .finally(() => {
          this.isNavigating = false;
        })
        .catch((err) => {
          if (err.name !== "NavigationDuplicated") console.error(err);
        });

      console.log(key, path);
      console.log(this.$router);
    },

    // 检查当前用户是否有权限访问指定菜单
    hasPermission(menuKey) {
      // 调试信息
      console.log("检查权限:", menuKey, "当前角色:", this.userRole);

      // 如果没有角色信息，默认显示所有菜单（临时解决方案）
      if (!this.userRole) {
        console.log("未检测到用户角色，默认显示所有菜单");
        return true;
      }

      const allowedMenus = this.permissionMenus[this.userRole] || [];
      const hasPermission = allowedMenus.includes(menuKey);
      console.log("权限检查结果:", hasPermission, "允许的菜单:", allowedMenus);

      return hasPermission;
    },
  },
};
</script>

<style lang="scss" scoped>
.left-sidebar {
  width: 220px;
  background-color: #8baf56;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  height: auto;

  .el-menu-vertical-demo {
    margin-top: 10%;
    width: 100% !important;
    background-color: #8baf56;
    border-right: none;

    .el-submenu__title {
      font-size: 24px;
      border-bottom: 1px solid rgba(250, 248, 244, 0.2);
      display: flex;
      align-items: center;
      justify-content: flex-start;
      position: relative;
      height: 100px;

      i {
        margin-right: 15px;
        font-size: 20px;
        width: 24px;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        line-height: 1;
        color: #2e0e36;
        text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
      }

      span {
        margin-left: 0;
        line-height: 1;
        text-align: left;
        display: inline-block;
        width: 100%;
        font-size: 24px;
        font-weight: bold;
      }

      .el-submenu__icon-arrow {
        position: absolute;
        right: 20px;
        top: 50%;
        transform: translateY(-50%);
        margin-top: 0;
      }

      &:hover {
        background-color: rgba(250, 248, 244, 0.15) !important;
        i {
          color: #ffeb3b;
          text-shadow: 0 0 8px rgba(255, 235, 59, 0.5);
        }
      }

      &.is-active {
        i {
          color: #ffeb3b;
          text-shadow: 0 0 8px rgba(255, 235, 59, 0.5);
        }
      }
    }

    .el-menu--inline {
      background-color: rgba(139, 175, 86, 0.8) !important;
      padding: 8px 0;

      .el-menu-item {
        padding-left: 30px !important;
        height: 60px !important;
        line-height: 50px !important;
        display: flex;
        align-items: center;
        font-size: 20px;
        color: #2e0e36;

        i {
          margin-right: 15px;
          font-size: 18px;
          width: 24px;
          display: inline-flex;
          align-items: center;
          justify-content: center;
          color: #2e0e36;
          text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
        }

        span {
          text-align: left;
          display: inline-block;
          width: 100%;
        }

        &:hover {
          background-color: rgba(250, 248, 244, 0.15) !important;
          i {
            color: #ffeb3b;
            text-shadow: 0 0 8px rgba(255, 235, 59, 0.5);
          }
        }

        &.is-active {
          i {
            color: #ffeb3b;
            text-shadow: 0 0 8px rgba(255, 235, 59, 0.5);
          }
        }
      }
    }

    // 分组标题样式
    .el-menu-item-group__title {
      color: rgba(250, 248, 244, 0.7);
      padding: 12px 0 6px 20px;
    }

    // 图标样式
    [class^="el-icon-"] {
      margin-right: 15px;
      font-size: 18px;
      display: inline-flex;
      align-items: center;
      justify-content: center;
      width: 24px;
      height: 24px;
    }

    // 一级菜单图标
    .el-submenu__title [class^="el-icon-"] {
      font-size: 20px;
      width: 24px;
      height: 24px;
    }

    // 展开箭头样式
    .el-submenu__icon-arrow {
      font-size: 16px;
      margin-top: 0;
      position: absolute;
      right: 20px;
      top: 50%;
      transform: translateY(-50%);
    }
  }
}
</style>
