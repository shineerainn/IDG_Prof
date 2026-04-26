<template>
  <div class="detail-container">
    <!-- 头部导航 -->
    <div class="header">
      <h1 class="title">{{ pageTitle }}</h1>
      <div class="model-info">当前模型：{{ modelId }}</div>
      <el-button
        type="primary"
        icon="el-icon-arrow-left"
        @click="goBack"
        class="back-btn"
      >
        返回详情
      </el-button>
    </div>

    <!-- 学生信息板块 -->
    <el-card class="info-card">
      <div class="student-info">
        <div class="info-content">
          <div class="info-left">
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="info-row">
                  <span class="label">姓名：</span>{{ studentInfo.name }}
                </div>
                <div class="info-row">
                  <span class="label">学号：</span>{{ studentInfo.studentId }}
                </div>
                <div class="info-row">
                  <span class="label">学院：</span>{{ studentInfo.college }}
                </div>
                <div class="info-row">
                  <span class="label">专业：</span>{{ studentInfo.major }}
                </div>
              </el-col>
              <el-col :span="8">
                <div class="info-row">
                  <span class="label">年龄：</span>{{ studentInfo.age }}
                </div>
                <div class="info-row">
                  <span class="label">年级：</span>{{ studentInfo.grade }}
                </div>
                <div class="info-row">
                  <span class="label">联系方式：</span>{{ studentInfo.phone }}
                </div>
              </el-col>
              <el-col :span="4">
                <div class="info-row">
                  <span class="label">预警类型：</span>
                  <el-tag :type="color">{{ warningTypes[warningType] }}</el-tag>
                </div>
                <div class="info-row">
                  <span class="label">预警分数：</span>
                  <el-tag :type="color">{{ warningScore }}</el-tag>
                </div>
              </el-col>
            </el-row>
          </div>
          <div class="info-right">
            <img
              :src="
                require(
                  studentInfo.gender === '男'
                    ? '@/assets/PhotoBoy.png'
                    : '@/assets/PhotoGirl.png',
                )
              "
              alt="证件照"
              class="id-photo"
            />
          </div>
        </div>
      </div>
    </el-card>

    <!-- 并排布局的卡片容器 -->
    <div class="cards-row">
      <!-- AI辅助指导建议板块 -->
      <el-card class="suggestion-card">
        <div slot="header" class="card-header">
          <span>{{ pageTitle }}</span>
          <el-button type="primary" size="small" @click="showAddRecordDialog"
            >新增记录</el-button
          >
        </div>
        <div class="suggestion-content">
          <ul class="suggestion-list">
            <li
              v-for="(item, index) in aiSuggestions"
              :key="index"
              class="suggestion-item"
            >
              <i class="el-icon-check suggestion-icon"></i>
              <span>{{ item }}</span>
            </li>
          </ul>
        </div>
      </el-card>

      <!-- 教师工作记录板块 -->
      <el-card class="work-record-card">
        <div slot="header" class="card-header">
          <span>{{ recordType }}</span>
          <el-button type="primary" size="small" @click="showAddRecordDialog"
            >新增记录</el-button
          >
        </div>
        <div class="record-content">
          <div v-if="workRecords.length > 0" class="record-list">
            <div
              v-for="(record, index) in workRecords"
              :key="index"
              class="record-item"
            >
              <div class="record-meta">
                <span class="record-date">{{ record.date }}</span>
                <span class="record-operator">{{ record.operator }}</span>
                <el-tag
                  :type="getEvaluationType(record.evaluation)"
                  size="small"
                  >{{ record.evaluation }}</el-tag
                >
              </div>
              <div class="record-text">{{ record.content }}</div>
            </div>
          </div>
          <el-empty v-else description="暂无工作记录" />
        </div>
      </el-card>

      <!-- 后续工作计划板块 -->
      <el-card class="plan-card">
        <div slot="header" class="card-header">
          <span>{{ planType }}</span>
          <el-button type="primary" size="small" @click="showEditPlanDialog"
            >编辑计划</el-button
          >
        </div>
        <div class="plan-content">
          <ul class="suggestion-list">
            <li
              v-for="(item, index) in planItems"
              :key="index"
              class="suggestion-item"
            >
              <span>{{ item }}</span>
            </li>
          </ul>
        </div>
      </el-card>
    </div>

    <!-- 新增工作记录对话框 -->
    <el-dialog
      title="新增工作记录"
      :visible.sync="recordDialogVisible"
      width="600px"
    >
      <el-form :model="newRecord" label-width="80px">
        <el-form-item label="工作内容" required>
          <el-input
            type="textarea"
            v-model="newRecord.content"
            :rows="4"
            placeholder="请输入工作内容..."
          ></el-input>
        </el-form-item>
        <el-form-item label="自评价" required>
          <el-radio-group v-model="newRecord.evaluation">
            <el-radio label="良好">良好</el-radio>
            <el-radio label="一般">一般</el-radio>
            <el-radio label="较差">较差</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="recordDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveRecord">保 存</el-button>
      </div>
    </el-dialog>

    <!-- 编辑后续计划对话框 -->
    <el-dialog
      title="编辑后续工作计划"
      :visible.sync="planDialogVisible"
      width="600px"
    >
      <el-form :model="planForm" label-width="80px">
        <el-form-item label="计划内容" required>
          <el-input
            type="textarea"
            v-model="planForm.content"
            :rows="6"
            placeholder="请输入后续工作计划..."
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="planDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="savePlan">保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "GraduateDetectionAnalysisStudentDetailSuggestionView",
  data() {
    return {
      studentInfo: {},
      modelId: "",
      studentId: "",
      warningType: this.$route.query.warningType || "未知类型",
      warningScore: this.$route.query.warningScore || "0",
      color: this.$route.query.color || "info",
      userRole: this.$store.state.user.role || "supv", // 获取用户角色，默认为supv
      warningTypes: {
        1: "学业异常",
        2: "毕设异常",
        3: "就业异常",
        4: "生活异常",
      },
      // AI辅助指导建议 - 导师版本
      supvAiSuggestions: [
        "1.	主动和学生取得联系，了解学生遇到的问题和困难",
        "2.	帮助其制定明确详细的能力提高方案，并进行针对性指导",
        "3.	协调团队中能力相对强的同学开展相应的帮扶，适当调整科研工作分工",
        "4.	和辅导员取得联系，同步学生情况和后续工作安排",
        "5.	沟通中，注意方式方法，以鼓励为主，提供一定的情绪价值支持",
        "6.	视情况，向学院、心理中心转介",
      ],
      // AI辅助指导建议 - 辅导员版本
      counAiSuggestions: [
        "1.	积极同学生进行联系，开展深度辅导，加强心理引导",
        "2.	视情况，与学生导师进行有效沟通，发挥桥梁纽带作用",
        "3.	协调其宿舍同学进行帮扶引导，发挥朋辈力量",
        "4.	建立相关台账，定期跟进学生动态",
        "5.	视情况，向心理中心转介",
      ],
      // 教师工作记录
      workRecords: [],
      recordDialogVisible: false,
      newRecord: {
        content: "",
        evaluation: "良好",
      },
      // 后续工作计划
      followUpPlan: "",
      planItems: [],
      planDialogVisible: false,
      planForm: {
        content: "",
      },
    };
  },
  computed: {
    // 根据用户角色返回对应的AI建议
    aiSuggestions() {
      return this.userRole === "supv" || this.userRole === "admin"
        ? this.supvAiSuggestions
        : this.counAiSuggestions;
    },
    // 根据用户角色返回对应的标题
    pageTitle() {
      return this.userRole === "supv" || this.userRole === "admin"
        ? "导师疏导建议"
        : "辅导员疏导建议";
    },
    // 根据用户角色返回对应的记录类型
    recordType() {
      return this.userRole === "supv" || this.userRole === "admin"
        ? "导师工作记录"
        : "辅导员工作记录";
    },
    // 根据用户角色返回对应的计划类型
    planType() {
      return this.userRole === "supv" || this.userRole === "admin"
        ? "后续工作计划"
        : "后续帮扶计划";
    },
  },
  mounted() {
    this.modelId = this.$route.query.modelId || "";
    this.studentId = this.$route.query.studentId || "";
    this.color = this.$route.query.color || "info";

    if (!this.modelId || !this.studentId) {
      this.$message.error("缺少必要参数");
      this.$router.push("/graduate-detection/modeling");
    }

    // 从路由参数获取学生信息
    this.studentInfo = {
      name: this.$route.query.name || "未知",
      studentId: this.studentId || "未知",
      college: this.$route.query.college || "未知",
      major: this.$route.query.major || "未知",
      age: this.$route.query.age || "未知",
      grade: this.$route.query.grade || "未知",
      phone: this.$route.query.phone || "未知",
      gender: this.$route.query.gender || "未知",
    };

    // 根据用户角色加载不同的工作记录
    this.loadWorkRecords();

    // 获取后续工作计划
    this.followUpPlan =
      "1. 每周进行一次学习进度跟进，了解学生的学业情况\n2. 组织一次学习方法分享会，邀请优秀学生分享经验\n3. 与导师保持沟通，了解学生在科研方面的进展\n4. 根据学生情况，适时调整帮扶策略";

    // 将计划内容转换为数组
    this.planItems = this.followUpPlan
      .split("\n")
      .filter((item) => item.trim());
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    showAddRecordDialog() {
      this.newRecord = {
        content: "",
        evaluation: "良好",
      };
      this.recordDialogVisible = true;
    },
    saveRecord() {
      if (!this.newRecord.content.trim()) {
        this.$message.warning("请输入工作内容");
        return;
      }

      const today = new Date();
      const date = `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, "0")}-${String(today.getDate()).padStart(2, "0")}`;

      this.workRecords.unshift({
        date: date,
        operator: this.userRole === "supv" ? "当前导师" : "当前辅导员",
        content: this.newRecord.content,
        evaluation: this.newRecord.evaluation,
      });

      this.recordDialogVisible = false;
      this.$message.success("工作记录保存成功");
    },
    getEvaluationType(evaluation) {
      switch (evaluation) {
        case "良好":
          return "success";
        case "一般":
          return "warning";
        case "较差":
          return "danger";
        default:
          return "info";
      }
    },
    showEditPlanDialog() {
      this.planForm.content = this.followUpPlan || "";
      this.planDialogVisible = true;
    },
    savePlan() {
      if (!this.planForm.content.trim()) {
        this.$message.warning("请输入后续工作计划");
        return;
      }

      this.followUpPlan = this.planForm.content;
      this.planItems = this.followUpPlan
        .split("\n")
        .filter((item) => item.trim());
      this.planDialogVisible = false;
      this.$message.success("后续工作计划保存成功");
    },
    // 加载工作记录
    loadWorkRecords() {
      if (this.userRole === "supv" || this.userRole === "admin") {
        this.workRecords = [
          {
            date: "2025-03-15",
            operator: "导师",
            content:
              "已于学生进行面对面交流，了解到了学生遇到的困难，已经给学生提供了具体科研的思路，并进行了人文关怀，后续将持续跟进学生状态。",
            evaluation: "良好",
          },
          {
            date: "2025-03-18",
            operator: "导师",
            content: "组织学习小组帮扶方案，安排成绩优秀的学生进行一对一辅导",
            evaluation: "良好",
          },
          {
            date: "2025-03-20",
            operator: "导师",
            content: "与学生导师沟通，了解学生在科研方面的表现和压力",
            evaluation: "一般",
          },
        ];
      } else {
        // 辅导员版本的工作记录
        this.workRecords = [
          {
            date: "2025-03-15",
            operator: "辅导员",
            content:
              "与学生进行面对面交流，了解其学习困难和心理状态，已安排学习小组进行帮扶。",
            evaluation: "良好",
          },
          {
            date: "2025-03-18",
            operator: "辅导员",
            content: "与导师沟通学生情况，协调科研任务安排",
            evaluation: "良好",
          },
          {
            date: "2025-03-20",
            operator: "辅导员",
            content: "组织心理健康讲座，关注学生心理状态",
            evaluation: "一般",
          },
        ];
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.detail-container {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding: 0 20px;

  .title {
    flex: 1;
    margin: 0;
    color: #303133;
    font-size: 24px;
    text-align: left;
  }

  .model-info {
    margin: 0 30px;
    color: #606266;
    font-size: 16px;
    min-width: 200px;
    text-align: center;
  }

  .back-btn {
    width: 140px;
    ::v-deep .el-icon-arrow-left {
      margin-right: 5px;
      transform: rotate(180deg);
    }
  }
}

.info-card {
  margin-bottom: 20px;

  .info-content {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
  }

  .info-left {
    flex: 1;
  }

  .info-right {
    width: 120px;
    margin-left: 0px;
    display: flex;
    justify-content: flex-end;
  }

  .info-row {
    display: flex;
    align-items: center;
    margin: 10px 0;
    line-height: 1.6;

    .label {
      width: 80px;
      color: #909399;
      flex-shrink: 0;
    }
  }
}

.id-photo {
  width: 120px;
  height: 160px;
  object-fit: cover;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.cards-row {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.suggestion-card,
.work-record-card,
.plan-card {
  flex: 1;
  min-width: 0;
  height: 440px;
  display: flex;
  flex-direction: column;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
  height: 20px;
  padding: 10px 20px;
  border-bottom: none;
}

.suggestion-content,
.record-content,
.plan-content {
  flex: 1;
  overflow-y: auto;
  padding: 0 20px;
  text-align: left;
}

.suggestion-list {
  padding-left: 0;
  margin: 0;

  .suggestion-item {
    margin-bottom: 15px;
    line-height: 1.6;
    display: flex;
    align-items: flex-start;
    padding-left: 0;

    .suggestion-icon {
      color: #67c23a;
      margin-right: 10px;
      margin-top: 3px;
    }

    span {
      text-align: left;
    }
  }
}

.record-content {
  .record-list {
    .record-item {
      padding: 15px 0;
      border-bottom: 1px solid #ebeef5;

      &:last-child {
        border-bottom: none;
      }

      .record-meta {
        color: #909399;
        font-size: 12px;
        margin-bottom: 8px;
        display: flex;
        align-items: center;
        gap: 15px;
        padding-left: 0;
      }

      .record-text {
        color: #606266;
        line-height: 1.6;
        white-space: pre-wrap;
        text-align: left;
        padding-left: 0;
      }
    }
  }
}

.plan-content {
  .suggestion-list {
    padding-left: 20px;
    margin: 0;

    .suggestion-item {
      margin-bottom: 15px;
      line-height: 1.6;
      display: flex;
      align-items: flex-start;
    }
  }
}
</style>
