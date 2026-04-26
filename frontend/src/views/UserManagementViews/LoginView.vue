<template>
  <div class="page-wrap">
    <div class="login-bg">
      <div class="header-navbar">
        <el-row class="navbar-container" type="flex" align="middle">
          <!-- 左侧 Logo 和系统名称 -->
          <el-col :span="6" class="left-section">
            <div class="brand">
              <img src="@/assets/buptcslogo.png" alt="北邮Logo" class="logo" />
            </div>
            <div class="motto">
              <span>开放</span>
              <span>交流</span>
              <span>包容</span>
              <span>奉献</span>
            </div>
          </el-col>
        </el-row>
      </div>
      <div class="login-container">
        <div class="login-box">
          <el-tabs v-model="activeTab">
            <el-tab-pane label="扫码登录" name="scan">
              <!-- 扫码登录内容 -->
              <img
                src="../../assets/qrcode.png"
                alt="二维码"
                class="qrcode-image"
              />
            </el-tab-pane>
            <el-tab-pane label="密码登录" name="password">
              <form>
                <div class="input-group">
                  <input
                    type="text"
                    id="account"
                    v-model="account"
                    placeholder="请输入学工号"
                    class="input-style"
                    @keydown.enter="login"
                  />
                </div>

                <div class="input-group password-group">
                  <input
                    :type="showPassword ? 'text' : 'password'"
                    id="password"
                    v-model="password"
                    placeholder="请输入密码"
                    class="input-style"
                    @keydown.enter="login"
                  />
                  <span class="eye-icon" @click="showPassword = !showPassword">
                    <i v-if="showPassword" class="el-icon-remove-outline"></i>
                    <i v-else class="el-icon-view"></i>
                  </span>
                </div>

                <button
                  type="submit"
                  @click.prevent="login"
                  class="button-style"
                  :disabled="isLogging"
                >
                  {{ isLogging ? "登录中..." : "登录" }}
                </button>
              </form>
              <div class="extra-links">
                <el-link type="primary" class="left-align">立即激活</el-link>
                <el-link type="primary">重置密码</el-link>
                <el-link type="primary" class="right-align">帮助中心</el-link>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
      <!-- 添加系统名称到登录板块左侧 -->
      <div class="system-title">
        <img
          src="@/assets/systemTitle.png"
          alt="智邮心语"
          class="artistic-title-img"
        />
      </div>
    </div>
  </div>
</template>

<script>
import { getUserPwd, getUserRole } from "@/api/user";

export default {
  data() {
    return {
      account: "",
      password: "",
      showPassword: false,
      activeTab: "password",
      isLogging: false,
    };
  },

  created() {
    console.log("登录页面初始化");
  },

  methods: {
    // 检查是否已经存在有效session
    async checkExistingSession() {
      try {
        const sessionResponse = await fetch("/user/session/status", {
          credentials: "include",
        });
        const sessionData = await sessionResponse.json();

        if (
          sessionData.code === 200 &&
          sessionData.data &&
          sessionData.data.includes("Session存在: true")
        ) {
          console.log("检测到有效session，直接跳转首页");
          this.$message.info("检测到已登录状态，自动跳转");
          this.$router.push("/home");
        }
      } catch (error) {
        console.log("无有效session，显示登录页面");
      }
    },

    // 主要登录方法
    async login() {
      // 防止重复提交
      if (this.isLogging) return;

      try {
        // 开始登录流程
        this.isLogging = true;

        const { account, password } = this;

        // 基础输入验证
        if (!account || !password) {
          this.$message.error("请输入账号和密码");
          return;
        }

        console.log("开始登录流程，用户:", account);

        // 方案1: 保持原有验证逻辑 + 后端登录
        console.log("步骤1: 验证用户密码...");
        const res = await getUserPwd({ userId: account });

        if (res.code === 200) {
          if (res.data.userPwd === password) {
            console.log("步骤2: 密码验证成功，调用后端登录接口...");

            // 关键修复：调用后端登录接口创建session
            const loginResponse = await fetch("/user/login", {
              method: "POST",
              headers: {
                "Content-Type": "application/x-www-form-urlencoded",
              },
              body: `userId=${encodeURIComponent(account)}&password=${encodeURIComponent(password)}`,
              credentials: "include", // 关键：携带cookie，创建session
            });

            const loginResult = await loginResponse.json();
            console.log("步骤3: 后端登录接口响应:", loginResult);

            if (loginResult.code === 200) {
              console.log("步骤4: 后端session创建成功");

              // 获取用户权限并存储到store
              await this.getUserRoleAndStore();

              // 验证session是否创建成功（调试用）
              await this.verifySessionCreated();

              this.$message.success("登录成功");
              console.log("步骤5: 登录流程完成，跳转首页");

              this.$router.push("/home");
            } else {
              console.error("后端登录失败:", loginResult);
              this.$message.error(
                "登录失败: " + (loginResult.msg || "服务器错误"),
              );
            }
          } else {
            this.$message.error("密码错误");
            console.warn("密码验证失败");
          }
        } else {
          this.$message.error(res.message || "账户不存在");
          console.warn("用户不存在:", res);
        }
      } catch (error) {
        console.error("登录过程异常:", error);

        if (error.name === "TypeError" && error.message.includes("fetch")) {
          this.$message.error("网络连接失败，请检查网络");
        } else {
          this.$message.error("登录失败，请稍后重试");
        }
      } finally {
        this.isLogging = false;
      }
    },

    // 验证session是否创建成功
    async verifySessionCreated() {
      try {
        const sessionResponse = await fetch("/user/session/status", {
          credentials: "include",
        });
        const sessionData = await sessionResponse.json();
        console.log("Session验证结果:", sessionData);

        if (
          sessionData.code === 200 &&
          sessionData.data &&
          sessionData.data.includes("Session存在: true")
        ) {
          console.log("✅ Session创建成功，AgentChat可以正常使用");

          // 额外验证：测试获取用户信息
          const userResponse = await fetch("/user/current-user", {
            credentials: "include",
          });
          const userData = await userResponse.json();
          console.log("✅ 用户信息验证:", userData);

          if (userData.code === 200 && userData.data) {
            console.log("✅ 用户信息正确返回，包含:", userData.data);
          }
        } else {
          console.warn("⚠️ Session可能未正确创建");
        }
      } catch (error) {
        console.warn("Session验证失败:", error);
      }
    },

    // 获取用户权限并存储到全局变量
    async getUserRoleAndStore() {
      try {
        console.log("获取用户权限...");
        const roleRes = await getUserRole({ userId: this.account });

        if (roleRes.code === 200) {
          this.$store.commit("setUserId", this.account);
          this.$store.commit("setUserRole", roleRes.data.userRole);

          console.log("用户权限获取成功:", roleRes.data.userRole);
          console.log("Store中的用户ID:", this.$store.state.userId);
          console.log("Store中的用户权限:", this.$store.state.userRole);
        } else {
          console.error("获取用户权限失败:", roleRes.message);
        }
      } catch (error) {
        console.error("获取用户权限出错:", error);
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.input-group {
  width: 250px;

  &.password-group {
    .input-style {
      padding-right: 35px;
    }
  }
}

.eye-icon {
  position: absolute;
  right: 15px;
  top: 47.8%;
  transform: translateY(-125%);
  cursor: pointer;
  color: #c0c4cc;
  transition: color 0.2s;
  z-index: 2;

  &:hover {
    color: #38a169;
  }

  i {
    font-size: 18px;
    vertical-align: middle;
  }
}

.input-style {
  width: 270px;
  height: 25px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  transition: border-color 0.2s;
  padding: 15px;
  box-sizing: border-box;

  &:focus {
    border-color: #38a169;
    outline: none;
  }
}

.login-bg {
  background-image: url("@/assets/Login-bg.jpg");
  background-size: cover;
  background-position: center;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
  padding: 0;
}

.header-navbar {
  height: 100px;
  color: #013094;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
  position: relative;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.navbar-container {
  height: 100%;
  padding: 0 24px;
  z-index: 1;
}

.left-section {
  position: relative;
  display: flex;
  align-items: center;
  flex-shrink: 0;
  min-width: 600px;
  justify-content: space-between;

  .brand {
    display: flex;
    align-items: center;
    white-space: nowrap;
    overflow: hidden;
    flex-shrink: 0;

    .logo {
      height: 64px;
      margin-right: 24px;
      flex-shrink: 0;
      filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
    }
  }

  .motto {
    display: flex;
    gap: 80px;
    margin-left: 280px;

    span {
      font-size: 20px;
      color: rgba(255, 255, 255, 0.95);
      width: 100px;
      font-weight: 500;
      letter-spacing: 0.25px;
      position: relative;
      text-shadow:
        0 1px 3px rgba(0, 0, 0, 0.5),
        0 2px 6px rgba(0, 0, 0, 0.3);
      transition: all 0.3s ease;

      &:not(:last-child)::after {
        content: "";
        position: absolute;
        right: -20px;
        top: 50%;
        transform: translateY(-50%);
        width: 2px;
        height: 20px;
        background: rgba(255, 255, 255, 0.3);
        box-shadow: 0 0 4px rgba(0, 0, 0, 0.2);
      }

      &:hover {
        color: rgba(255, 255, 255, 1);
        text-shadow:
          0 2px 4px rgba(0, 0, 0, 0.6),
          0 3px 8px rgba(0, 0, 0, 0.4);
        transform: translateY(-2px);
      }
    }
  }
}

.system-title {
  position: absolute;
  top: 250px;
  left: 50%;
  transform: translateX(calc(-50% - 240px));
  width: auto;
  z-index: 1;
  opacity: 1;

  .artistic-title-img {
    width: 1000px;
    height: auto;
    filter: drop-shadow(2px 2px 4px rgba(0, 0, 0, 0.2));
    transition: opacity 0.3s ease;

    &:hover {
      opacity: 0.9;
    }
  }
}

@keyframes glow {
  from {
    text-shadow:
      0 0 5px rgba(56, 161, 105, 0.5),
      0 0 10px rgba(56, 161, 105, 0.3);
  }
  to {
    text-shadow:
      0 0 10px rgba(56, 161, 105, 0.8),
      0 0 20px rgba(56, 161, 105, 0.5);
  }
}

.login-container {
  position: absolute;
  top: 300px;
  right: 130px;
  width: 300px;
  z-index: 2;
}

.login-box {
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  padding: 15px;
  display: flex;
  flex-direction: column;
  align-items: center;
  box-sizing: border-box;
  width: 350px;
  min-height: 200px;
}

.password-label {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 250px;
  margin-bottom: 5px;
  margin-left: 25px;

  label {
    margin: 0;
    color: #999;
    text-align: start;
  }

  .password-preview {
    cursor: pointer;
    color: #999;
    font-size: 16px;
    transition: color 0.3s ease;

    &:hover {
      color: #38a169;
    }
  }
}

input {
  width: 250px;
  padding: 10px;
  margin-bottom: 25px;
  border: 1px solid #ccc;
  border-radius: 5px;
  height: auto;
}

button {
  width: 270px;
  padding: 10px;
  background-color: #38a169;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  margin-bottom: 10px;

  &:disabled {
    background-color: #a0aec0;
    cursor: not-allowed;
  }
}

button:hover:not(:disabled) {
  background-color: #1e98d7;
}

.qrcode-image {
  width: 270px;
  height: 270px;
  margin-bottom: 0px;
}

.extra-links {
  display: flex;
  justify-content: space-between;
  width: 270px;
  margin-left: 0;
}

.left-align {
  margin-left: 0;
}

.right-align {
  margin-right: 0;
}

.center-align {
  margin: 0 auto;
}
</style>
