<template>
  <Layout>
    <Title title="销售明细"></Title>
    <!-- (设定一个时间段，查看此时间段内的出/入库数量/金额，销售/进货的数量/金额。库存数量要有合计，这一点统一于普适需求。） -->
    <div>
      <span><strong>请选择一个时间段: </strong></span>
      <el-date-picker
          v-model="date"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          @change="filterDate()">
      </el-date-picker>
    </div>
    <div class="mt15">
      <el-button @click="clearFilter">重置筛选</el-button>
      <el-button @click="exportAsExcel" style="float: right">导出Excel</el-button>
      <el-table
          ref="tableRef"
          :data="saleDetail"
          stripe
          style="width: 100%"
          :header-cell-style="{'text-align':'center'}"
          :cell-style="{'text-align':'center'}"
          class="mt15">
        <el-table-column
            label="时间"
            width="200">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column
            prop="pname"
            label="商品"
            width="150"
            :filter-multiple="true"
            :filters="filterData('pname')"
            :filter-method="filterTag"
            filter-placement="bottom-end"
        >
        </el-table-column>
        <el-table-column
            prop="sellerName"
            label="客户"
            width="150"
            :filter-multiple="true"
            :filters="filterData('sellerName')"
            :filter-method="filterTag"
            filter-placement="bottom-end"
        >
        </el-table-column>
        <el-table-column
            prop="categoryType"
            label="型号"
            width="200"
            :filter-multiple="true"
            :filters="filterData('categoryType')"
            :filter-method="filterTag"
            filter-placement="bottom-end"
        >
        </el-table-column>
        <el-table-column
            prop="quantity"
            label="数量"
            width="150"
        >
        </el-table-column>
        <el-table-column
            prop="unit_price"
            label="单价(元)"
            width="150"
        >
        </el-table-column>
        <el-table-column
            prop="total_price"
            label="总价(元)"
            width="150"
        >
        </el-table-column>
      </el-table>
    </div>
  </Layout>
</template>

<script>
import Layout from "@/components/content/Layout";
import Title from "@/components/content/Title";
import {formatDate} from "@/common/utils";
import {saleDetail} from "@/network/business";
import axios from "axios";


export default {
  components: {
    Layout,
    Title
  },
  data() {
    return {
      date: '',
      saleDetailRaw: [],
      saleDetail: [],
    }
  },
  mounted() {
    saleDetail().then(_res => {
      this.saleDetailRaw = _res.result;
      this.saleDetail = _res.result;
    }).catch(err => {
      this.$message({
        type: 'error',
        message: '获取销售明细失败!' + err
      })
    })
  },
  computed: {
    // 找到所有可能的值的集合
    filterData() {
      return function (data) {
        let obj = [];
        //找到对应的数据 并添加到obj
        this.saleDetail.filter(item => {
          obj.push({
            text: item[data],
            value: item[data]
          })
        })
        return this.deWeight(obj)
      }
    },
  },
  methods: {
    filterTag(value, row, column) {
      const property = column['property'];
      return row[property] === value;
    },
    deWeight(arr) {
      for (let i = 0; i < arr.length - 1; i++) {
        for (let j = i + 1; j < arr.length; j++) {
          if (arr[i].text === arr[j].text) {
            arr.splice(j, 1);
            j--;
          }
        }
      }
      return arr;
    },
    formatDate,
    //根据日期把所有的明细中符合条件的送到saleDetail里
    filterDate() {
      this.saleDetail = [];
      for (let item of this.saleDetailRaw) {
        if (formatDate(item.createTime) < formatDate(this.date[1]) && formatDate(item.createTime) > formatDate(this.date[0])) {
          this.saleDetail.push(item);
        }
      }
    },
    //重置过滤器
    clearFilter() {
      this.$refs.tableRef.clearFilter();
      this.saleDetail = this.saleDetailRaw;
      this.date = '';
    },

    exportAsExcel() {
      //TODO 导出Excel
      axios({
        method:"get",
        url:"/api/business/sale-detail/excel",
        responseType:"blob"
      })
          .then(res=>{
            console.log(res);
            let blob=new Blob([res.data],{
              type:"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});
            const link =document.createElement("a");
            link.style.display="none";
            link.href=URL.createObjectURL(blob);
            link.setAttribute("download",decodeURI(res.headers['filename']));
            link.download="销售明细";
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
          })
          .catch(error=>{
            this.$Notice.error({
              title:"错误",
              desc:"系统数据错误"
            });
            console.log(error);
          });
    },
  }
};
</script>

<style scoped lang="scss">
.mt15 {
  margin-top: 15px;
}
</style>
