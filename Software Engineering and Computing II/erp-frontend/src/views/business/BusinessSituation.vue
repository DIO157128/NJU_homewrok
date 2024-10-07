<template>
  <Layout>
    <Title title="经营情况"></Title>
    <div>
      <span><strong>请选择一个时间段: </strong></span>
      <el-date-picker
          v-model="date"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format='yyyy/MM/dd hh:mm:ss'
          @change="getData()">
      </el-date-picker>
      <div  class="mt15">
        <div class="mt15">
          <span><strong>经营情况为: </strong></span>
          <el-table
              :data="dataPresented"
              stripe
              style="width: 100%"
              :header-cell-style="{'text-align':'center'}"
              :cell-style="{'text-align':'center'}"
              class="mt15">
            <el-table-column
                prop="saleRevenueRaw"
                label="销售总额"
                width="150">
            </el-table-column>
            <el-table-column
                prop="saleRevenueDiscount"
                label="销售折扣"
                width="150">
            </el-table-column>
            <el-table-column
                prop="saleRevenueReal"
                label="实际销售额"
                width="150">
            </el-table-column>
            <el-table-column
                prop="sellingCost"
                label="销售成本"
                width="150">
            </el-table-column>
            <el-table-column
                prop="labourCost"
                label="人力成本"
                width="150">
            </el-table-column>
            <el-table-column
                prop="profit"
                label="利润"
                width="150">
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
  </Layout>
</template>

<script>
import Layout from "@/components/content/Layout";
import Title from "@/components/content/Title";
import {businessSituation} from "@/network/business";

export default {
  components: {
    Layout,
    Title
  },
  data() {
    return {
      date: [],
      datas: [],
      dataPresented:[]
    }
  },
  methods: {
    getData() {
      this.dataPresented=[]
      const config = {
        params: {
          fromDate: this.date[0],
          toDate: this.date[1]
        }
      }
      businessSituation(config).then(_res => {
            this.dataPresented.push(_res.result)
        console.log(this.dataPresented)
        this.$message.success('查询成功!')
        this.$emit("refresh")
      })
    }
  }
};
</script>

<style scoped lang="scss">
.mt15 {
  margin-top: 15px;
}
</style>
