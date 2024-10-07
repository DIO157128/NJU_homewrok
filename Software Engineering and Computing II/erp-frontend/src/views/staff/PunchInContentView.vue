<template>
  <Layout>
    <Title title="员工打卡详情"></Title>
    <!-- (设定一个时间段，查看此时间段内的出/入库数量/金额，销售/进货的数量/金额。库存数量要有合计，这一点统一于普适需求。） -->
    <div>

      <span><strong>请输入员工ID: </strong></span>
      <el-input v-model="staffId" style="width: 25%; margin-right: 5%" placeholder="请输入要查询的员工ID"></el-input>

      <br/>
      <span><strong>请选择时间段: </strong></span>
      <el-date-picker
        v-model="date"
        type="datetimerange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        @change="getData()">
      </el-date-picker>

      <div v-if="date !== ''" class="mt15">
        <h4>{{beginDate}}&nbsp;至&nbsp;{{endDate}}内的</h4>

        <div class="mt15">
          <span><strong>该员工打卡详情记录为: </strong></span>
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
              prop="punchInTime"
              label="打卡时间"
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
import { getPunchContent } from "@/network/staff";

export default {
  components: {
    Layout,
    Title
  },
  data() {
    return {
      staffId: 8,
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
  methods: {
    getData() {
      const config = {
        params: {
          staffId: this.staffId,
          beginDateStr: this.beginDate,
          endDateStr: this.endDate
        }
      }
      getPunchContent(config).then(_res => {
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
