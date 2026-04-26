<template>
  <div class="academic-modeling">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>研究生学业画像生成</span>
        <el-button
          style="float: right; padding: 3px 0"
          type="text"
          @click="refreshData"
        >
          刷新数据
        </el-button>
      </div>

      <!-- 配置区域 -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card class="config-card">
            <div slot="header">
              <span>学业特征选择</span>
            </div>

            <el-checkbox-group
              v-model="selectedAttributes"
              @change="handleAttributeChange"
            >
              <el-checkbox
                v-for="attr in availableAttributes"
                :key="attr.value"
                :label="attr.value"
                :disabled="isModeling"
              >
                {{ attr.label }}
              </el-checkbox>
            </el-checkbox-group>
          </el-card>
        </el-col>

        <el-col :span="12">
          <el-card class="config-card">
            <div slot="header">
              <span>建模操作</span>
            </div>

            <div class="modeling-actions">
              <el-button
                type="primary"
                @click="startModeling"
                :loading="isModeling"
                :disabled="selectedAttributes.length === 0"
              >
                开始建模
              </el-button>

              <el-button
                type="info"
                @click="viewHistory"
                :disabled="isModeling"
              >
                查看历史
              </el-button>
            </div>

            <!-- 进度显示 -->
            <div v-if="isModeling" class="progress-section">
              <el-progress
                :percentage="progress"
                :stroke-width="16"
                :text-inside="true"
                style="width: 100%; margin-top: 20px"
              />
              <p class="status-text">{{ statusText }}</p>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 建模结果 -->
      <el-card v-if="modelingResult" class="result-card">
        <div slot="header">
          <span>建模结果</span>
        </div>

        <el-row :gutter="20">
          <el-col :span="8">
            <div class="result-item">
              <h4>记录数量</h4>
              <p class="result-value">{{ modelingResult.recordCount || 0 }}</p>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="result-item">
              <h4>建模时间</h4>
              <p class="result-value">
                {{ modelingResult.createTime || "未知" }}
              </p>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="result-item">
              <h4>模型ID</h4>
              <p class="result-value">
                {{ modelingResult.profileId || "未知" }}
              </p>
            </div>
          </el-col>
        </el-row>
      </el-card>
    </el-card>

    <!-- 历史记录对话框 -->
    <el-dialog
      title="建模历史"
      :visible.sync="historyDialogVisible"
      width="80%"
    >
      <el-table :data="historyRecords" style="width: 100%">
        <el-table-column
          prop="profileId"
          label="模型ID"
          width="200"
        ></el-table-column>
        <el-table-column
          prop="createTime"
          label="创建时间"
          width="180"
        ></el-table-column>
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button size="mini" @click="viewDetail(scope.row)"
              >查看详情</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="建模详情" :visible.sync="detailDialogVisible" width="90%">
      <div v-if="detailData">
        <h3>模型ID: {{ detailData.profileId }}</h3>
        <p>记录数量: {{ detailData.recordCount }}</p>

        <el-table :data="detailData.data" style="width: 100%" max-height="400">
          <el-table-column
            v-for="column in tableColumns"
            :key="column.prop"
            :prop="column.prop"
            :label="column.label"
            :width="column.width"
          ></el-table-column>
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapState } from "vuex";
import {
  generateAcademicProfile,
  fetchAcademicProfile,
  getAcademicProfileDetail,
} from "@/api/academicAnalysis";

export default {
  name: "AcademicModelingView",
  computed: {
    ...mapState({
      userId: (state) => state.user.id,
    }),
  },
  data() {
    return {
      isModeling: false,
      progress: 0,
      statusText: "未开始",
      selectedAttributes: [],
      availableAttributes: [
        // 学业表现相关
        { value: "weightedAvgGrade", label: "加权平均成绩" },
        { value: "coreCourseGrade", label: "核心课程成绩" },
        { value: "lastSemesterAvgGrade", label: "上学期平均成绩" },
        { value: "gpaGrowthRate", label: "GPA增长率" },
        { value: "gradeRanking", label: "成绩排名" },
        { value: "rankingLevel", label: "排名等级" },
        { value: "academicPerformanceScore", label: "学业表现得分" },

        // 科研项目相关
        { value: "researchProjectLevel", label: "科研项目级别" },
        { value: "projectRole", label: "项目角色" },
        { value: "projectAchievementTransformation", label: "项目成果转化" },
        { value: "researchProjectScore", label: "科研项目得分" },

        // 论文相关
        { value: "thesisDefenseGrade", label: "论文答辩成绩" },
        { value: "thesisInnovativeness", label: "论文创新性" },
        { value: "thesisPracticalValue", label: "论文实用价值" },
        { value: "thesisAchievementTransformation", label: "论文成果转化" },
        { value: "thesisScore", label: "论文得分" },

        // 学术论文相关
        { value: "publishedJournalConference", label: "发表期刊会议" },
        { value: "authorLevel", label: "作者级别" },
        { value: "isHighlyCited", label: "是否高被引" },
        { value: "isIfAbove", label: "是否影响因子以上" },
        { value: "isCoverPaper", label: "是否封面论文" },
        { value: "academicPaperScore", label: "学术论文得分" },

        // 竞赛相关
        { value: "competitionLevel", label: "竞赛级别" },
        { value: "awardLevel", label: "获奖级别" },
        { value: "competitionScope", label: "竞赛范围" },
        { value: "competitionTeamRole", label: "竞赛团队角色" },
        { value: "competitionScore", label: "竞赛得分" },

        // 专利软件相关
        { value: "patentSoftwareType", label: "专利软件类型" },
        { value: "legalStatusCoefficient", label: "法律状态系数" },
        { value: "technologyTransferAmount", label: "技术转让金额" },
        { value: "patentSoftwareScore", label: "专利软件得分" },

        // 其他创新成果相关
        { value: "hasStartupProject", label: "是否有创业项目" },
        { value: "startupFundingStage", label: "创业融资阶段" },
        { value: "isCompanyRegistered", label: "是否注册公司" },
        { value: "hasStartupCompetitionAward", label: "是否有创业竞赛获奖" },
        { value: "techApplicationEnterpriseCount", label: "技术应用企业数量" },
        { value: "techPromotionContractAmount", label: "技术推广合同金额" },
        { value: "otherInnovationAchievementScore", label: "其他创新成果得分" },
      ],
      modelingResult: null,
      historyDialogVisible: false,
      detailDialogVisible: false,
      historyRecords: [],
      detailData: null,
      tableColumns: [
        { prop: "studentId", label: "学号", width: 120 },
        { prop: "graduateName", label: "姓名", width: 100 },
        { prop: "academicPerformanceScore", label: "学业表现", width: 100 },
        { prop: "researchProjectScore", label: "科研项目", width: 100 },
        { prop: "thesisScore", label: "论文", width: 100 },
        { prop: "academicPaperScore", label: "学术论文", width: 100 },
        { prop: "competitionScore", label: "竞赛", width: 100 },
        { prop: "patentSoftwareScore", label: "专利软件", width: 100 },
        {
          prop: "otherInnovationAchievementScore",
          label: "其他创新",
          width: 100,
        },
        { prop: "clusterResult", label: "聚类结果", width: 100 },
      ],
    };
  },
  methods: {
    // 格式化分数显示
    formatScore(val) {
      if (val === undefined || val === null || isNaN(val)) {
        return "0.000";
      }
      return parseFloat(val).toFixed(3);
    },

    // 开始建模
    async startModeling() {
      if (!this.userId) {
        this.$message.error("用户未登录");
        return;
      }

      if (this.selectedAttributes.length === 0) {
        this.$message.warning("请至少选择一个特征");
        return;
      }

      this.isModeling = true;
      this.statusText = "建模进行中...";
      this.progress = 0;

      // 构建请求参数
      const params = {
        userId: this.userId,
        attrList: this.selectedAttributes,
      };

      // 启动进度条
      this.startProgressBar();

      try {
        console.log("开始发送建模请求，参数：", params);

        // 异步发送建模请求
        const response = await generateAcademicProfile(params);

        console.log("建模请求响应：", response);

        // 建模成功
        this.modelingResult = {
          profileId: response.data,
          createTime: new Date().toLocaleString(),
          recordCount: 0, // 稍后通过详情接口获取实际数量
        };

        // 获取建模详情以获取实际记录数量
        try {
          const detailResponse = await getAcademicProfileDetail({
            profileId: response.data,
          });
          if (detailResponse.code === 200 && detailResponse.data) {
            this.modelingResult.recordCount =
              detailResponse.data.recordCount || 0;
          }
          console.log("建模成功：", detailResponse);
        } catch (detailError) {
          console.warn("获取建模详情失败:", detailError);
        }
        this.progress = 100;
        this.statusText = "建模完成";
        this.$message.success("建模完成");

        // 跳转到聚类结果页面
        this.$router.push({
          path: `/academic-analysis/clustering-result/${response.data}`,
        });
      } catch (error) {
        console.error("建模失败：", error);
        this.$message.error("建模失败：" + (error.message || "未知错误"));
        this.resetModelingStatus();
      } finally {
        this.isModeling = false;
        if (this.modelingInterval) {
          clearInterval(this.modelingInterval);
        }
      }
    },

    // 启动进度条
    startProgressBar() {
      let speed = 1;
      this.modelingInterval = setInterval(() => {
        if (this.progress >= 90) {
          speed = 0.1;
        }
        if (this.progress >= 90 && !this.modelingResult) {
          clearInterval(this.modelingInterval);
          return;
        }
        if (this.progress >= 100) {
          clearInterval(this.modelingInterval);
          this.isModeling = false;
          return;
        }
        this.progress += speed;
      }, 100);
    },

    // 重置建模状态
    resetModelingStatus() {
      this.isModeling = false;
      this.progress = 0;
      this.statusText = "未开始";
      if (this.modelingInterval) {
        clearInterval(this.modelingInterval);
      }
    },

    // 查看历史
    async viewHistory() {
      try {
        const response = await fetchAcademicProfile({ userId: this.userId });
        if (response.code === 200) {
          this.historyRecords = response.data;
          this.historyDialogVisible = true;
        } else {
          this.$message.error("获取历史记录失败");
        }
      } catch (error) {
        this.$message.error("获取历史记录失败：" + error.message);
      }
    },

    // 查看详情
    async viewDetail(record) {
      try {
        const response = await getAcademicProfileDetail({
          profileId: record.profileId,
        });
        if (response.code === 200) {
          this.detailData = response.data;
          this.detailDialogVisible = true;
        } else {
          this.$message.error("获取详情失败");
        }
      } catch (error) {
        this.$message.error("获取详情失败：" + error.message);
      }
    },

    // 刷新数据
    refreshData() {
      this.modelingResult = null;
      this.selectedAttributes = [];
      this.resetModelingStatus();
      this.$message.success("数据已刷新");
    },

    // 处理属性选择变化
    handleAttributeChange(value) {
      console.log("Selected attributes:", value);
    },
  },

  beforeDestroy() {
    if (this.modelingInterval) {
      clearInterval(this.modelingInterval);
    }
  },
};
</script>

<style scoped>
.academic-modeling {
  padding: 20px;
}

.config-card {
  margin-bottom: 20px;
}

.modeling-actions {
  margin-bottom: 20px;
}

.progress-section {
  margin-top: 20px;
}

.status-text {
  text-align: center;
  margin-top: 10px;
  color: #666;
}

.result-card {
  margin-top: 20px;
}

.result-item {
  text-align: center;
  padding: 20px;
}

.result-item h4 {
  margin: 0 0 10px 0;
  color: #333;
}

.result-value {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
  margin: 0;
}

.el-checkbox-group {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.el-checkbox {
  margin-right: 0;
}
</style>
