<template>
  <div class="modeling-container">
    <div
      style="
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;
        padding-left: 14px;
      "
    >
      <h1 class="page-title">研究生信息列表</h1>
      <div class="search-container">
        <el-select
          v-model="searchCategory"
          placeholder="选择搜索类别"
          style="width: 150px; margin-right: 10px"
        >
          <el-option
            v-for="column in searchableColumns"
            :key="column.prop"
            :label="column.label"
            :value="column.prop"
          ></el-option>
        </el-select>
        <el-input
          v-model="search"
          placeholder="搜索..."
          style="width: 400px"
          @keyup.enter.native="handleSearch"
        >
          <el-button
            slot="append"
            icon="el-icon-search"
            @click="handleSearch"
          ></el-button>
        </el-input>
      </div>
    </div>
    <el-table :data="paginatedData" style="width: 100%" border stripe>
      <el-table-column
        type="index"
        label="序号"
        width="60"
        align="center"
      ></el-table-column>
      <el-table-column prop="name" label="姓名" width="85">
        <template #header> 姓名 </template>
        <template #default="scope">
          <el-button type="text" @click="navigateToDetail(scope.$index)">{{
            scope.row.name
          }}</el-button>
        </template>
      </el-table-column>
      <el-table-column
        prop="studentId"
        label="学号"
        width="120"
      ></el-table-column>
      <el-table-column prop="gender" label="性别" width="80"></el-table-column>
      <el-table-column prop="age" label="年龄" width="80"></el-table-column>
      <el-table-column prop="grade" label="年级" width="200"></el-table-column>
      <el-table-column
        prop="college"
        label="学院"
        min-width="200"
      ></el-table-column>
      <el-table-column
        prop="major"
        label="专业"
        min-width="150"
      ></el-table-column>
      <el-table-column
        prop="supervisor"
        label="导师"
        width="100"
      ></el-table-column>
      <el-table-column
        prop="counselor"
        label="辅导员"
        width="100"
      ></el-table-column>
      <el-table-column
        prop="phone"
        label="联系电话"
        min-width="145"
      ></el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      background
      layout="total, sizes, prev, pager, next"
      :total="filteredData.length"
      :page-sizes="[10, 20, 50]"
      :page-size="pageSize"
      :current-page.sync="currentPage"
      class="pagination-container"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
import { getPostgraduateList } from "@/api/wideTable";

export default {
  data() {
    return {
      search: "",
      searchCategory: "",
      tableData: [],
      wideTableData: [],
      currentPage: 1,
      pageSize: 10,
      searchableColumns: [
        { prop: "name", label: "姓名" },
        { prop: "studentId", label: "学号" },
        { prop: "gender", label: "性别" },
        { prop: "age", label: "年龄" },
        { prop: "grade", label: "年级" },
        { prop: "college", label: "学院" },
        { prop: "major", label: "专业" },
        { prop: "supervisor", label: "导师" },
        { prop: "counselor", label: "辅导员" },
        { prop: "phone", label: "联系电话" },
      ],
      searchResult: [],
    };
  },
  computed: {
    filteredData() {
      return this.searchResult.length > 0 ? this.searchResult : this.tableData;
    },
    paginatedData() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.filteredData.slice(start, end);
    },
  },
  async mounted() {
    try {
      // 调用接口获取学生宽表数据
      const response = await getPostgraduateList();
      this.wideTableData = response.data || [];

      // 映射数据到tableData
      this.tableData = this.wideTableData.map((item) => ({
        name: item.graduateName,
        studentId: item.studentId,
        gender: item.gender,
        age: item.currentAge,
        grade: item.currentGrade,
        college: item.school,
        major: item.major,
        phone: item.cellPhoneNumber,
        supervisor: item.supervisorName,
        counselor: item.counselorName,
      }));
    } catch (error) {
      console.error("获取学生宽表数据失败:", error);
      this.$message.error("获取学生数据失败，请稍后重试");
    }
  },
  methods: {
    navigateToDetail(index) {
      const studentId =
        this.tableData[index + this.pageSize * (this.currentPage - 1)]
          .studentId;

      // 获取当前行的完整宽表数据
      const currentRow = this.wideTableData.find(
        (item) => item.studentId === studentId,
      );

      this.$router.push({
        name: "GraduateInformationDetail",
        params: {
          studentId: studentId,
          graduateData: JSON.stringify(currentRow),
        },
      });
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.currentPage = 1;
    },
    handleCurrentChange(val) {
      this.currentPage = val;
    },
    handleSearch() {
      if (!this.search || !this.searchCategory) {
        this.searchResult = [];
        return;
      }

      this.searchResult = this.tableData.filter((item) => {
        const value = String(item[this.searchCategory] || "").toLowerCase();
        return value.includes(this.search.toLowerCase());
      });

      // 重置到第一页
      this.currentPage = 1;
    },
  },
};
</script>

<style scoped lang="scss">
.modeling-container {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

  .page-title {
    text-align: center;
    color: #2c3e50;
    font-size: 24px;
  }

  .pagination-container {
    display: flex;
    justify-content: flex-end;
    margin-top: 20px;
  }

  .search-container {
    display: flex;
    align-items: center;
  }

  ::v-deep .el-table {
    th {
      background-color: #f5f7fa !important;
      font-weight: 600;
    }

    .el-table__body tr:hover > td {
      background-color: #f0f7ff !important;
    }

    .el-table__body td {
      padding: 8px 8px;
    }
  }
}
</style>
