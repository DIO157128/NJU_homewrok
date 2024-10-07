<template>
  <Layout>
    <Title title="顾客等级促销策略" ></Title>
    <el-button type="primary" size="medium" @click="addPromotionStrategy">新增促销策略</el-button>
    <div style="margin-top:10px">
      <el-table
        :data="StrategyList"
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
          prop="level"
          label="会员等级"
          width="100">
        </el-table-column>
        <el-table-column
          prop="discount"
          label="折扣"
          width="100">
        </el-table-column>
        <el-table-column
          prop="voucher"
          label="代金券"
          width="100">
        </el-table-column>
        <el-table-column
          prop="start_time"
          label="开始日期"
          width="200">
        </el-table-column>
        <el-table-column
          prop="end_time"
          label="结束日期"
          width="200">
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
              @click="deleteStrategy(scope.row.id)"
              type="text"
              size="small">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog
      title="新增促销策略"
      :visible.sync="addDialogVisible"
      width="30%"
      @close="close()">
      <el-form :model="addForm" :label-width="'100px'" size="mini">
        <el-form-item label="级 别">

          <el-select v-model="addForm.level" type="number">
            <el-option key=1 label="1" value=1></el-option>
            <el-option key=2 label="2" value=2></el-option>
            <el-option key=3 label="3" value=3></el-option>
            <el-option key=4 label="4" value=4></el-option>
            <el-option key=5 label="5" value=5></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="折扣">
          <el-input v-model="addForm.discount" placeholder="请输入折扣"></el-input>
        </el-form-item>

        <el-form-item label="代金券(元)">
          <el-input v-model="addForm.voucher" placeholder="请输入代金券金额"></el-input>
        </el-form-item>

        <el-form-item label="开始日期" prop="start_time" :rules="[{type:'string',message:'请选择开始时间',trigger:'change'}]">
          <el-date-picker v-model="addForm.start_time"
                          type="datetime"
                          placeholder="选择日期"
                          value-format="yyyy-MM-dd HH:mm:ss"
          ></el-date-picker>
        </el-form-item>

        <el-form-item label="结束日期" prop="end_time" :rules="[{type:'string',message:'请选择结束时间',trigger:'change'}]">
          <el-date-picker v-model="addForm.end_time"
                          type="datetime"
                          placeholder="选择日期"
                          value-format="yyyy-MM-dd HH:mm:ss"
          ></el-date-picker>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleAdd(false)">取消</el-button>
        <el-button type="primary" @click="handleAdd(true)">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="修改促销策略"
      :visible.sync="editDialogVisible"
      width="30%"
      @close="close()">
      <el-form :model="editForm" :label-width="'100px'" size="mini">
        <el-form-item label="级 别">
          <el-select v-model="editForm.level">
            <el-option label="1" value=1></el-option>
            <el-option label="2" value=2></el-option>
            <el-option label="3" value=3></el-option>
            <el-option label="4" value=4></el-option>
            <el-option label="5" value=5></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="折扣">
          <el-input v-model="editForm.discount" placeholder="请输入折扣"></el-input>
        </el-form-item>
        <el-form-item label="代金券(元)">
          <el-input v-model="editForm.voucher" placeholder="请输入代金券金额"></el-input>
        </el-form-item>
        <el-form-item label="开始日期" prop="start_time" :rules="[{type:'string',message:'请选择开始时间',trigger:'change'}]">
          <el-date-picker v-model="editForm.start_time"
                          type="datetime"
                          placeholder="选择日期"
                          value-format="yyyy-MM-dd HH:mm:ss"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="结束日期" prop="end_time" :rules="[{type:'string',message:'请选择结束时间',trigger:'change'}]">
          <el-date-picker v-model="editForm.end_time"
                          type="datetime"
                          placeholder="选择日期"
                          value-format="yyyy-MM-dd HH:mm:ss"
          ></el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleEdit(false)">取消</el-button>
        <el-button type="primary" @click="handleEdit(true)">确定</el-button>
      </div>
    </el-dialog>
  </Layout>

</template>
<script>
import Layout from "@/components/content/Layout";
import Title from "@/components/content/Title";
import {
  promotionStrategyOnLevelCreate,
  promotionStrategyOnLevelDelete, promotionStrategyOnLevelGetAll,
  promotionStrategyOnLevelUpdate
} from "@/network/promotionStrategy";
export default {
  name: 'PromotionStrategyOnLevelView',
  components: {
    Layout,
    Title
  },

  data(){
    return{
      StrategyList:[],
      addDialogVisible:false,
      addForm:{
        level:1,
        discount:1,
        voucher:0,
        start_time:'',
        end_time:'',
        create_time:''
      },
      editDialogVisible:false,
      editForm:{
        id:0,
        level:0,
        discount:1,
        voucher:0,
        start_time:'',
        end_time:'',
        create_time:''
      },
    }
  },

  mounted() {
    promotionStrategyOnLevelGetAll({}).then(_res =>{
      console.log(_res.result)
      this.StrategyList=_res.result
    }).catch(err=>{
      this.$message({
        type:'error',
        message:'获取促销策略失败'+err
      })
    })
  },

  methods:{
    //为了防止两个get异步
    getAll(){
      promotionStrategyOnLevelGetAll({}).then(_res =>{
        this.StrategyList=_res.result
      }).catch(err=>{
        this.$message({
          type:'error',
          message:'获取促销策略失败'+err
        })
      })
    },

    addPromotionStrategy(){
      this.addDialogVisible=true;
    },

    handleAdd(type) {
      if (type === false) {
        this.addDialogVisible = false;
        this.addForm = {};
      } else if (type === true) {
        promotionStrategyOnLevelCreate(this.addForm).then(_res => {
          if (_res.code === "000000" || _res.code === "000001" || _res.code==="000002" ||  _res.code==="000003" || _res.code==="000004" || _res.code==="000005") {
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
    },

    editInfo(id) {
      this.editForm = this.StrategyList.filter(x => x.id === id)[0];
      this.editDialogVisible = true;
    },

    handleEdit(type) {
      if (type === false) {
        this.editDialogVisible = false;
        this.editForm = {};
      } else if (type === true) {
        promotionStrategyOnLevelUpdate(this.editForm).then(_res => {
          if (_res.code === "000000" || _res.code === "000001" || _res.code==="000002" ||  _res.code==="000003" || _res.code==="000004" || _res.code==="000005") {
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
    },

    deleteStrategy(id) {
      let config = {
        params: {
          id:id
        }
      };
      this.$confirm('是否要删除该促销策略？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        promotionStrategyOnLevelDelete(config).then(_res => {
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
    },

    close(){
      this.addForm={};
      this.editForm={};
    }

  }
}
</script>