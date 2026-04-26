<template>
  <div class="agent-tools">
    <!-- 临时测试按钮 -->
    <button
      @click="testAPI"
      style="margin: 20px; padding: 10px; background: red; color: white"
    >
      测试API连接
    </button>
    <div class="page-header">
      <h1>智能体助手</h1>
      <p>选择一个智能助手开始对话</p>
    </div>

    <!-- 分类筛选 -->
    <div class="category-filter">
      <button
        class="filter-btn"
        :class="{ active: selectedCategory === '' }"
        @click="selectedCategory = ''"
      >
        全部
      </button>
      <button
        class="filter-btn"
        :class="{ active: selectedCategory === 'education' }"
        @click="selectedCategory = 'education'"
      >
        教育
      </button>
      <button
        class="filter-btn"
        :class="{ active: selectedCategory === 'analytics' }"
        @click="selectedCategory = 'analytics'"
      >
        数据分析
      </button>
      <button
        class="filter-btn"
        :class="{ active: selectedCategory === 'general' }"
        @click="selectedCategory = 'general'"
      >
        通用
      </button>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <div class="loading">加载中...</div>
    </div>

    <!-- 错误状态 -->
    <div v-else-if="error" class="error-container">
      <div class="error">{{ error }}</div>
    </div>

    <!-- 工具列表 -->
    <div v-else class="tool-grid">
      <div
        v-for="tool in filteredTools"
        :key="tool.id"
        class="tool-card"
        @click="goToChat(tool.id)"
      >
        <div class="tool-icon">
          <i :class="getToolIcon(tool.category)"></i>
        </div>
        <div class="tool-name">{{ tool.name }}</div>
        <div class="tool-description">{{ tool.description }}</div>
        <div class="tool-category">{{ getCategoryName(tool.category) }}</div>
      </div>
    </div>
  </div>
</template>

<script>
import { agentApi } from "@/api/agent";

export default {
  name: "AgentTools",
  data() {
    return {
      tools: [],
      loading: true,
      error: null,
      selectedCategory: "",
    };
  },
  computed: {
    filteredTools() {
      if (!this.selectedCategory) {
        return this.tools;
      }
      return this.tools.filter(
        (tool) => tool.category === this.selectedCategory,
      );
    },
  },
  watch: {
    selectedCategory() {
      this.loadTools();
    },
  },
  mounted() {
    this.loadTools();
  },
  methods: {
    async testAPI() {
      try {
        console.log("测试Middleware...");
        const response = await this.$http.get(
          "http://localhost:8080/agent/tools",
        );
        console.log("Middleware响应:", response.data);

        console.log("测试Python...");
        const pythonResponse = await this.$http.post(
          "http://localhost:8009/agent/chat",
          {
            messages: [{ role: "user", content: "你好" }],
          },
        );
        console.log("Python响应:", pythonResponse.data);
      } catch (error) {
        console.error("测试失败:", error);
      }
    },
    async loadTools() {
      try {
        this.loading = true;
        const response = await agentApi.getTools(this.selectedCategory);

        if (response.code === 200) {
          this.tools = response.data;
        } else {
          this.error = response.message;
        }
      } catch (error) {
        this.error = "加载工具失败: " + error.message;
      } finally {
        this.loading = false;
      }
    },

    getToolIcon(category) {
      const icons = {
        education: "fas fa-graduation-cap",
        analytics: "fas fa-chart-line",
        general: "fas fa-robot",
        research: "fas fa-flask",
      };
      return icons[category] || "fas fa-robot";
    },

    getCategoryName(category) {
      const names = {
        education: "教育",
        analytics: "数据分析",
        general: "通用",
        research: "研究",
      };
      return names[category] || "其他";
    },

    goToChat(toolId) {
      console.log("点击工具，ID:", toolId, "类型:", typeof toolId);
      this.$router.push({ name: "AgentChat", params: { toolId } });
    },
  },
};
</script>

<style scoped>
.agent-tools {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.page-header {
  text-align: center;
  margin-bottom: 40px;
}

.page-header h1 {
  color: #333;
  margin-bottom: 10px;
}

.page-header p {
  color: #666;
  font-size: 16px;
}

.category-filter {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-bottom: 40px;
}

.filter-btn {
  padding: 8px 20px;
  border: 1px solid #ddd;
  border-radius: 20px;
  background: white;
  cursor: pointer;
  transition: all 0.3s ease;
}

.filter-btn:hover {
  border-color: #6d28d9;
  color: #6d28d9;
}

.filter-btn.active {
  background: #6d28d9;
  color: white;
  border-color: #6d28d9;
}

.tool-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 24px;
}

.tool-card {
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  padding: 24px;
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  cursor: pointer;
}

.tool-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  border-color: #6d28d9;
}

.tool-icon {
  width: 60px;
  height: 60px;
  margin: 0 auto 20px;
  border-radius: 12px;
  background: linear-gradient(135deg, #6d28d9, #8b5fbf);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
}

.tool-name {
  font-size: 20px;
  font-weight: 600;
  text-align: center;
  margin-bottom: 12px;
  color: #333;
}

.tool-description {
  color: #666;
  font-size: 14px;
  line-height: 1.6;
  text-align: center;
  margin-bottom: 16px;
}

.tool-category {
  display: inline-block;
  background: #f0f0f0;
  color: #666;
  padding: 6px 12px;
  border-radius: 16px;
  font-size: 12px;
  margin: 0 auto;
  display: block;
  width: fit-content;
}

.loading-container,
.error-container {
  text-align: center;
  padding: 60px 20px;
}

.loading {
  font-size: 18px;
  color: #666;
}

.error {
  font-size: 16px;
  color: #e53e3e;
  background: #fed7d7;
  padding: 12px 20px;
  border-radius: 8px;
  display: inline-block;
}

@media (max-width: 768px) {
  .tool-grid {
    grid-template-columns: 1fr;
  }

  .category-filter {
    flex-wrap: wrap;
    gap: 8px;
  }
}
</style>
