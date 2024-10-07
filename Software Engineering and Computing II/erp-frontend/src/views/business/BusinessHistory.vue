<template>
  <Layout>
    <Title title="经营历程"></Title>
    <span><strong>请选择时间段: </strong></span>
    <el-date-picker
        v-model="date"
        type="datetimerange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期">
    </el-date-picker>
    <div style="margin-top: 10px">
      <span><strong>请输入客户ID: </strong></span>
      <el-input v-model="customerId" style="width: 25%; margin-right: 5%" placeholder="请输入要查询的客户ID"></el-input>

      <span><strong>请输入操作员姓名: </strong></span>
      <el-input v-model="operatorName" style="width: 25%; margin-right: 5%" placeholder="请输入要查询的操作员姓名"></el-input>
      <el-button-group>
        <el-button @click="filter">筛选</el-button>
        <el-button @click="clearFilter">重置筛选</el-button>
      </el-button-group>
    </div>
    <div class="body">
      <el-tabs v-model="activeName" :stretch="true">
        <el-tab-pane label="销售类单据" name="SaleSheet">
          <sale-list :list="saleListPresented"/>
          <sale-return-list :list="saleReturnListPresented"/>
        </el-tab-pane>
        <el-tab-pane label="进货类单据" name="PurchaseSheet">
          <purchase-list :list="purchaseListPresented"/>
          <purchase-return-list :list="purchaseReturnListPresented"/>
        </el-tab-pane>
        <el-tab-pane label="财务类单据" name="FinancialSheet">
          <pay-list :list="payListPresented"/>
          <receive-list :list="receiveListPresented"/>
          <salary-list :list="salaryListPresented"/>
        </el-tab-pane>
      </el-tabs>
    </div>
  </Layout>
</template>

<script>
import Layout from "@/components/content/Layout";
import Title from "@/components/content/Title";
import SaleList from './components/SaleList';
import SaleReturnList from "@/views/business/components/SaleReturnList";
import PurchaseList from "@/views/business/components/PurchaseList";
import PurchaseReturnList from "@/views/business/components/PurchaseReturnList";
import PayList from "@/views/business/components/PayList";
import ReceiveList from "@/views/business/components/ReceiveList";
import SalaryList from "@/views/business/components/SalaryList";
import {businessHistory} from "@/network/business";
import {formatDate} from "@/common/utils";
import {getCustomerById} from "@/network/purchase";


export default {
  name: 'BusinessHistory',
  components: {
    SaleList,
    SaleReturnList,
    PurchaseList,
    PurchaseReturnList,
    PayList,
    ReceiveList,
    SalaryList,
    Layout,
    Title,
  },
  data() {
    return {
      activeName: 'SaleSheet',
      saleListPresented: [],
      saleListRaw: [],
      saleReturnListPresented: [],
      saleReturnListRaw: [],
      purchaseListPresented: [],
      purchaseListRaw: [],
      purchaseReturnListPresented: [],
      purchaseReturnListRaw: [],
      payListPresented: [],
      payListRaw: [],
      receiveListPresented: [],
      receiveListRaw: [],
      salaryListPresented: [],
      salaryListRaw: [],
      date: '',
      customerId: '',
      customerName: '',
      operatorName: ''
    }
  },
  mounted() {
    this.getSheet()
  },
  methods: {
    getSheet() {
      businessHistory({}).then(_res => {
        this.saleListRaw = _res.result.saleList;
        this.saleListPresented = _res.result.saleList;
        this.saleReturnListPresented = _res.result.saleReturnList;
        this.saleReturnListRaw = _res.result.saleReturnList;
        this.purchaseListPresented = _res.result.purchaseList;
        this.purchaseListRaw = _res.result.purchaseList;
        this.purchaseReturnListPresented = _res.result.purchaseReturnList;
        this.purchaseReturnListRaw = _res.result.purchaseReturnList;
        this.purchaseReturnListPresented = _res.result.purchaseReturnList;
        this.purchaseReturnListRaw = _res.result.purchaseReturnList;
        this.payListPresented = _res.result.payList;
        this.payListRaw = _res.result.payList;
        this.receiveListPresented = _res.result.receiveList;
        this.receiveListRaw = _res.result.receiveList;
        this.salaryListPresented = _res.result.salaryList;
        this.salaryListRaw = _res.result.salaryList;
      })
    },
    filter() {
      //filter payList
      this.payListPresented = [];
      for (let item of this.payListRaw) {
        if ((this.date === '' ||
                (formatDate(item.createDate) < formatDate(this.date[1]) && formatDate(item.createDate) > formatDate(this.date[0])))
            && (this.customerId === '' || item.payerId === Number(this.customerId)) &&
            (this.operatorName === '' || item.operator === this.operatorName)) {
          this.payListPresented.push(item);
        }
      }
      //filter receiveList
      //get customerName according to customerId
      let id = this.customerId;
      getCustomerById({
        params: {
          id: id,
        }
      }).then(_res => {
        let customer = _res.result;
        this.customerName = customer.name;
        this.receiveListPresented = [];
        for (let item of this.receiveListRaw) {
          if ((this.date === '' ||
                  (formatDate(item.createDate) < formatDate(this.date[1]) && formatDate(item.createDate) > formatDate(this.date[0])))
              && (this.customerId === '' || item.customerName === this.customerName) &&
              (this.operatorName === '' || item.operator === this.operatorName)) {
            this.receiveListPresented.push(item);
          }
        }
      });
      //filter saleList
      this.saleListPresented = [];
      for (let item of this.saleListRaw) {
        if ((this.customerId === '' || item.supplier === Number(this.customerId)) &&
            (this.operatorName === '' || item.operator === this.operatorName)) {
          this.saleListPresented.push(item);
        }
      }
      //filter saleReturnList
      this.saleReturnListPresented = [];
      for (let item of this.saleReturnListRaw) {
        if ((this.date === '' ||
                (formatDate(item.createTime) < formatDate(this.date[1]) && formatDate(item.createTime) > formatDate(this.date[0]))) &&
            (this.operatorName === '' || item.operator === this.operatorName)) {
          this.saleReturnListPresented.push(item);
        }
      }
      //filterPurchaseList
      this.purchaseListPresented = [];
      for (let item of this.purchaseListRaw) {
        if ((this.customerId === '' || item.supplier === Number(this.customerId)) &&
            (this.operatorName === '' || item.operator === this.operatorName)) {
          this.purchaseListPresented.push(item);
        }
      }
      //filterPurchaseReturnList
      this.purchaseReturnListPresented = [];
      for (let item of this.purchaseReturnListRaw) {
        if ((this.date === '' ||
                (formatDate(item.createTime) < formatDate(this.date[1]) && formatDate(item.createTime) > formatDate(this.date[0]))) && (this.customerId === '' || item.supplier === this.customerId)
            && (this.operatorName === '' || item.operator === this.operatorName)) {
          this.purchaseListPresented.push(item);
        }
      }
    },
    clearFilter() {
      this.getSheet()
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
