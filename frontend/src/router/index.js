import Vue from "vue";
import VueRouter from "vue-router";

Vue.use(VueRouter);

// 解决编程式路由往同一地址跳转时会报错的情况
const originalPush = VueRouter.prototype.push;
const originalReplace = VueRouter.prototype.replace;

// push
VueRouter.prototype.push = function push(location, onResolve, onReject) {
  if (onResolve || onReject)
    return originalPush.call(this, location, onResolve, onReject);
  return originalPush.call(this, location).catch((err) => err);
};

//replace
VueRouter.prototype.replace = function push(location, onResolve, onReject) {
  if (onResolve || onReject)
    return originalReplace.call(this, location, onResolve, onReject);
  return originalReplace.call(this, location).catch((err) => err);
};

const routes = [
  {
    path: "/",
    name: "home",
    redirect: "/login",
  },
  //系统主页
  {
    path: "/home",
    name: "Home",
    component: () => import("../layout/components/AppLayout.vue"),
    children: [
      {
        path: "/",
        name: "Home",
        component: () => import("../views/HomeView.vue"),
      },
    ],
  },
  {
    path: "/agent-chat",
    component: () => import("../layout/components/AppLayout.vue"),
    redirect: "/agent-chat/tools",
    children: [
      {
        path: "tools",
        name: "AgentTools",
        component: () =>
          import(
            /* webpackChunkName: "agent-chat" */ "../views/AgentChatViews/AgentTools.vue"
          ),
      },
      {
        path: "chat/:toolId",
        name: "AgentChat",
        component: () =>
          import(
            /* webpackChunkName: "agent-chat" */ "../views/AgentChatViews/AgentChat.vue"
          ),
      },
    ],
  },
  {
    path: "/graduate-profile",
    name: "GraduateProfile", // 学生画像
    component: () => import("../layout/components/AppLayout.vue"),
    redirect: "/graduate-profile/information", // 默认重定向到建模页面
    children: [
      {
        path: "information",
        name: "GraduateInformation", // 研究生信息
        component: () =>
          import("../views/GraduateProfileViews/GraduateInformationView.vue"),
      },
      {
        path: "information/:studentId",
        name: "GraduateInformationDetail", // 研究生信息
        component: () =>
          import(
            "../views/GraduateProfileViews/GraduateInformationDetailView.vue"
          ),
      },
      {
        path: "modeling",
        name: "GraduateProfileModeling", // 研究生画像建模
        component: () =>
          import(
            "../views/GraduateProfileViews/GraduateProfileModelingView.vue"
          ),
      },
      {
        path: "analysis",
        name: "GraduateProfileAnalysis", // 研究生画像解读
        component: () =>
          import(
            "../views/GraduateProfileViews/GraduateProfileAnalysisView.vue"
          ),
      },
      {
        path: "interpretation/:role/:profileId",
        name: "GraduateProfileInterpretation", // 研究生画像解读详情
        component: () =>
          import(
            "../views/GraduateProfileViews/GraduateProfileInterpretationView.vue"
          ),
        children: [
          {
            path: "",
            name: "ClusterOverview", // 分类概览
            component: () =>
              import("@/components/InterpretationSubviews/ClusterOverview.vue"),
          },
          {
            path: ":clusterId",
            name: "ClusterDetail", // 分类详情
            component: () =>
              import("@/components/InterpretationSubviews/ClusterDetail.vue"),
          },
          {
            path: ":clusterId/:attributeId",
            name: "AttributeDetail", // 属性详情
            component: () =>
              import("@/components/InterpretationSubviews/AttributeDetail.vue"),
          },
        ],
      },
    ],
  },
  {
    path: "/supervisor-profile",
    name: "SupervisorProfile", // 导师画像
    component: () => import("../layout/components/AppLayout.vue"),
    redirect: "/supervisor-profile/information", // 默认重定向到建模页面
    children: [
      {
        path: "information",
        name: "SupervisorInformation", // 导师信息
        component: () =>
          import(
            "../views/SupervisorProfileViews/SupervisorInformationView.vue"
          ),
      },
      {
        path: "information/:teacherId",
        name: "SupervisorInformationDetail", // 修改路由名称
        component: () =>
          import(
            "../views/SupervisorProfileViews/SupervisorInformationDetailView.vue"
          ),
      },
      {
        path: "modeling",
        name: "SupervisorProfileModeling", // 导师画像建模
        component: () =>
          import(
            "../views/SupervisorProfileViews/SupervisorProfileModelingView.vue"
          ),
      },
      {
        path: "analysis",
        name: "SupervisorProfileAnalysis", // 导师画像解读
        component: () =>
          import(
            "../views/SupervisorProfileViews/SupervisorProfileAnalysisView.vue"
          ),
      },
      {
        path: "interpretation/:role/:profileId",
        name: "SupervisorProfileInterpretation", // 研究生画像解读详情
        component: () =>
          import(
            "../views/SupervisorProfileViews/SupervisorProfileInterpretationView.vue"
          ),
        children: [
          {
            path: "",
            name: "ClusterOverview", // 分类概览
            component: () =>
              import("@/components/InterpretationSubviews/ClusterOverview.vue"),
          },
          {
            path: ":clusterId",
            name: "ClusterDetail", // 分类详情
            component: () =>
              import("@/components/InterpretationSubviews/ClusterDetail.vue"),
          },
          {
            path: ":clusterId/:attributeId",
            name: "AttributeDetail", // 属性详情
            component: () =>
              import("@/components/InterpretationSubviews/AttributeDetail.vue"),
          },
        ],
      },
    ],
  },
  {
    path: "/graduate-detection",
    name: "GraduateDetection", // 研究生预警
    component: () => import("../layout/components/AppLayout.vue"),
    redirect: "/graduate-detection/modeling", // 默认重定向到建模页面
    children: [
      // 预警建模
      {
        path: "modeling",
        name: "GraduateDetectionModeling",
        component: () =>
          import(
            /* webpackChunkName: "graduate-detection" */ "../views/GraduateDetectionViews/GraduateDetectionModelingView.vue"
          ),
      },
      // 预警分析
      {
        path: "analysis",
        name: "GraduateDetectionAnalysis",
        component: () =>
          import(
            /* webpackChunkName: "graduate-detection" */ "../views/GraduateDetectionViews/GraduateDetectionAnalysisView.vue"
          ),
      },
      // 学生总览
      {
        path: "analysis/student-overview",
        name: "GraduateDetectionAnalysisStudentOverview",
        component: () =>
          import(
            /* webpackChunkName: "graduate-detection" */ "../views/GraduateDetectionViews/GraduateDetectionAnalysisStudentOverView"
          ),
      },
      // 学生列表
      {
        path: "analysis/student-list",
        name: "GraduateDetectionAnalysisStudentList",
        component: () =>
          import(
            /* webpackChunkName: "graduate-detection" */ "../views/GraduateDetectionViews/GraduateDetectionAnalysisStudentListView.vue"
          ),
      },
      // 学生详情（动态参数）
      {
        path: "analysis/student-detail",
        name: "GraduateDetectionAnalysisStudentDetail",
        props: true, // 启用 props 接收路由参数
        component: () =>
          import(
            /* webpackChunkName: "graduate-detection" */ "../views/GraduateDetectionViews/GraduateDetectionAnalysisStudentDetailView.vue"
          ),
      },
      // 建议详情
      {
        path: "analysis/student-detail/suggestion",
        name: "GraduateDetectionAnalysisStudentDetailSuggestion",
        props: true, // 启用 props 接收路由参数
        component: () =>
          import(
            /* webpackChunkName: "graduate-detection" */ "../views/GraduateDetectionViews/GraduateDetectionAnalysisStudentDetailSuggestionView.vue"
          ),
      },
    ],
  },
  // 用户信息管理
  {
    path: "/userInfo",
    name: "userInformation",
    component: () => import("../views/UserManagementViews/UserInfoView.vue"),
  },
  //用户登录管理
  {
    path: "/login",
    name: "login",
    component: () => import("../views/UserManagementViews/LoginView.vue"),
  },
];

const router = new VueRouter({
  routes,
});

export default router;
