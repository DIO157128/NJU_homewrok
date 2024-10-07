<template>
  <Layout>
    <Title title="收款管理"></Title>
    <el-button type="primary" size="medium" @click="dialogVisible = true">制定收款单</el-button>
    <div class="body">
      <el-tabs v-model="activeName" :stretch="true">
        <el-tab-pane label="待审批" name="PENDING">
          <div v-if="pendingList.length === 0">
            <el-empty description="暂无数据"></el-empty>
          </div>
          <div v-else>
            <receive-list :list="pendingList" :type="1" @refresh="getReceive()"/>
          </div>
        </el-tab-pane>
        <el-tab-pane label="审批完成" name="SUCCESS">
          <div v-if="successList.length === 0">
            <el-empty description="暂无数据"></el-empty>
          </div>
          <div v-else>
            <receive-list :list="successList" :type="2" @refresh="getReceive()"/>
          </div>
        </el-tab-pane>
        <el-tab-pane label="审批失败" name="FAILURE">
          <div v-if="failureList.length === 0">
            <el-empty description="暂无数据"></el-empty>
          </div>
          <div v-else>
            <receive-list :list="failureList" :type="3"/>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
    <el-dialog
        title="创建收款单"
        :visible.sync="dialogVisible"
        width="40%"
        :before-close="handleClose">
      <div style="width: 90%; margin: 0 auto">
        <el-form :model="receiveForm" label-width="100px" ref="receiveForm">
          <el-form-item label="客户: " prop="customerName">
            <el-select v-model="receiveForm.customerName" placeholder="请选择客户">
              <el-option
                  v-for="item in customers"
                  :key="item.name"
                  :label="item.name"
                  :value="item.name">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="操作员: " prop="operator">
            <el-input v-model="receiveForm.operator" disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="实际金额: " prop="actualAmount">
            <el-input v-model="receiveForm.actualAmount"></el-input>
          </el-form-item>
          <el-form-item label="折扣金额: " prop="discount">
            <el-input v-model="receiveForm.discount"></el-input>
          </el-form-item>
          <el-form-item
              v-for="(item, index) in receiveForm.receiveSheetContent"
              :key="index"
              :label="'条目' + index">
            <div>
              <el-select v-model="item.bankAccount" placeholder="请选择公司账户">
                <el-option
                    v-for="item in bankAccounts"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name">
                </el-option>
              </el-select>
              <el-input v-model="item.transferAmount" style="width: 40%; margin-right: 5%"
                        placeholder="请输入转账金额"></el-input>
            </div>
            <div style="margin-top: 10px">
              <el-input v-model="item.remark" style="width: 70%; margin-right: 10%" placeholder="请填写备注"></el-input>
              <el-button type="text" size="small" @click="addProduct"
                         v-if="index === receiveForm.receiveSheetContent.length - 1">添加
              </el-button>
              <el-button type="text" size="small" @click.prevent="removeProduct(item)" v-if="index !== 0">删除</el-button>
            </div>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm('receiveForm')">立即创建</el-button>
      </span>
    </el-dialog>
  </Layout>
</template>

<script>
import Layout from "@/components/content/Layout";
import Title from "@/components/content/Title";
import ReceiveList from "@/views/finance/components/ReceiveList";
import {getAllReceive, createReceive} from '@/network/finance';
import {getAllCustomerReal} from '@/network/purchase';
import {companyAccountQueryAll} from "@/network/finance";

export default {
  name: 'ReceiveView',
  components: {
    Layout,
    Title,
    ReceiveList
  },
  data() {
    return {
      activeName: 'PENDING',
      receiveList: [],
      pendingList: [],
      successList: [],
      failureList: [],
      dialogVisible: false,
      receiveForm: {
        customerName: '',
        customerType: '',
        operator: sessionStorage.getItem("name"),
        allAmount: '',
        discount: '',
        actualAmount: '',
        receiveSheetContent: [
          {
            bankAccount: '',
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
    this.getReceive()
    companyAccountQueryAll({}).then(_res => {
      let res = _res.result
      res.forEach(item => this.bankAccounts.push({name: item.name}))
    })
    getAllCustomerReal({}).then(_res => {
      this.customers = _res.result
    })
  },
  methods: {
    getReceive() {
      getAllReceive({}).then(_res => {
        this.receiveList = _res.result
        this.pendingList = this.receiveList.filter(item => item.state === '待审批')
        this.successList = this.receiveList.filter(item => item.state === '审批完成')
        this.failureList = this.receiveList.filter(item => item.state === '审批失败')
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
      this.receiveForm.id = null
      this.receiveForm.state = null
      this.receiveForm.createDate = null
      this.setCustomerType()
      this.setAllAmount()
      this.receiveForm.receiveSheetContent.forEach((item) => {
        item.id = null
        item.receiveSheetId = null
        if (item.bankAccount === "" || item.transferAmount === null || item.transferAmount < 0) {
          isValid = false
        }
      })
      console.log(this.receiveForm.customerName)
      if (isValid) {
        createReceive(this.receiveForm).then(_res => {
          if (_res.msg === 'Success') {
            this.$message.success('创建成功!')
            this.resetForm()
            this.dialogVisible = false
            this.getReceive()
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
          message: '账号不能为空，转账金额不能为空/小于0!'
        })

      }

    },
    setCustomerType() {
      this.customers.forEach(item => {
        if (item.name === this.receiveForm.customerName) {
          this.receiveForm.customerType = item.type;
        }
      })
    },
    setAllAmount() {
      this.receiveForm.allAmount = this.receiveForm.actualAmount + this.receiveForm.discount
    },
    resetForm() {
      this.receiveForm = {
        customerName: '',
        customerType: '',
        operator: sessionStorage.getItem("name"),
        allAmount: '',
        discount: '',
        actualAmount: '',
        receiveSheetContent: [
          {
            bankAccount: '',
            remark: '',
            transferAmount: ''
          }
        ]
      }
    },
    addProduct() {
      this.receiveForm.receiveSheetContent.push({});
    },
    removeProduct(item) {
      let index = this.receiveForm.receiveSheetContent.indexOf(item)
      if (index !== -1) {
        this.receiveForm.receiveSheetContent.splice(index, 1)
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
