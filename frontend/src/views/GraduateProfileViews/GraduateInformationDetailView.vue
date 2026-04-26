<template>
  <div class="graduate-info-container">
    <div class="header">
      <h1>研究生详细信息</h1>
      <el-button type="primary" @click="goBack">返回研究生列表</el-button>
    </div>

    <el-card class="info-card">
      <!-- 基本属性 -->
      <div class="info-section">
        <h2>基本属性</h2>
        <div class="info-content">
          <div class="info-left">
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">姓名：</span>
                  <span class="value">{{ graduateInfo.graduateName }}</span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">学号：</span>
                  <span class="value">{{ graduateInfo.studentId }}</span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">入学时间：</span>
                  <span class="value">{{ graduateInfo.registerDate }}</span>
                </div>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">身份证号：</span>
                  <span class="value">{{ graduateInfo.citizenId }}</span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">性别：</span>
                  <span class="value">{{ graduateInfo.gender }}</span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">年龄：</span>
                  <span class="value">{{ graduateInfo.currentAge }}</span>
                </div>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">手机号：</span>
                  <span class="value">{{ graduateInfo.cellPhoneNumber }}</span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">籍贯：</span>
                  <span class="value">{{ graduateInfo.nativePlace }}</span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">政治面貌：</span>
                  <span class="value">{{ graduateInfo.politicalStatus }}</span>
                </div>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">本科院校：</span>
                  <span class="value">{{
                    graduateInfo.undergraduateSchool
                  }}</span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">本科专业：</span>
                  <span class="value">{{
                    graduateInfo.undergraduateMajor
                  }}</span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">入学方式：</span>
                  <span class="value">{{ graduateInfo.enrollmentType }}</span>
                </div>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">考研成绩：</span>
                  <span class="value">{{
                    graduateInfo.postgraduateScore === -1
                      ? "非考研入学"
                      : graduateInfo.postgraduateScore
                  }}</span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">所在学院：</span>
                  <span class="value">{{ graduateInfo.school }}</span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">所在专业：</span>
                  <span class="value">{{ graduateInfo.major }}</span>
                </div>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">当前年级：</span>
                  <span class="value">{{ graduateInfo.currentGrade }}</span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">导师姓名：</span>
                  <span class="value">{{ graduateInfo.supervisorName }}</span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">导师工号：</span>
                  <span class="value">{{ graduateInfo.supervisorWorkId }}</span>
                </div>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">辅导员：</span>
                  <span class="value">{{ graduateInfo.counselorName }}</span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">辅导员工号：</span>
                  <span class="value">{{ graduateInfo.counselorWorkId }}</span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">学生干部：</span>
                  <span class="value">{{
                    graduateInfo.isStudentLeader ? "是" : "否"
                  }}</span>
                </div>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">个人奖励：</span>
                  <span class="value"
                    >{{ graduateInfo.individualAwardCount }}次</span
                  >
                </div>
              </el-col>
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">不当言论：</span>
                  <span class="value"
                    >{{ graduateInfo.improperRemarksCount }}次</span
                  >
                </div>
              </el-col>
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">最高受惩：</span>
                  <span class="value">{{
                    graduateInfo.highestPunishmentLevel || "无"
                  }}</span>
                </div>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="info-item">
                  <span class="label">学业奖学金：</span>
                  <span class="value">{{
                    graduateInfo.latestScholarshipLevel
                  }}</span>
                </div>
              </el-col>
            </el-row>
          </div>
          <div class="info-right">
            <img
              :src="
                require(
                  graduateInfo.gender === '男'
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

      <!-- 学业情况 -->
      <div class="info-section">
        <h2>学业情况</h2>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">学业健康指数：</span>
              <span class="value blue-text">{{
                graduateInfo.academicIndex.toFixed(2)
              }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">国奖获奖：</span>
              <span class="value">{{ graduateInfo.prizeCount }}次</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">竞赛获奖：</span>
              <span class="value">{{ graduateInfo.competitionCount }}次</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">学分进度：</span>
              <span
                class="value"
                :class="{ 'red-text': graduateInfo.credit < 1 }"
                >{{ graduateInfo.credit.toFixed(1) * 100 }}%</span
              >
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">不及格学分：</span>
              <span class="value">{{ graduateInfo.failedCredit }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">发表论文：</span>
              <span class="value">{{ graduateInfo.paperCount }}篇</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">EI论文：</span>
              <span class="value">{{ graduateInfo.eiPaperCount }}篇</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">SCI论文：</span>
              <span class="value">{{ graduateInfo.sciPaperCount }}篇</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">学业状态：</span>
              <span class="value">{{ graduateInfo.academicStatus }}</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 毕业设计 -->
      <div class="info-section">
        <h2>毕业设计</h2>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">毕设健康指数：</span>
              <span class="value blue-text">{{
                graduateInfo.graduationDesignIndex.toFixed(2)
              }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">开题情况：</span>
              <span
                class="value"
                :class="{
                  'red-text':
                    graduateInfo.thesisProposal !== '通过' &&
                    graduateInfo.thesisProposal !== '暂未涉及',
                }"
                >{{ graduateInfo.thesisProposal }}</span
              >
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">中期检查：</span>
              <span
                class="value"
                :class="{
                  'red-text':
                    graduateInfo.middleCheck !== '通过' &&
                    graduateInfo.middleCheck !== '暂未涉及',
                }"
                >{{ graduateInfo.middleCheck }}</span
              >
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">查重：</span>
              <span
                class="value"
                :class="{
                  'red-text':
                    graduateInfo.duplicateCheck !== '通过' &&
                    graduateInfo.duplicateCheck !== '暂未涉及',
                }"
                >{{ graduateInfo.duplicateCheck }}</span
              >
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">盲审：</span>
              <span
                class="value"
                :class="{
                  'red-text':
                    graduateInfo.blindReview !== '通过' &&
                    graduateInfo.blindReview !== '暂未涉及',
                }"
                >{{ graduateInfo.blindReview }}</span
              >
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">预答辩：</span>
              <span
                class="value"
                :class="{
                  'red-text':
                    graduateInfo.preDefense !== '通过' &&
                    graduateInfo.preDefense !== '暂未涉及',
                }"
                >{{ graduateInfo.preDefense }}</span
              >
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">答辩：</span>
              <span
                class="value"
                :class="{
                  'red-text':
                    graduateInfo.defense !== '通过' &&
                    graduateInfo.defense !== '暂未涉及',
                }"
                >{{ graduateInfo.defense }}</span
              >
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 就业情况 -->
      <div class="info-section">
        <h2>就业情况</h2>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">就业健康指数：</span>
              <span class="value blue-text">{{
                graduateInfo.employmentIndex.toFixed(2)
              }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">求职意向：</span>
              <span class="value">{{
                graduateInfo.jobIntention === 1 ? "是" : "否"
              }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">是否签约：</span>
              <span class="value">{{ graduateInfo.sign ? "是" : "否" }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">是否有意向：</span>
              <span class="value">{{ graduateInfo.offer ? "是" : "否" }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">是否录取：</span>
              <span class="value">{{
                graduateInfo.admission ? "是" : "否"
              }}</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 生活情况 -->
      <div class="info-section">
        <h2>生活情况</h2>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">生活健康指数：</span>
              <span class="value blue-text">{{
                graduateInfo.livingIndex.toFixed(2)
              }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">月异常行为：</span>
              <span
                class="value"
                :class="{
                  'blue-text': graduateInfo.monthlyAbnormalCount === 0,
                  'red-text': graduateInfo.monthlyAbnormalCount > 0,
                }"
                >{{ graduateInfo.monthlyAbnormalCount }}次</span
              >
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">问题种类：</span>
              <span
                class="value"
                :class="{
                  'blue-text':
                    !graduateInfo.latestIssueType ||
                    graduateInfo.latestIssueType === '无',
                  'red-text':
                    graduateInfo.latestIssueType &&
                    graduateInfo.latestIssueType !== '无',
                }"
                >{{ graduateInfo.latestIssueType || "无" }}</span
              >
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">周平均消费：</span>
              <span class="value">{{ graduateInfo.averageFee }}元</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">周平均消费标准差：</span>
              <span class="value">{{ graduateInfo.stdFee }}元</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">周平均消费趋势指数：</span>
              <span
                class="value"
                :class="{ 'red-text': graduateInfo.gradientFee < -20 }"
                >{{ graduateInfo.gradientFee }}</span
              >
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">食堂消费：</span>
              <span
                class="value"
                :class="{ 'yellow-text': graduateInfo.averageFoodCount <= 0 }"
              >
                {{ graduateInfo.averageFoodFee }}元/{{
                  graduateInfo.averageFoodCount
                }}次
              </span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">食堂消费标准差：</span>
              <span class="value">{{ graduateInfo.stdFoodFee }}元</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">食堂消费趋势指数：</span>
              <span
                class="value"
                :class="{ 'yellow-text': graduateInfo.gradientFoodFee >= 0 }"
              >
                {{ graduateInfo.gradientFoodFee }}
              </span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">浴室消费：</span>
              <span
                class="value"
                :class="{ 'yellow-text': graduateInfo.gradientBathFee <= 0 }"
              >
                {{ graduateInfo.gradientBathFee }}
              </span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">浴室消费标准差：</span>
              <span
                class="value"
                :class="{ 'yellow-text': graduateInfo.averageBathCount <= 0 }"
              >
                {{ graduateInfo.averageBathFee }}元/{{
                  graduateInfo.averageBathCount
                }}次
              </span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">浴室消费趋势指数：</span>
              <span
                class="value"
                :class="{ 'yellow-text': graduateInfo.gradientBathFee <= 0 }"
              >
                {{ graduateInfo.gradientBathFee }}
              </span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">超市消费：</span>
              <span
                class="value"
                :class="{ 'yellow-text': graduateInfo.gradientStoreFee >= 0 }"
              >
                {{ graduateInfo.gradientStoreFee }}
              </span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">超市消费标准差：</span>
              <span
                class="value"
                :class="{ 'yellow-text': graduateInfo.averageStoreCount >= 0 }"
              >
                {{ graduateInfo.averageStoreFee }}元/{{
                  graduateInfo.averageStoreCount
                }}次
              </span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">超市消费趋势指数：</span>
              <span
                class="value"
                :class="{ 'yellow-text': graduateInfo.gradientStoreFee >= 0 }"
              >
                {{ graduateInfo.gradientStoreFee }}
              </span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">运动场进入：</span>
              <span class="value"
                >{{ graduateInfo.averageStadiumCount }}次/周</span
              >
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">运动场进入标准差：</span>
              <span class="value">{{ graduateInfo.stdStadiumCount }}元</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">运动场进入趋势指数：</span>
              <span class="value">{{ graduateInfo.gradientStadiumCount }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">出校门次数：</span>
              <span class="value"
                >{{ graduateInfo.averageExitCount }}次/周</span
              >
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">出校门次数标准差：</span>
              <span class="value">{{ graduateInfo.stdExitCount }}元</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">出校门次数趋势指数：</span>
              <span class="value">{{ graduateInfo.gradientExitCount }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">宿舍驻留：</span>
              <span class="value"
                >{{ graduateInfo.averageStayHour }}小时/周</span
              >
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">宿舍驻留标准差：</span>
              <span class="value">{{ graduateInfo.stdStayHour }}元</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">宿舍驻留趋势指数：</span>
              <span
                class="value"
                :class="{ 'red-text': graduateInfo.gradientStayHour > 2 }"
                >{{ graduateInfo.gradientStayHour }}</span
              >
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">上网时长：</span>
              <span class="value"
                >{{ graduateInfo.averageInternetHour }}小时/周</span
              >
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">上网时长标准差：</span>
              <span class="value">{{ graduateInfo.stdInternetHour }}元</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">上网时长趋势指数：</span>
              <span
                class="value"
                :class="{ 'red-text': graduateInfo.gradientInternetHour > 2 }"
                >{{ graduateInfo.gradientInternetHour }}</span
              >
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">移动端上网：</span>
              <span class="value"
                >{{ graduateInfo.averageMobileHour }}小时/周</span
              >
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">移动端上网标准差：</span>
              <span class="value">{{ graduateInfo.stdMobileHour }}元</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">移动端上网趋势指数：</span>
              <span class="value">{{ graduateInfo.gradientMobileHour }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">非移动端上网：</span>
              <span class="value">{{ graduateInfo.averagePcHour }}小时/周</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">非移动端上网标准差：</span>
              <span class="value">{{ graduateInfo.stdPcHour }}元</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">非移动端上网趋势指数：</span>
              <span class="value">{{ graduateInfo.gradientPcHour }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">论坛发帖：</span>
              <span class="value"
                >{{ graduateInfo.averageForumPost }}次/周</span
              >
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">论坛发帖标准差：</span>
              <span class="value">{{ graduateInfo.stdForumPost }}元</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">论坛发帖趋势指数：</span>
              <span class="value">{{ graduateInfo.gradientForumPost }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">论坛回帖：</span>
              <span class="value"
                >{{ graduateInfo.averageForumReply }}次/周</span
              >
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">论坛回帖标准差：</span>
              <span class="value">{{ graduateInfo.stdForumReply }}元</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">论坛回帖趋势指数：</span>
              <span class="value">{{ graduateInfo.gradientForumReply }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">深夜发帖：</span>
              <span class="value"
                >{{ graduateInfo.averageLateNightPost }}次/周</span
              >
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">深夜发帖标准差：</span>
              <span class="value">{{ graduateInfo.stdLateNightPost }}元</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">深夜发帖趋势指数：</span>
              <span class="value">{{
                graduateInfo.gradientLateNightPost
              }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">深夜回帖：</span>
              <span class="value"
                >{{ graduateInfo.averageLateNightReply }}次/周</span
              >
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">深夜回帖标准差：</span>
              <span class="value">{{ graduateInfo.stdLateNightReply }}元</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">深夜回帖趋势指数：</span>
              <span class="value">{{
                graduateInfo.gradientLateNightReply
              }}</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 家庭情况 -->
      <div class="info-section">
        <h2>家庭情况</h2>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="label">独生子女：</span>
              <span class="value">{{
                graduateInfo.isOnlyChild ? "是" : "否"
              }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="label">经济困难：</span>
              <span class="value">{{
                graduateInfo.isPovertyStudent ? "是" : "否"
              }}</span>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "GraduateInformationDetailView",
  data() {
    return {
      graduateInfo: {},
    };
  },
  mounted() {
    // 从路由参数中获取学生数据
    console.info("路由参数:", this.$route.params);
    if (this.$route.params.graduateData) {
      try {
        const data = JSON.parse(this.$route.params.graduateData);
        console.log("解析后的学生数据:", data); // 添加日志
        this.graduateInfo = data;
      } catch (error) {
        console.error("解析学生数据失败:", error);
        this.$message.error("获取学生数据失败");
      }
    } else {
      console.warn("未接收到学生数据");
    }
  },
  methods: {
    goBack() {
      this.$router.push({ path: "/graduate-profile/information" });
    },
  },
};
</script>

<style scoped lang="scss">
.graduate-info-container {
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
