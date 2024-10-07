<template>
  <div class="card">
    <el-card v-for="(item, index) in list" :index="item.index" :key="item.id" shadow="hover">
      <template #header>
        <el-row>
          <el-col :span="18">
            <span><strong>id: </strong>{{ item.id }}</span>
          </el-col>
          <el-col :span="6">
            <el-button class="button" type="text"
                       v-if="!showAll[index]"
                       @click="changeState(index)">
              展开
            </el-button>
            <el-button class="button" type="text"
                       v-if="showAll[index]"
                       @click="changeState(index)">
              收起
            </el-button>
          </el-col>
        </el-row>
      </template>
      <div>
        <el-row>
          <el-col :span="4">
            <span><strong>客户名称: </strong>{{ item.customerName }}</span>
          </el-col>
          <el-col :span="4">
            <span><strong>客户类型: </strong>{{ item.customerType }}</span>
          </el-col>
          <el-col :span="4">
            <span><strong>收款金额: </strong>{{ item.allAmount }}元</span>
          </el-col>
          <el-col :span="4">
            <span><strong>折扣金额: </strong>{{ item.discount }}元</span>
          </el-col>
          <el-col :span="4">
            <span><strong>实际金额: </strong>{{ item.actualAmount }}元</span>
          </el-col>
          <el-button @click="red(item.index)" style="float: right">红冲</el-button>
        </el-row>
        <el-row style="margin-top: 15px">
          <el-col :span="12">
            <span><strong>创建日期: </strong>{{ item.createDate }}</span>
          </el-col>
          <el-col :span="6">
            <span><strong>操作员: </strong>{{ item.operator }}</span>
          </el-col>
        </el-row>
        <el-row style="margin-top: 15px">
          <el-col :span="24">
            <span><strong>备注: </strong>{{ item.remark }}</span>
          </el-col>
        </el-row>
        <div v-if="showAll[index]" style="margin-top: 15px">
          <div style="margin-bottom: 15px"><strong>详细收款清单:</strong></div>
          <el-table
              :data="item.receiveSheetContent"
              stripe
              style="width: 100%"
              :header-cell-style="{'text-align':'center'}"
              :cell-style="{'text-align':'center'}">
            <el-table-column
                prop="id"
                label="id"
                width="180">
            </el-table-column>
            <el-table-column
                prop="bankAccount"
                label="公司账户"
                width="200">
            </el-table-column>
            <el-table-column
                prop="transferAmount"
                label="转账金额(元)"
                width="200">
            </el-table-column>
            <el-table-column
                prop="remark"
                label="备注"
                width="300">
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import {createReceive} from '@/network/finance'

export default {
  name: 'ReceiveList',
  props: {
    list: Array,
    type: Number
    // 1 pending. 2 success, 3 failure
  },
  data() {
    return {
      showAll: [],
    }
  },
  mounted() {
    this.showAll = new Array(this.list.length).fill(false)
  },
  methods: {
    changeState(index) {
      this.$set(this.showAll, index, !this.showAll[index])
    },
    red(index) {
      let newSheet = {}
      newSheet = this.list.at(index)
      newSheet.receiveSheetContent = this.list.at(index).receiveSheetContent
      for (let item of newSheet.receiveSheetContent) {
        item.transferAmount = -1 * item.transferAmount;
      }
      newSheet.state = null
      createReceive(newSheet).then(_res => {
        if (_res.msg === 'Success') {
          this.$message.success('红冲成功!')
        }
        //刷新页面
        this.$router.go(0)
      })
    },
  }
}
</script>

<style lang="scss" scoped>
.card {
  width: 80%;
  margin: 0 auto;

  .button {
    float: right;
    padding: 3px 0
  }
}

.el-card {
  margin-bottom: 20px;
  background: #EEF7F2;
}
</style>
