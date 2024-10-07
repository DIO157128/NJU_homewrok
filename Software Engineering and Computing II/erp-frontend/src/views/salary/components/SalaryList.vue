<template>
  <div class="card">
    <el-card v-for="(item, index) in list" :index="item.index" :key="item.id" shadow="hover">
      <template #header>
        <el-row>
          <el-col :span="18">
            <span><strong>id: </strong>{{item.id}}</span>
            <el-button v-if="authorization() === 1" style="margin-left: 10px"
              type="success" icon="el-icon-check" circle size="mini" @click="approval(item.id)"></el-button>
            <el-button v-if="authorization() === 1"
              type="danger" icon="el-icon-close" circle size="mini" @click="deny(item.id)"></el-button>
            <el-button v-if="authorization() === 2" style="margin-left: 10px"
              type="primary" icon="el-icon-check" circle size="mini" @click="approval(item.id)"></el-button>
            <el-button v-if="authorization() === 2" 
              type="danger" icon="el-icon-close" circle size="mini" @click="deny(item.id)"></el-button>
            <span style="margin-left: 10px">
              <!-- 如果是年终奖类型，则直接显示审核通过 -->
              <el-tag v-if="type === 3 || item.type===2" effect="dark" type='success'>审核通过</el-tag>
              <el-tag v-if="type === 4 && item.type===1" effect="dark" type='danger'>审核未通过</el-tag>
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
            收起</el-button>
          </el-col>
        </el-row>
      </template>
      <div>
        <el-row>
          <el-col :span="12">
            <span><strong>员工姓名:  </strong>{{item.staffName}}</span>
          </el-col>
          <el-col :span="12">
            <span><strong>税前工资:  </strong>{{item.dueSalary}}(元)</span>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <span><strong>缴纳税额:  </strong>{{item.tax}}(元)</span>
          </el-col>
          <el-col :span="12">
            <span><strong>税后工资:  </strong>{{item.actualSalary}}(元)</span>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script>
import { firstApproval, secondApproval} from '../../../network/salary'
export default {
  name: 'SalaryList',
  props: {
    list: Array,
    type: Number
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
      if (this.type === 1 && sessionStorage.getItem('role') === 'HR') {
        return 1
      } else if (this.type === 2 && sessionStorage.getItem('role') === 'GM') {
        return 2
      }
    },
    approval(id) {
      let config = {
        params: {
          salarySheetId: id,
          state: this.type === 1 ? 'PENDING_LEVEL_2' : 'SUCCESS'
        }
      }
      if (this.type === 1) {
        firstApproval(config).then(res => {
          this.$emit("refresh")
          this.$message({
            message: '操作成功!',
            type: 'success'
          })
        })
      } else {
        secondApproval(config).then(res => {
          this.$emit("refresh")
          this.$message({
            message: '操作成功!',
            type: 'success'
          })
        })
      }
    },
    deny(id) {
      let config = {
        params: {
          salarySheetId: id,
          state: 'FAILURE'
        }
      }
      if (this.type === 1) {
        firstApproval(config).then(res => {
          this.$emit("refresh")
          this.$message({
            message: '操作成功!',
            type: 'success'
          })
        })
      } else {
        secondApproval(config).then(res => {
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