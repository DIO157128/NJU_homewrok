<template>
  <Layout>
    <Title title="付款管理"></Title>
    <el-button type="primary" size="medium" @click="dialogVisible = true">制定付款单</el-button>
    <div class="body">
      <el-tabs v-model="activeName" :stretch="true">
        <el-tab-pane label="待审批" name="PENDING">
          <div v-if="pendingList.length === 0">
            <el-empty description="暂无数据"></el-empty>
          </div>
          <div v-else>
            <pay-list :list="pendingList" :type="1" @refresh="getPay()"/>
          </div>
        </el-tab-pane>
        <el-tab-pane label="审批完成" name="SUCCESS">
          <div v-if="successList.length === 0">
            <el-empty description="暂无数据"></el-empty>
          </div>
          <div v-else>
            <pay-list :list="successList" :type="2" @refresh="getPay()"/>
          </div>
        </el-tab-pane>
        <el-tab-pane label="审批失败" name="FAILURE">
          <div v-if="failureList.length === 0">
            <el-empty description="暂无数据"></el-empty>
          </div>
          <div v-else>
            <pay-list :list="failureList" :type="3"/>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
    <el-dialog
        title="创建付款单"
        :visible.sync="dialogVisible"
        width="40%"
        :before-close="handleClose">
      <div style="width: 90%; margin: 0 auto">
        <el-form :model="payForm" label-width="100px" ref="payForm">
          <el-form-item label="公司账户: " prop="bankAccount">
            <el-select v-model="payForm.bankAccount" placeholder="请选择公司账户">
              <el-option
                  v-for="item in bankAccounts"
                  :key="item.name"
                  :label="item.name"
                  :value="item.name">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="操作员: " prop="operator">
            <el-input v-model="payForm.operator" disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="应付金额: " prop="allAmount">
            <el-input v-model="payForm.allAmount"></el-input>
          </el-form-item>
          <el-form-item label="客户: " prop="payerId">
            <el-select v-model="payForm.payerId" placeholder="请选择客户">
              <el-option
                  v-for="item in customers"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item
              v-for="(item, index) in payForm.paySheetContent"
              :key="index"
              :label="'条目' + index">
            <div>
              <el-input v-model="item.entryName" style="width: 40%; margin-right: 5%" placeholder="请输入条目名称"></el-input>
              <el-input v-model="item.transferAmount" style="width: 40%; margin-right: 5%"
                        placeholder="请输入转账金额"></el-input>
            </div>
            <div style="margin-top: 10px">
              <el-input v-model="item.remark" style="width: 70%; margin-right: 10%" placeholder="请填写备注"></el-input>
              <el-button type="text" size="small" @click="addProduct"
                         v-if="index === payForm.paySheetContent.length - 1">添加
              </el-button>
              <el-button type="text" size="small" @click.prevent="removeProduct(item)" v-if="index !== 0">删除</el-button>
            </div>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm('payForm')">立即创建</el-button>
      </span>
    </el-dialog>
  </Layout>
</template>

<script>
import Layout from "@/components/content/Layout";
import Title from "@/components/content/Title";
import PayList from './components/PayList'
import {getAllPay, createPay} from '@/network/finance'
import {getAllCustomerReal} from '@/network/purchase'
import {companyAccountQueryAll} from "@/network/finance";

export default {
  name: 'PayView',
  components: {
    Layout,
    Title,
    PayList
  },
  data() {
    return {
      activeName: 'PENDING',
      payList: [],
      pendingList: [],
      successList: [],
      failureList: [],
      dialogVisible: false,
      payForm: {
        bankAccount: '',
        operator: sessionStorage.getItem("name"),
        allAmount: '',
        payerId: '',
        paySheetContent: [
          {
            entryName: '',
            remark: '',
            transferAmount: ''
          }
        ]
      },
      bankAccounts: [],
      customers: [],
    }
  },
  mounted() {
    this.getPay()
    companyAccountQueryAll({}).then(_res => {
      let res = _res.result
      res.forEach(item => this.bankAccounts.push({name: item.name}))
    })
    getAllCustomerReal({}).then(_res => {
      this.customers = _res.result
    })
  },
  methods: {
    getPay() {
      getAllPay({}).then(_res => {
        this.payList = _res.result
        this.pendingList = this.payList.filter(item => item.state === '待审批')
        this.successList = this.payList.filter(item => item.state === '审批完成')
        this.failureList = this.payList.filter(item => item.state === '审批失败')
      })
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
          .then(_ => {
            this.resetForm()
            done();
          })
          .catch(_ => {
          });
    },
    submitForm() {
      let isValid = true
      this.payForm.id = null
      this.payForm.state = null
      this.payForm.createDate = null
      this.payForm.paySheetContent.forEach((item) => {
        item.id = null
        item.paySheetId = null
        if (item.transferAmount == null || item.transferAmount < 0) {
          isValid = false
        }
      })
      if (isValid) {
        createPay(this.payForm).then(_res => {
          if (_res.msg === 'Success') {
            this.$message.success('创建成功!')
            this.resetForm()
            this.dialogVisible = false
            this.getPay()
          } else {
            this.$message({
              type: 'error',
              message: _res.msg
            });
          }
        })
      } else {
        this.$message({
          type: 'error',
          message: '转账金额不能为空/小于0!'
        })
      }

    },
    resetForm() {
      this.payForm = {
        bankAccount: '',
        operator: sessionStorage.getItem("name"),
        allAmount: '',
        payerId: '',
        paySheetContent: [
          {
            entryName: '',
            remark: '',
            transferAmount: ''
          }
        ]
      }
    },
    addProduct() {
      this.payForm.paySheetContent.push({});
    },
    removeProduct(item) {
      let index = this.payForm.paySheetContent.indexOf(item)
      if (index !== -1) {
        this.payForm.paySheetContent.splice(index, 1)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.body {
  margin: 10px auto 0;
  height: 80vh;
  overflow-y: auto;
  width: 100%;
  background: rgba($color: #fff, $alpha: 0.5);
}
</style>
