<template>
  <Layout>
    <Title title="公司账户管理"></Title>
    <el-button type="primary" size="medium" @click="addAccount">新增账户</el-button>
    <div style="margin-top: 10px">
      <el-table
          :data="accountList"
          stripe
          style="width: 100%"
          :header-cell-style="{'text-align':'center'}"
          :cell-style="{'text-align':'center'}">
        <el-table-column
            prop="id"
            label="id"
            width="150">
        </el-table-column>
        <el-table-column
            prop="name"
            label="账户名称"
            width="300">
        </el-table-column>
        <el-table-column
            prop="balance"
            label="余额"
            width="300">
        </el-table-column>
        <el-table-column
            label="操作">
          <template slot-scope="scope">
            <el-button
                @click.native.prevent="editInfo(scope.row.id)"
                type="text"
                size="middle">
              编辑
            </el-button>
            <el-button
                @click="deleteAccount(scope.row.id)"
                type="text"
                size="middle">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog
        title="新增账户"
        :visible.sync="addDialogVisible"
        width="30%"
        @close="close()">
      <el-form :model="addForm" :label-width="'100px'" size="mini">
        <el-form-item label="账户名称">
          <el-input v-model="addForm.name" placeholder="请输入账户名称"></el-input>
        </el-form-item>
        <el-form-item label="余额">
          <el-input v-model="addForm.balance" placeholder="请输入账户余额"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleAdd(false)">取 消</el-button>
        <el-button type="primary" @click="handleAdd(true)">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog
        title="修改账户信息"
        :visible.sync="editDialogVisible"
        width="30%"
        @close="close()">
      <el-form :model="editForm" :label-width="'100px'" size="mini">
        <el-form-item label="账户id">
          <el-input v-model="editForm.id" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="账户名称">
          <el-input v-model="editForm.name" placeholder="请输入账户名称"></el-input>
        </el-form-item>
        <el-form-item label="余额">
          <el-input v-model="editForm.balance" :disabled="true"></el-input>
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
import {
  companyAccountQueryAll,
  companyAccountUpdate,
  companyAccountDelete,
  companyAccountCreate
} from "@/network/finance";

export default {
  name: 'CompanyAccountView',
  components: {
    Layout,
    Title
  },
  data() {
    return {
      accountList: [],
      addDialogVisible: false,
      addForm: {
        name: '',
        balance: 0
      },
      editDialogVisible: false,
      editForm: {
        id: 0,
        name: '',
        balance: 0,
      },
    }
  },
  mounted() {
    companyAccountQueryAll({}).then(_res => {
      this.accountList = _res.result
    }).catch(err => {
      this.$message({
        type: 'error',
        message: '获取账户信息失败!' + err
      })
    })
  },
  methods: {
    //为了防止两个get异步
    getAll() {
      companyAccountQueryAll({}).then(_res => {
        this.accountList = _res.result
      }).catch(err => {
        this.$message({
          type: 'error',
          message: '获取账户信息失败!' + err
        })
      })
    }
    ,
    addAccount() {
      this.addDialogVisible = true;
    }
    ,
    handleAdd(type) {
      if (type === false) {
        this.addDialogVisible = false;
        this.addForm = {};
      } else if (type === true) {
        companyAccountCreate(this.addForm).then(_res => {
          if (_res.code === "300001" || _res.code === "300002" || _res.code === "300003") {
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
      this.editForm = this.accountList.filter(x => x.id === id)[0];
      this.editDialogVisible = true;
    }
    ,
    handleEdit(type) {
      if (type === false) {
        this.editDialogVisible = false;
        this.editForm = {};
      } else if (type === true) {
        companyAccountUpdate(this.editForm).then(_res => {
          if (_res.code === '000001') {
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
    deleteAccount(id) {
      let config = {
        params: {
          id: id
        }
      };
      this.$confirm('是否要删除该账户？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        companyAccountDelete(config).then(_res => {
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
