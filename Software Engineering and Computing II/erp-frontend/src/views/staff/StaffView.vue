<template>
  <Layout>
    <Title title="员工信息"></Title>
    <el-button type="primary" size="medium" @click="addStaff">新增员工</el-button>
    <div>

      <div class="mt15">
        <span><strong>员工信息为: </strong></span>
        <el-table
            :data="staffContent"
            stripe
            style="width: 100%"
            :header-cell-style="{'text-align':'center'}"
            :cell-style="{'text-align':'center'}"
            class="mt15">
          <el-table-column
              prop="id"
              label="员工ID"
              width="80">
          </el-table-column>

          <el-table-column
              prop="name"
              label="员工姓名"
              width="100">
          </el-table-column>

          <el-table-column prop="gender" label="性别">
            <template slot-scope="scope">
              <span v-if="scope.row.gender===1">男</span>
              <span v-if="scope.row.gender===0">女</span>
            </template>
          </el-table-column>

          <el-table-column
              prop="birthday"
              label="生日"
              width="150">
          </el-table-column>
          <el-table-column
              prop="phoneNum"
              label="电话号码"
              width="130">
          </el-table-column>
          <el-table-column
              prop="postId"
              label="岗位ID"
              width="80">
          </el-table-column>
          <el-table-column
              prop="cardAccountId"
              label="卡账号ID"
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
                  @click="deleteStaff(scope.row.id)"
                  type="text"
                  size="middle">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <el-dialog
          title="新增员工"
          :visible.sync="addDialogVisible"
          width="50%"
          @close="close()">
        <el-form :model="addForm" :label-width="'100px'" size="mini">
          <el-form-item label="姓名">
            <el-input v-model="addForm.name" placeholder="请输入员工姓名"></el-input>
          </el-form-item>
          <el-form-item label="性别">
            <el-radio-group v-model="addForm.gender">
              <el-radio label="1">男</el-radio>
              <el-radio label="0">女</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="出生年月">
            <el-col :span="11">
              <el-date-picker value-format="yyyy-MM-dd HH:mm:ss" type="datetime" placeholder="选择日期" v-model="addForm.birthday" style="width: 100%;"></el-date-picker>
            </el-col>
          </el-form-item>

          <el-form-item label="电话号码">
            <el-input v-model="addForm.phoneNum" placeholder="请输入员工电话号码"></el-input>
          </el-form-item>

          <el-form-item label="岗位ID">
            <el-input v-model="addForm.postId" placeholder="请输入员工岗位ID"></el-input>
          </el-form-item>

          <el-form-item label="卡账号ID">
            <el-input v-model="addForm.cardAccountId" placeholder="请输入员工卡账号ID"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="handleAdd(false)">取 消</el-button>
          <el-button type="primary" @click="handleAdd(true)">确 定</el-button>
        </div>
      </el-dialog>
      <el-dialog
          title="修改员工信息"
          :visible.sync="editDialogVisible"
          width="50%"
          @close="close()">
        <el-form :model="editForm" :label-width="'100px'" size="mini">
          <el-form-item label="姓名">
            <el-input v-model="editForm.name" placeholder="请输入员工姓名"></el-input>
          </el-form-item>
          <el-form-item label="性别">
            <el-input v-model="editForm.gender" placeholder="请输入员工性别" :disabled="true"></el-input>
          </el-form-item>

          <el-form-item label="电话号码">
            <el-input v-model="editForm.phoneNum" placeholder="请输入员工电话号码"></el-input>
          </el-form-item>

<!--          <el-form-item label="出生年月">-->
<!--            <el-col :span="11">-->
<!--              <el-date-picker value-format="yyyy-MM-dd HH:mm:ss" type="datetime" placeholder="选择日期" v-model="editbir" style="width: 100%;"></el-date-picker>-->
<!--            </el-col>-->
<!--          </el-form-item>-->

          <el-form-item label="出生年月">
            <el-input v-model="editForm.birthday" placeholder="请输入员工出生年月" :disabled="true"></el-input>
          </el-form-item>

          <el-form-item label="岗位ID">
            <el-input v-model="editForm.postId" placeholder="请输入员工岗位ID"></el-input>
          </el-form-item>

          <el-form-item label="卡账号ID">
            <el-input v-model="editForm.cardAccountId" placeholder="请输入员工卡账号ID"></el-input>
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
import {getStaffInfo,StaffCreate,StaffDelete,StaffUpdate} from "@/network/staff";
import {formatDate} from "@/common/utils";

export default {
  components: {
    Layout,
    Title
  },
  data() {
    return {
      date: '',
      addbir:"",
      staffContent: [],
      addDialogVisible: false,
      addForm: {
        name:"",
        gender:"",
        birthday:this.addbir === '' ? '' : formatDate(Date(this.addbir)),
        // birthday:"",
        phoneNum:"",
        postId:"",
        cardAccountId:""
      },
      editDialogVisible: false,
      editForm: {
        name:"",
        gender:"",
        birthday:"",
        phoneNum:"",
        postId:"",
        cardAccountId:""
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
      getStaffInfo(config).then(_res => {
        this.staffContent = _res.result
        if (this.staffContent === []) {
          this.$message.error('无员工信息记录!')
        }
      })
    },
    editInfo(id) {
      this.editForm = this.staffContent.filter(x => x.id === id)[0];
      this.editDialogVisible = true;
    },

    handleAdd(type) {
      if(this.checkInfo("add")===false){
        this.$message({
          type: 'error',
          message: "数据有误",
        });
        return;
      }
      if (type === false) {
        this.addDialogVisible = false;
        this.addForm = {};
      } else if (type === true) {
        console.log(this.addForm);
        StaffCreate(this.addForm).then(_res => {
          if (_res.code === "B0001" || _res.code === "B0002"|| _res.code === "B0003") {
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
    checkInfo(type){
      if(type==="edit"){
        //检查edit中数据
        if(this.editForm.birthday==="") return false;
        if(this.editForm.name==="") return false;
        if(this.editForm.postId==="") return false;
        if(isNaN(Number(this.editForm.phoneNum,10))) return false;
        return true;
      }
      if(type==="add"){
        //检查添加的数据
        if(this.addForm.birthday==="") return false;
        if(this.addForm.name==="") return false;
        if(this.addForm.postId==="") return false;
        if(isNaN(Number(this.addForm.phoneNum,10))) return false;
        return true;
      }
    },
    handleEdit(type) {
      if(this.checkInfo("edit")===false){
        this.$message({
          type: 'error',
          message: "数据有误",
        });
        return;
      }
      if (type === false) {
        this.editDialogVisible = false;
        this.editForm = {};
      } else if (type === true) {
        StaffUpdate(this.editForm).then(_res => {
          if (_res.code === 'B0001'||_res.code ==="B0002"||_res.code==="B0003"|| _res.code==="B0004") {
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
    deleteStaff(id) {
      let config = {
        params: {
          id: id
        }
      };
      this.$confirm('是否要删除该员工？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        StaffDelete(config).then(_res => {
          if (_res.msg === 'Success') {
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            this.getData();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
      this.getData()                //刷新一下
    },
    addStaff() {
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
