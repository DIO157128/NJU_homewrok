<template>
  <Layout>
    <Title title="工资管理"></Title>
    <el-button type="primary" size="medium" @click="generateSalary" v-if="permit(PATH.SALARY_VIEW.requiresAuth)">制定工资单
    </el-button>
    <el-button type="primary" size="medium" @click="generateAnnual" v-if="permit(PATH.ANNUAL_VIEW.requiresAuth)">制定年终奖
    </el-button>
    <div class="body">
      <el-tabs v-model="activeName" :stretch="true">
        <el-tab-pane label="待一级审批" name="PENDING_LEVEL_1">
          <div v-if="pendingLevel1List.length === 0">
            <el-empty description="暂无数据"></el-empty>
          </div>
          <div v-else>
            <salary-list :list="pendingLevel1List" :type="1" @refresh="getSalary()"/>
          </div>
        </el-tab-pane>
        <el-tab-pane label="待二级审批" name="PENDING_LEVEL_2">
          <div v-if="pendingLevel2List.length === 0">
            <el-empty description="暂无数据"></el-empty>
          </div>
          <div v-else>
            <salary-list :list="pendingLevel2List" :type="2" @refresh="getSalary()"/>
          </div>
        </el-tab-pane>
        <el-tab-pane label="审批完成" name="SUCCESS">
          <div v-if="successList.length === 0">
            <el-empty description="暂无数据"></el-empty>
          </div>
          <div v-else>
            <salary-list :list="successList" :type="3"/>
          </div>
        </el-tab-pane>
        <el-tab-pane label="审批失败" name="FAILURE">
          <div v-if="failureList.length === 0">
            <el-empty description="暂无数据"></el-empty>
          </div>
          <div v-else>
            <salary-list :list="failureList" :type="4"/>
          </div>
        </el-tab-pane>

        <el-tab-pane label="年终奖" name="ANNUAL" v-if="permit(PATH.ANNUAL_VIEW.requiresAuth)">
          <div v-if="failureList.length === 0">
            <el-empty description="暂无数据"></el-empty>
          </div>
          <div v-else>
            <salary-list :list="annualList" :type="4"/>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </Layout>
</template>

<script>
import Layout from "@/components/content/Layout";
import Title from "@/components/content/Title";
import SalaryList from "@/views/salary/components/SalaryList";
import {getAllSalary, generateAllSalary,generateAllAnnual} from "@/network/salary";
import {PATH} from "@/common/const";

export default {
  name: 'SalaryView',
  components: {
    Layout,
    Title,
    SalaryList,
  },
  data() {
    return {
      PATH: PATH,
      activeName: 'PENDING_LEVEL_1',
      salaryList: [],
      pendingLevel1List: [],
      pendingLevel2List: [],
      successList: [],
      failureList: [],
      annualList: [],    //年终奖
    }
  },
  mounted() {
    this.getSalary()
  },
  methods: {
    generateSalary() {      //生成
      generateAllSalary({}).then(_res => {
        if (_res.code === "D0001") {
          this.$message({
            type: 'error',
            message: _res.msg
          });
        }else{
          this.$message({
            message: '操作成功!',
            type: 'success'
          })
        }
      })
      this.getSalary()
    },
    getSalary() {         //获取
      getAllSalary({}).then(_res => {
        this.salaryList = _res.result
        this.pendingLevel1List = this.salaryList.filter(item => item.state === '待HR审批' && item.type === 1)
        this.pendingLevel2List = this.salaryList.filter(item => item.state === '待总经理审批' && item.type === 1)
        this.successList = this.salaryList.filter(item => item.state === '审批通过' && item.type === 1)
        this.failureList = this.salaryList.filter(item => item.state === '审批失败' && item.type === 1)
        this.annualList = this.salaryList.filter(item => item.type === 2 )      // 还是和工资的记录分开吧，不然无法查看记录
      })
    },
    generateAnnual() {
      generateAllAnnual({}).then(_res => {
        if (_res.code === "D0002") {
          this.$message({
            type: 'error',
            message: _res.msg
          });
        }else{
          this.$message({
            message: '操作成功!',
            type: 'success'
          })
        }
      })
      this.getSalary()
    },
    permit(arr) {
      return arr.some(role => role === sessionStorage.getItem("role"));
    }
  }
}
</script>

<style lang="scss" scoped>
.body {
  margin: 0 auto;
  margin-top: 10px;
  height: 80vh;
  overflow-y: auto;
  width: 100%;
  background: rgba($color: #fff, $alpha: 0.5);
}
</style>