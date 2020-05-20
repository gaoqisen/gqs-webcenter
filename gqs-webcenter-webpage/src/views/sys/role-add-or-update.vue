<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible" width="890px">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <el-form-item label="角色名称" prop="roleName">
        <el-input v-model="dataForm.roleName" placeholder="角色名称"></el-input>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="dataForm.remark" placeholder="备注"></el-input>
      </el-form-item>
       <el-form-item label="系统" prop="remark">
         <el-radio-group v-model="currentStatus.applicationName" @change="refreshListFun">
          <el-radio v-for="(item, index) of sysCodeList" :key="index" :label="item.applicationName">{{item.name}}</el-radio>
         </el-radio-group>
      </el-form-item>
      <el-form-item label="菜单权限">
        <el-tree
        @check="handleCheckedMenuChange"
          :data="dataList[currentStatus.applicationName].menuList"
          :props="menuListTreeProps"
          node-key="menuId"
          ref="menuListTree"
          :default-expand-all="true"
          show-checkbox>
        </el-tree>
      </el-form-item>
      <el-form-item label="接口权限">
            <el-checkbox-group v-model="checkedRests" @change="handleCheckedRestChange">
              <el-checkbox style="width:230px;"  v-for="rest of dataList[currentStatus.applicationName].restList" :label="rest.digest" :key="rest.digest">
                <div v-if="rest.apiName != undefined">{{rest.apiName}}</div>
                <div v-else>{{rest.url}}</div>
              </el-checkbox>
            </el-checkbox-group>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button plain @click="checkAllOrCancel()">全选/取消</el-button>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  import { treeDataTranslate } from '@/utils'
  export default {
    data () {
      return {
        // 数据列表
        dataList: {
            "webcenter-sample": {
                "menuList": [],
                "restList": []
            }
        },
        // 选中状态
        checkedStatus: {"webcenter-sample": {
          "restCheck":[],
          "menuCheck":[]
        }},
        checkedRests: [],
        // 当前状态
        currentStatus: {
          sysRadio : "",
          applicationName: "webcenter-sample",
          isCheckAll: true
        },
        sysCodeList:[],
        visible: false,
        menuListTreeProps: {
          label: 'name',
          children: 'children'
        },
        dataForm: {
          id: 0,
          roleName: '',
          remark: ''
        },
        dataRule: {
          roleName: [
            { required: true, message: '角色名称不能为空', trigger: 'blur' }
          ]
        },
        tempKey: 0 // 临时key, 用于解决tree半选中状态项不能传给后台接口问题. # 待优化
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.getSysCodeFun()
      },
      // REST接口选择
      handleCheckedRestChange() {
        this.checkedStatus[this.currentStatus.applicationName].restCheck = this.checkedRests
      },
      // 全选或取消
      checkAllOrCancel () {
        //  全选
        if(this.currentStatus.isCheckAll) {
          var obj = this.dataList[this.currentStatus.applicationName]
          var menus = obj.menuList
          var menuChecks = []
          for(var menu in menus) {
            menuChecks.push(menus[menu].menuId)
          }
          this.checkedStatus[this.currentStatus.applicationName].menuCheck = menuChecks
          var rests = obj.restList
          var restChecks = []
          for(var rest in rests) {
            restChecks.push(rests[rest].digest)
          }
          this.checkedRests = restChecks
          this.checkedStatus[this.currentStatus.applicationName].restCheck = restChecks
          this.$refs.menuListTree.setCheckedKeys(this.checkedStatus[this.currentStatus.applicationName].menuCheck)
          this.currentStatus.isCheckAll = false
        }
        // 全选取消
        else {
          this.checkedStatus[this.currentStatus.applicationName].menuCheck = []
          this.checkedStatus[this.currentStatus.applicationName].restCheck = []
          this.checkedRests = []
          this.$refs.menuListTree.setCheckedKeys([])
          this.currentStatus.isCheckAll = true
        }
      },
      // 菜单选择
      handleCheckedMenuChange() {
        var currentSelected = this.$refs.menuListTree.getCheckedKeys()
        this.checkedStatus[this.currentStatus.applicationName].menuCheck = currentSelected
      },
      // 系统单选框选择
      refreshListFun () {
        this.$refs.menuListTree.setCheckedKeys(this.checkedStatus[this.currentStatus.applicationName].menuCheck)
      },
      // 获取所有系统信息
      getSysCodeFun () {
        this.$http({
          url: this.$http.adornUrl('/sys/code/queryAllInfo'),
          method: 'get'
        }).then(({data}) => {
          this.sysCodeList = data.sysCodeList
          this.radio = this.sysCodeList[0].applicationName
          this.currentStatus.applicationName = this.sysCodeList[0].applicationName
          var dataList = data.dataList
          for(var key in dataList) {
            dataList[key].menuList = treeDataTranslate(dataList[key].menuList, 'menuId')
          }
          this.dataList = dataList
          this.roleInfo()
        })
      },
      // 角色信息获取
      roleInfo () {
        for(var key in this.sysCodeList) {
          this.checkedStatus[this.sysCodeList[key].applicationName] = {"restCheck":[],"menuCheck":[]}
        }
        if (this.dataForm.id) {
          this.$http({
            url: this.$http.adornUrl(`/sys/role/info/${this.dataForm.id}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm.roleName = data.sysRole.roleName
              this.dataForm.remark = data.sysRole.remark
              this.checkedStatus = data.sysRole.roleLimitInfo
              this.checkedRests = this.checkedStatus[this.currentStatus.applicationName].restCheck
              this.$refs.menuListTree.setCheckedKeys(this.checkedStatus[this.currentStatus.applicationName].menuCheck)
              this.visible = true
            }
          })
        } 
        this.visible = true
      },
      // 表单提交
      dataFormSubmit () {
        // 提交
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/sys/role/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'roleId': this.dataForm.id || undefined,
                'roleName': this.dataForm.roleName,
                'remark': this.dataForm.remark,
                'roleLimitInfo': this.checkedStatus
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      }
    }
  }
</script>
<style scoped>
.el-checkbox+.el-checkbox {
  margin-left: 0px;
}
.el-checkbox {
  margin-right: 25px;
}
</style>