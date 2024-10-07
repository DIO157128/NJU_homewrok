<template>
  <div class="card">
    <el-card v-for="(item, index) in list" :index="item.index" :key="item.id" shadow="hover">
      <template #header>
        <el-row>
          <el-col :span="18">
            <span><strong>id: </strong>{{item.id}}</span>
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
          <el-col :span="8">
            <span><strong>关联的进货单id: </strong>{{item.purchaseSheetId}}</span>
          </el-col>
          <el-col :span="3">
            <span><strong>操作员: </strong>{{item.operator}}</span>
          </el-col>
          <el-col :span="6">
            <span><strong>总额合计: </strong>{{item.totalAmount}}(元)</span>
          </el-col>
          <el-button @click="red(item.index)" style="float: right">红冲</el-button>
        </el-row>
        <el-row style="margin-top: 15px">
          <el-col :span="24">
            <span><strong>备注: </strong>{{item.remark}}</span>
          </el-col>
        </el-row>
        <div v-if="showAll[index]" style="margin-top: 15px">
          <div style="margin-bottom: 15px"><strong>详细商品清单:</strong></div>
          <el-table
            :data="item.purchaseReturnsSheetContent"
            stripe
            style="width: 100%"
            :header-cell-style="{'text-align':'center'}"
            :cell-style="{'text-align':'center'}">
            <el-table-column
              prop="id"
              label="id"
              width="100">
            </el-table-column>
            <el-table-column
              prop="pid"
              label="商品id"
              width="180">
            </el-table-column>
            <el-table-column
              prop="quantity"
              label="数量"
              width="200">
            </el-table-column>
            <el-table-column
              prop="unitPrice"
              label="单价(元)"
              width="200">
            </el-table-column>
            <el-table-column
              prop="totalPrice"
              label="金额(元)"
              width="200">
            </el-table-column>
            <el-table-column
              prop="remark"
              label="备注">
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import {createPurchaseReturn} from "@/network/purchase";

export default {
  name: "PurchaseReturnList",
  props: {
    list: Array,
    type: Number,
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
      newSheet.purchaseReturnsSheetContent = this.list.at(index).purchaseReturnsSheetContent
      for (let item of newSheet.purchaseReturnsSheetContent) {
        item.quantity = -1 * item.quantity;
      }
      newSheet.state = null
      createPurchaseReturn(newSheet).then(_res => {
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
