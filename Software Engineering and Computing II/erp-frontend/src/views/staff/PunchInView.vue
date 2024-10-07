<template>
  <Layout>
    <Title title="打卡记录"></Title>
    <!-- (设定一个时间段，查看此时间段内的出/入库数量/金额，销售/进货的数量/金额。库存数量要有合计，这一点统一于普适需求。） -->
    <div>
      <span><strong>请选择一个时间段: </strong></span>
      <el-date-picker
        v-model="date"
        type="datetimerange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        @change="getData()">
      </el-date-picker>
      <div  class="mt15">
        <div v-if="date !== ''">
          <h4>{{beginDate}}&nbsp;至&nbsp;{{endDate}}内的</h4>
        </div>

        <div class="mt15">
          <span><strong>打卡记录为: </strong></span>
          <el-table
            :data="sheetContent"
            stripe
            style="width: 100%"
            :header-cell-style="{'text-align':'center'}"
            :cell-style="{'text-align':'center'}"
            class="mt15">
            <el-table-column
              prop="staffId"
              label="员工id"
              width="290">
            </el-table-column>
            <el-table-column
              prop="staffName"
              label="员工姓名"
              width="290">
            </el-table-column>
            <el-table-column
              prop="times"
              label="打卡次数"
              width="290">
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
import { formatDate } from "@/common/utils";
import { getPunchInInfo } from "@/network/staff";

export default {
  components: {
    Layout,
    Title
  },
  data() {
    return {
      date: '',
      sheetContent: []
    }
  },
  computed: {
    beginDate: function(){
      return this.date === '' ? '' : formatDate(this.date[0])
    },
    endDate: function() {
      return this.date === '' ? '' : formatDate(this.date[1])
    }
  },
  created() {
    const config={
      params: {
        beginDateStr: "2000-01-01 00:00:00",
        endDateStr: "2030-01-01 00:00:00"
      }
    }
    getPunchInInfo(config).then(_res=>{
      this.sheetContent = _res.result
      console.log(this.sheetContent)
    })
  },
  methods: {
    getData() {
      const config = {
        params: {
          beginDateStr: this.beginDate,
          endDateStr: this.endDate
        }
      }
      getPunchInInfo(config).then(_res => {
        this.sheetContent = _res.result
        if (this.sheetContent.length === 0) {
          this.$message.error('该时间段内无打卡记录!')
        } else {
          this.$message.success('查询成功!')
        }
      })
    },
    formatDate
  }
};
</script>

<style scoped lang="scss">
.mt15 {
  margin-top: 15px;
}
</style>
