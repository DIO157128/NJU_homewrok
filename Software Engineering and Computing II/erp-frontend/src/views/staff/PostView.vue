<template>
  <Layout>
    <Title title="岗位信息"></Title>
    <el-button type="primary" size="medium" @click="addPost">新增岗位</el-button>
    <div>

      <div class="mt15">
        <span><strong>岗位信息为: </strong></span>
        <el-table
            :data="postContent"
            stripe
            style="width: 100%"
            :header-cell-style="{'text-align':'center'}"
            :cell-style="{'text-align':'center'}"
            class="mt15">
<!--          <el-table-column-->
<!--              prop="postName"-->
<!--              label="岗位名称"-->
<!--              width="100">-->
<!--          </el-table-column>-->

          <el-table-column prop="postName" label="岗位名称">
            <template slot-scope="scope">
              <span v-if="scope.row.postName==='INVENTORY_MANAGER'">库存管理</span>
              <span v-if="scope.row.postName==='SALE_MANAGER'">销售经理</span>
              <span v-if="scope.row.postName==='SALE_STAFF'">销售人员</span>
              <span v-if="scope.row.postName==='GM'">总经理</span>
              <span v-if="scope.row.postName==='HR'">人力资源人员</span>
            </template>
          </el-table-column>

          <el-table-column
              prop="basicSalary"
              label="基本工资"
              width="150">
          </el-table-column>
          <el-table-column
              prop="postSalary"
              label="岗位工资"
              width="150">
          </el-table-column>
          <el-table-column
              prop="level"
              label="等级"
              width="100">
          </el-table-column>
<!--          <el-table-column-->
<!--              prop="calSalaryMethod"-->
<!--              label="薪资计算方式"-->
<!--              width="150">-->
<!--          </el-table-column>-->
          <el-table-column prop="calSalaryMethod" label="薪资计算方式">
            <template slot-scope="scope">
              <span v-if="scope.row.calSalaryMethod==='YEARLY_SALARY'">年薪</span>
              <span v-if="scope.row.calSalaryMethod==='MONTHLY_SALARY'">月薪</span>
              <span v-if="scope.row.calSalaryMethod==='PERCENTAGE'">提成制</span>
            </template>
          </el-table-column>
          <el-table-column
              prop="paySalaryMethod"
              label="薪资发放方式"
              width="150">
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
                  @click="deletePost(scope.row.id)"
                  type="text"
                  size="middle">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <el-dialog
          title="新增岗位"
          :visible.sync="addDialogVisible"
          width="30%"
          @close="close()">
        <el-form :model="addForm" :label-width="'100px'" size="mini">
          <el-form-item label="岗位名称">
            <el-radio-group v-model="addForm.postName">
              <el-radio label="INVENTORY_MANAGER">库存管理</el-radio>
              <el-radio label="SALE_MANAGER">销售经理</el-radio>
              <el-radio label="SALE_STAFF">销售人员</el-radio>
              <el-radio label="GM">总经理</el-radio>
              <el-radio label="HR">人力资源人员</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="基本工资">
            <el-input v-model="addForm.basicSalary" placeholder="请输入基本工资"></el-input>
          </el-form-item>
          <el-form-item label="岗位工资">
            <el-input v-model="addForm.postSalary" placeholder="请输入岗位工资"></el-input>
          </el-form-item>
          <el-form-item label="岗位等级">
            <el-radio-group v-model="addForm.level">
              <el-radio label="1">总经理级</el-radio>
              <el-radio label="2">经理级</el-radio>
              <el-radio label="3">普通员工级</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="薪资计算方式">
            <el-radio-group v-model="addForm.calSalaryMethod">
              <el-radio label="YEARLY_SALARY">年薪</el-radio>
              <el-radio label="MONTHLY_SALARY">月薪</el-radio>
              <el-radio label="PERCENTAGE">提成制</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="薪资发放方式">
            <el-radio-group v-model="addForm.paySalaryMethod">
              <el-radio label="CARD">账号卡发放</el-radio>
              <el-radio label="CASH">现金发放</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="税收信息">
            <el-input v-model="addForm.taxDeductionRemark"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="handleAdd(false)">取 消</el-button>
          <el-button type="primary" @click="handleAdd(true)">确 定</el-button>
        </div>
      </el-dialog>

      <el-dialog
          title="修改岗位信息"
          :visible.sync="editDialogVisible"
          width="30%"
          @close="close()">
        <el-form :model="editForm" :label-width="'100px'" size="mini">

          <el-form-item label="岗位名称" >
            <el-input v-model="editForm.postName" placeholder="请输入岗位名称" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="基本工资">
            <el-input v-model="editForm.basicSalary" placeholder="请输入基本工资"></el-input>
          </el-form-item>
          <el-form-item label="岗位工资">
            <el-input v-model="editForm.postSalary" placeholder="请输入岗位工资"></el-input>
          </el-form-item>
          <el-form-item label="岗位等级">
            <el-input v-model="editForm.level" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="薪资计算方式">
            <el-input v-model="editForm.calSalaryMethod" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="薪资发放方式">
            <el-input v-model="editForm.paySalaryMethod" :disabled="true"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="handleEdit(false)">取 消</el-button>
          <el-button type="primary" @click="handleEdit(true)">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </Layout>
</template>

<script>
import Layout from "@/components/content/Layout";
import Title from "@/components/content/Title";
import {getPostInfo, PostDelete,PostUpdate,PostCreate} from "@/network/staff";


export default {
  components: {
    Layout,
    Title
  },
  data() {
    return {
      date: '',
      postContent: [],
      addDialogVisible: false,
      addForm: {
        postName: "",
        basicSalary: "",
        postSalary: "",
        level: "",
        calSalaryMethod: "",
        paySalaryMethod: "",
        taxDeductionRemark:"无"
      },
      editDialogVisible: false,
      editForm: {
        id: "",
        postName: "",
        basicSalary: "",
        postSalary: "",
        level: "",
        calSalaryMethod: "",
        paySalaryMethod: "",
      },
    }
  },
  computed: {},
  mounted() {
    this.getData();
  },
  methods: {
    getData() {
      const config = {}
      getPostInfo(config).then(_res => {
        this.postContent = _res.result
        if (this.postContent === []) {
          this.$message.error('无岗位信息记录!')
        }
      })
    },
    editInfo(id) {
      this.editForm = this.postContent.filter(x => x.id === id)[0];
      this.editDialogVisible = true;
    },
    checkInfo(type){
      if(type==="edit"){
        //检查edit中数据
        if(this.editForm.basicSalary<0) return false;
        if(this.editForm.postSalary<0) return false;
        return true;
      }
      if(type==="add"){
        //检查添加的数据
        if(this.addForm.basicSalary<0) return false;
        if(this.addForm.postSalary<0) return false;
        return true;
      }
    },
    handleAdd(type) {
      if(this.checkInfo("add")===false){
        this.$message({
          type: 'error',
          message: "格式不符合要求"
        })
      }
      if (type === false) {
        this.addDialogVisible = false;
        this.addForm = {};
      } else if (type === true) {
        PostCreate(this.addForm).then(_res => {
          if (_res.code === "C0001" || _res.code === "C0002" ||_res.code === "C0003"||_res.code === "C0004") {
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
            this.getData();
          }
        })
      }
    },
    handleEdit(type) {
      if (type === false) {
        this.editDialogVisible = false;
        this.editForm = {};
      } else if (type === true) {
        PostUpdate(this.editForm).then(_res => {
          if (_res.code === "C0001" || _res.code === "C0002" ||_res.code === "C0003"||_res.code === "C0004") {
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
            this.getData();
          }
        })
      }
    }
    ,
    deletePost(id) {
      let config = {
        params: {
          id: id
        }
      };
      this.$confirm('是否要删除该岗位？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        PostDelete(config).then(_res => {
          if (_res.msg === 'Success') {
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            this.getAll();
          } else {
            this.$message.error('存在关联该岗位的员工，请先删除该员工信息!')
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
      this.
      this.getData()                //刷新一下
    },
    addPost() {
      this.addDialogVisible = true;
    },
    close() {
      this.addForm = {};
      this.editForm = {};
    }
  }
};
</script>

<style scoped lang="scss">
.mt15 {
  margin-top: 15px;
}
</style>
