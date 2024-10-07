<template>
  <div class="card">
    <el-card v-for="(item, index) in list" :index="item.index" :key="item.id" shadow="hover">
      <template #header>
        <el-row>
          <el-col :span="18">
            <span><strong>id: </strong>{{ item.id }}</span>
            <el-button v-if="authorization() === 1" style="margin-left: 10px"
                       type="success" icon="el-icon-check" circle size="mini" @click="approval(item.id)"></el-button>
            <el-button v-if="authorization() === 1"
                       type="danger" icon="el-icon-close" circle size="mini" @click="deny(item.id)"></el-button>
            <span style="margin-left: 10px">
              <el-tag v-if="type === 2" effect="dark" type='success'>审核通过</el-tag>
              <el-tag v-if="type === 3" effect="dark" type='danger'>审核未通过</el-tag>
            </span>
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
          <el-col :span="6">
            <span><strong>银行账户: </strong>{{ item.bankAccount }}</span>
          </el-col>
          <el-col :span="8">
            <span><strong>操作员: </strong>{{ item.operator }}</span>
          </el-col>
          <el-col :span="6">
            <span><strong>应付金额: </strong>{{ item.allAmount }}元</span>
          </el-col>
        </el-row>
        <el-row style="margin-top: 15px">
          <el-col :span="12">
            <span><strong>创建日期: </strong>{{ item.createDate }}</span>
          </el-col>
          <el-col :span="6">
            <span><strong>客户id: </strong>{{ item.payerId }}</span>
          </el-col>
        </el-row>
        <el-row style="margin-top: 15px">
          <el-col :span="24">
            <span><strong>备注: </strong>{{ item.remark }}</span>
          </el-col>
        </el-row>
        <div v-if="showAll[index]" style="margin-top: 15px">
          <div style="margin-bottom: 15px"><strong>详细付款清单:</strong></div>
          <el-table
              :data="item.paySheetContent"
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
                prop="entryName"
                label="条目名称"
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
import {approvePay} from '@/network/finance'

export default {
  name: 'PayList',
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
    authorization() {
      if (this.type === 1 && sessionStorage.getItem('role') === 'GM') {
        return 1
      }
    },
    approval(id) {
      let config = {
        params: {
          paySheetId: id,
          state: 'SUCCESS'
        }
      }
      approvePay(config).then(_res => {
        if (_res.msg === 'Success') {
          this.$emit("refresh");
          this.$message.success('创建成功!')
        } else {
          this.$message({
            type: 'error',
            message: _res.msg
          });
        }
      })
    },
    deny(id) {
      let config = {
        params: {
          paySheetId: id,
          state: 'FAILURE'
        }
      }
      if (this.type === 1) {
        approvePay(config).then(res => {
          this.$emit("refresh")
          this.$message({
            message: '操作成功!',
            type: 'success'
          })
        })
      }
    }
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
