<template>
  <div class="supervisor-info-container">
    <div class="header">
      <h1>导师详细信息</h1>
      <el-button type="primary" @click="goBack">返回导师列表</el-button>
    </div>

    <el-card class="info-card">
      <!-- 基本属性 -->
      <div class="info-section full-width">
        <h2>基本属性</h2>
        <div class="info-content">
          <div class="info-left">
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">姓名：</span>
                  <span class="value">{{ supervisorInfo.supervisorName }}</span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">工作证号：</span>
                  <span class="value">{{ supervisorInfo.workId }}</span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">入职时间：</span>
                  <span class="value">{{ supervisorInfo.entryDate }}</span>
                </div>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">身份证号：</span>
                  <span class="value">{{ supervisorInfo.citizenId }}</span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">性别：</span>
                  <span class="value">{{ supervisorInfo.gender }}</span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">年龄：</span>
                  <span class="value">{{ supervisorInfo.currentAge }}</span>
                </div>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">手机号：</span>
                  <span class="value">{{
                    supervisorInfo.cellPhoneNumber
                  }}</span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">职称：</span>
                  <span class="value">{{ supervisorInfo.title }}</span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">所属中心：</span>
                  <span class="value">{{ supervisorInfo.centerName }}</span>
                </div>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">最高学位：</span>
                  <span class="value">{{ supervisorInfo.highestDegree }}</span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">硕士导师：</span>
                  <span class="value">{{
                    supervisorInfo.isMasterSupervisor ? "是" : "否"
                  }}</span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">博士导师：</span>
                  <span class="value">{{
                    supervisorInfo.isDoctorSupervisor ? "是" : "否"
                  }}</span>
                </div>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">特殊荣誉：</span>
                  <span class="value">{{
                    supervisorInfo.specialTitle || "无"
                  }}</span>
                </div>
              </el-col>
            </el-row>
          </div>
          <div class="info-right">
            <img
              :src="
                require(
                  supervisorInfo.gender === '男'
                    ? '@/assets/PhotoMan.png'
                    : '@/assets/PhotoWoman.png',
                )
              "
              alt="证件照"
              class="id-photo"
            />
          </div>
        </div>
      </div>

      <!-- 研究生指导 -->
      <div class="info-section full-width">
        <h2>研究生指导</h2>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">硕士在读：</span>
              <span class="value">{{ supervisorInfo.masterNumber }}人</span>
            </div>
          </el-col>

          <el-col :span="8">
            <div class="info-item">
              <span class="label">硕士延毕：</span>
              <span class="value"
                >{{ supervisorInfo.masterDelayNumber }}人</span
              >
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">硕士延毕率：</span>
              <span class="value"
                >{{ (supervisorInfo.masterDelayRate * 100).toFixed(1) }}%</span
              >
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">博士在读：</span>
              <span class="value">{{ supervisorInfo.doctorNumber }}人</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">博士延毕率：</span>
              <span class="value"
                >{{ (supervisorInfo.doctorDelayRate * 100).toFixed(1) }}%</span
              >
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">博士延毕：</span>
              <span class="value"
                >{{ supervisorInfo.doctorDelayNumber }}人</span
              >
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">优秀毕业生：</span>
              <span class="value"
                >{{ supervisorInfo.excellentGraduatesNumber }}人</span
              >
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">优秀论文：</span>
              <span class="value"
                >{{ supervisorInfo.excellentGraduationThesis }}篇</span
              >
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">国奖次数：</span>
              <span class="value">{{ supervisorInfo.prizeCount }}次</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">竞赛获奖：</span>
              <span class="value">{{ supervisorInfo.competitionCount }}次</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">就业率：</span>
              <span class="value"
                >{{ supervisorInfo.employmentRate.toFixed(1) }}%</span
              >
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 科研情况 -->
      <div class="info-section full-width">
        <h2>科研情况</h2>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">高水平论文：</span>
              <span class="value">{{ supervisorInfo.highLevelPaper }}篇</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">横向项目：</span>
              <span class="value"
                >{{ supervisorInfo.enterpriseProject }}个</span
              >
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">纵向项目：</span>
              <span class="value"
                >{{ supervisorInfo.governmentProject }}个</span
              >
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">人均经费：</span>
              <span class="value">{{ supervisorInfo.averageFund }}万元</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 教学情况 -->
      <div class="info-section full-width">
        <h2>教学情况</h2>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">本科课程：</span>
              <span class="value"
                >{{ supervisorInfo.undergraduateCourse }}门</span
              >
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">本科学分：</span>
              <span class="value"
                >{{ supervisorInfo.undergraduateCourseCredit }}分</span
              >
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">研究生课程：</span>
              <span class="value">{{ supervisorInfo.graduateCourse }}门</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">研究生学分：</span>
              <span class="value"
                >{{ supervisorInfo.graduateCourseCredit }}分</span
              >
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 行政管理 -->
      <div class="info-section full-width">
        <h2>行政管理</h2>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">校领导：</span>
              <span class="value">{{
                supervisorInfo.isUniversityLeader ? "是" : "否"
              }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">院领导：</span>
              <span class="value">{{
                supervisorInfo.isCollegeLeader ? "是" : "否"
              }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">系主任：</span>
              <span class="value">{{
                supervisorInfo.isDean ? "是" : "否"
              }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">中心主任：</span>
              <span class="value">{{
                supervisorInfo.isCenterDirector ? "是" : "否"
              }}</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 绩效情况 -->
      <div class="info-section full-width">
        <h2>绩效情况</h2>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">第一年：</span>
              <span class="value">{{ supervisorInfo.firstYearRankRate }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">第二年：</span>
              <span class="value">{{ supervisorInfo.secondYearRankRate }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">第三年：</span>
              <span class="value">{{ supervisorInfo.thirdYearRankRate }}</span>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "SupervisorInformationDetailView",
  data() {
    return {
      supervisorInfo: {},
    };
  },
  mounted() {
    // 从路由参数中获取导师数据
    console.info("路由参数:", this.$route.params);
    if (this.$route.params.supervisorData) {
      try {
        const data = JSON.parse(this.$route.params.supervisorData);
        console.log("解析后的导师数据:", data); // 添加日志
        this.supervisorInfo = data;
      } catch (error) {
        console.error("解析导师数据失败:", error);
        this.$message.error("获取导师数据失败");
      }
    } else {
      console.warn("未接收到导师数据");
    }
  },
  methods: {
    goBack() {
      this.$router.push({ path: "/supervisor-profile/information" });
    },
  },
};
</script>

<style scoped lang="scss">
.supervisor-info-container {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  max-height: 80vh;
  overflow-y: auto;

  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    h1 {
      margin: 0;
      font-size: 24px;
      color: #303133;
    }
  }

  .info-card {
    margin-bottom: 20px;
    max-height: 70vh;
    overflow-y: auto;

    .info-section {
      margin-bottom: 15px;
      padding: 12px;
      background: #f8f9fa;
      border-radius: 8px;
      height: 100%;
      display: flex;
      flex-direction: column;

      &.full-width {
        margin-bottom: 15px;
        min-height: auto;
      }

      h2 {
        font-size: 20px;
        color: #409eff;
        margin-bottom: 6px;
        padding-bottom: 4px;
        border-bottom: 1px solid #ebeef5;
      }

      .el-row {
        flex: 1;
        margin-bottom: 6px !important;

        &:last-child {
          margin-bottom: 0 !important;
        }
      }
    }

    .info-item {
      font-size: 20px;
      line-height: 1.5;
      display: flex;
      align-items: center;
      position: relative;
      padding-left: 200px;

      &:last-child {
        margin-bottom: 0;
      }

      .label {
        font-weight: 500;
        position: absolute;
        left: 0;
        width: 220px;
        text-align: right;
      }

      .value {
        flex: 1;
        text-align: left;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        margin-left: 18px;
      }
    }
  }
}

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
  margin-left: 20px;
  display: flex;
  justify-content: flex-end;
}

.id-photo {
  width: 120px;
  height: 160px;
  object-fit: cover;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

// 如果需要添加颜色样式类
.blue-text {
  color: #409eff;
  font-weight: bold;
}

.red-text {
  color: #ff4949;
  font-weight: bold;
}

.yellow-text {
  color: #e6a23c;
  font-weight: bold;
}
</style>
