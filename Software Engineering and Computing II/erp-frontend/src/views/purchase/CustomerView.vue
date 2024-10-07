<template>
  <Layout>
    <Title title="客户管理"></Title>
    <el-button type="primary" size="medium" @click="addCustomer">新增客户</el-button>
    <div style="margin-top: 10px">
      <el-table
          :data="customerList"
          stripe
          style="width: 100%"
          :header-cell-style="{'text-align':'center'}"
          :cell-style="{'text-align':'center'}">
        <el-table-column
            prop="id"
            label="id"
            width="60">
        </el-table-column>
        <el-table-column
            prop="name"
            label="姓名"
            width="70">
        </el-table-column>
        <el-table-column
            prop="type"
            label="类型"
            width="100"
            :filters="[{ text: '供应商', value: '供应商' }, { text: '销售商', value: '销售商' }]"
            :filter-method="filterTag"
            filter-placement="bottom-end">
          <template slot-scope="scope">
            <el-tag
                :type="scope.row.type === '供应商' ? 'primary' : 'success'"
                disable-transitions>{{ scope.row.type }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="level"
            label="级别"
            width="50">
        </el-table-column>
        <el-table-column
            prop="phone"
            label="电话"
            width="150">
        </el-table-column>
        <el-table-column
            prop="address"
            label="地址"
            width="150">
        </el-table-column>
        <el-table-column
            prop="zipcode"
            label="邮编"
            width="100">
        </el-table-column>
        <el-table-column
            prop="email"
            label="邮箱"
            width="200">
        </el-table-column>
        <el-table-column
            prop="lineOfCredit"
            label="应收额度(元)"
            width="120">
        </el-table-column>
        <el-table-column
            prop="receivable"
            label="应收(元)"
            width="120">
        </el-table-column>
        <el-table-column
            prop="payable"
            label="应付(元)"
            width="120">
        </el-table-column>
        <el-table-column
            prop="operator"
            label="操作员"
            width="120">
        </el-table-column>
        <el-table-column
            label="操作">
          <template slot-scope="scope">
            <el-button
                @click.native.prevent="editInfo(scope.row.id)"
                type="text"
                size="small">
              编辑
            </el-button>
            <el-button
                @click="deleteCustomer(scope.row.id)"
                type="text"
                size="small">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog
        title="新增客户"
        :visible.sync="addDialogVisible"
        width="30%"
        @close="close()">
      <el-form :model="addForm" :label-width="'100px'" size="mini">
        <el-form-item label="客户类别">
          <el-select v-model="addForm.type">
            <el-option label="供应商" value="供应商"/>
            <el-option label="销售商" value="销售商"/>
          </el-select>
        </el-form-item>
        <el-form-item label="级 别">
          <el-select v-model="addForm.level">
            <el-option label="1" value="1"/>
            <el-option label="2" value="2"/>
            <el-option label="3" value="3"/>
            <el-option label="4" value="4"/>
            <el-option label="5" value="5"/>
          </el-select>
        </el-form-item>
        <el-form-item label="姓 名">
          <el-input v-model="addForm.name" placeholder="请输入客户姓名"></el-input>
        </el-form-item>
        <el-form-item label="电 话">
          <el-input v-model="addForm.phone" placeholder="请输入客户电话"></el-input>
        </el-form-item>
        <el-form-item label="地 址">
          <el-input v-model="addForm.address" placeholder="请输入客户地址"></el-input>
        </el-form-item>
        <el-form-item label="邮 编">
          <el-input v-model="addForm.zipcode" placeholder="请输入客户邮编"></el-input>
        </el-form-item>
        <el-form-item label="电子邮箱">
          <el-input v-model="addForm.email" placeholder="请输入客户电子邮箱"></el-input>
        </el-form-item>
        <el-form-item v-if="isManager" label="应收额度">
          <el-input v-if="isManager" v-model="addForm.lineOfCredit" placeholder="请输入客户应收额度"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleAdd(false)">取 消</el-button>
        <el-button type="primary" @click="handleAdd(true)">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog
        title="修改客户信息"
        :visible.sync="editDialogVisible"
        width="30%"
        @close="close()">
      <el-form :model="editForm" :label-width="'100px'" size="mini">
        <el-form-item label="客户类别">
          <el-select v-model="editForm.type">
            <el-option label="供应商" value="供应商"/>
            <el-option label="销售商" value="销售商"/>
          </el-select>
        </el-form-item>
        <el-form-item label="级 别">
          <el-select v-model="editForm.level">
            <el-option label="1" value="1"/>
            <el-option label="2" value="2"/>
            <el-option label="3" value="3"/>
            <el-option label="4" value="4"/>
            <el-option label="5" value="5"/>
          </el-select>
        </el-form-item>
        <el-form-item label="姓 名">
          <el-input v-model="editForm.name" placeholder="请输入客户姓名"></el-input>
        </el-form-item>
        <el-form-item label="电 话">
          <el-input v-model="editForm.phone" placeholder="请输入客户电话"></el-input>
        </el-form-item>
        <el-form-item label="地 址">
          <el-input v-model="editForm.address" placeholder="请输入客户地址"></el-input>
        </el-form-item>
        <el-form-item label="邮 编">
          <el-input v-model="editForm.zipcode" placeholder="请输入客户邮编"></el-input>
        </el-form-item>
        <el-form-item label="电子邮箱">
          <el-input v-model="editForm.email" placeholder="请输入客户电子邮箱"></el-input>
        </el-form-item>
        <el-form-item v-if="isManager" label="应收额度">
          <el-input v-if="isManager" v-model="editForm.lineOfCredit" placeholder="请输入客户应收额度"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleEdit(false)">取 消</el-button>
        <el-button type="primary" @click="handleEdit(true)">确 定</el-button>
      </div>
    </el-dialog>
  </Layout>
</template>

<script>
import Layout from "@/components/content/Layout";
import Title from "@/components/content/Title";
import {getAllCustomer, deleteCustomer, updateCustomer, getCustomerById, addCustomer} from "@/network/purchase";
import {deleteCommodity, updateCommodity} from "@/network/commodity";

export default {
  name: 'CustomerView',
  components: {
    Layout,
    Title
  },
  data() {
    return {
      customerList: [],
      isManager: false,
      operatorName: sessionStorage.getItem("name"),
      addDialogVisible: false,
      addForm: {
        type: '',
        level: '',
        name: '',
        phone: '',
        address: '',
        zipcode: '',
        email: '',
        lineOfCredit: 0,
        receivable: 0,
        payable: 0,
        operator: ''
      },
      editDialogVisible: false,
      editForm: {
        type: '',
        level: '',
        name: '',
        phone: '',
        address: '',
        zipcode: '',
        email: '',
        lineOfCredit: 0,
        receivable: 0,
        payable: 0,
        operator: ''
      },
    }
  },
  async mounted() {
    await getAllCustomer({params: {type: 'SUPPLIER'}}).then(_res => {
      this.customerList = this.customerList.concat(_res.result)
    }).catch(err => {
      this.$message({
        type: 'error',
        message: '获取供应商失败!' + err
      })
    })
    await getAllCustomer({params: {type: 'SELLER'}}).then(_res => {
      this.customerList = this.customerList.concat(_res.result)
    }).catch(err => {
      this.$message({
        type: 'error',
        message: '获取销售商失败!' + err
      })
    })
    await this.getPrivilege()
  },
  methods: {
    //判定该用户权限
    getPrivilege() {
      this.isManager = sessionStorage.getItem("role") != "SALE_STAFF";
    },
    //为了防止两个get异步
    async getAll() {
      await getAllCustomer({params: {type: 'SUPPLIER'}}).then(_res => {
        this.customerList = _res.result
      }).catch(err => {
        this.$message({
          type: 'error',
          message: '获取供应商失败!' + err
        })
      })
      await getAllCustomer({params: {type: 'SELLER'}}).then(_res => {
        this.customerList = this.customerList.concat(_res.result)
      }).catch(err => {
        this.$message({
          type: 'error',
          message: '获取销售商失败!' + err
        })
      })
    },
    filterTag(value, row) {
      return row.type === value
    }
    ,
    addCustomer() {
      this.addDialogVisible = true;
    }
    ,
    handleAdd(type) {
      if (type === false) {
        this.addDialogVisible = false;
        this.addForm = {};
      } else if (type === true) {
        this.addForm.operator = this.operatorName;
        addCustomer(this.addForm).then(_res => {
          if (_res.code === "B0001" || _res.code === "B0002") {
            this.$message({
              type: 'error',
              message: _res.msg
            });
          } else {
            this.$message({
              type: 'success',
              message: '新增成功!'
            });
            this.addForm = {};
            this.addDialogVisible = false;
            this.getAll();
          }
        })
      }
    }
    ,
    editInfo(id) {
      this.editForm = this.customerList.filter(x => x.id === id)[0];
      this.editDialogVisible = true;
    }
    ,
    handleEdit(type) {
      if (type === false) {
        this.editDialogVisible = false;
        this.editForm = {};
      } else if (type === true) {
        updateCustomer(this.editForm).then(_res => {
          if (_res.code === 'B0003') {
            this.$message({
              type: 'error',
              message: _res.msg
            })
          } else {
            this.$message({
              type: 'success',
              message: '修改成功！'
            })
            this.editForm = {};
            this.editDialogVisible = false;
            this.getAll();
          }
        })
      }
    }
    ,
    deleteCustomer(id) {
      let config = {
        params: {
          id: id
        }
      };
      this.$confirm('是否要删除该客户？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCustomer(config).then(_res => {
          if (_res.msg === 'Success') {
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            this.getAll();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    }
    ,
    close() {
      this.addForm = {};
      this.editForm = {};
    }
  }
}
</script>

<style>

</style>
