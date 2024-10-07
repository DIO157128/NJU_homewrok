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
          <el-col :span="5">
            <span><strong>销售商id: </strong>{{ item.supplier }}</span>
          </el-col>
          <el-col :span="5">
            <span><strong>操作员: </strong>{{ item.operator }}</span>
          </el-col>
          <el-col :span="5">
            <span><strong>业务员: </strong>{{ item.salesman }}</span>
          </el-col>
          <el-col :span="6">
            <span><strong>折让前总额: </strong>{{ item.rawTotalAmount }}(元)</span>
          </el-col>
          <el-button @click="red(item.index)" style="float: right">红冲</el-button>
        </el-row>
        <el-row style="margin-top: 15px">
          <el-col :span="5">
            <span><strong>折扣: </strong>{{ item.discount }}</span>
          </el-col>
          <el-col :span="5">
            <span><strong>使用代金券总额: </strong>{{ item.voucherAmount }}(元)</span>
          </el-col>
          <el-col :span="6">
            <span><strong>折让后总额: </strong>{{ item.finalAmount }}(元)</span>
          </el-col>
        </el-row>
        <el-row style="margin-top: 15px">
          <el-col :span="24">
            <span><strong>备注: </strong>{{ item.remark }}</span>
          </el-col>
        </el-row>
        <div v-if="showAll[index]" style="margin-top: 15px">
          <div style="margin-bottom: 15px"><strong>详细商品清单:</strong></div>
          <el-table
              :data="item.saleSheetContent"
              stripe
              style="width: 100%"
              :header-cell-style="{'text-align':'center'}"
              :cell-style="{'text-align':'center'}">
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
import {createSale} from "@/network/sale";

export default {
  name: 'SaleList',
  props: {
    list: Array
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
      newSheet.saleSheetContent = this.list.at(index).saleSheetContent
      for (let item of newSheet.saleSheetContent) {
        item.quantity = -1 * item.quantity;
      }
      newSheet.state = null
      newSheet.finalAmount = -1 * this.list.at(index).finalAmount
      newSheet.voucherAmount = -1 * this.list.at(index).voucherAmount
      createSale(newSheet).then(_res => {
        if (_res.msg === 'Success') {
          this.$message.success('红冲成功!')
        }
        //刷新页面
        this.$router.go(0)
      })

    },
    redCopy(index) {

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
