<template>
  <div class="mod-menu">
    <el-form :inline="true">
      <!-- <el-form-item>
        <el-input v-model="dataForm.name" placeholder="系统名称" clearable></el-input>
      </el-form-item> -->
      <el-form-item>
        <!-- <el-button @click="getDataList()">查询</el-button> -->
        <el-button v-if="isAuth('sys:code:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      row-key="menuId"
      border
      style="width: 100%; ">
      <el-table-column
        prop="name"
        header-align="center"
        min-width="100"
        label="名称" >
      </el-table-column>
      <el-table-column
        prop="applicationName"
        header-align="center"
        min-width="150"
        label="应用名" >
      </el-table-column>
      <el-table-column
        prop="createTime"
        header-align="center"
        min-width="160"
        align="center"
        label="创建时间">
      </el-table-column>
      <el-table-column
        prop="clientId"
        header-align="center"
        align="center"
        width="150"
        :show-overflow-tooltip="true"
        label="客户端ID">
      </el-table-column>
      <el-table-column
        prop="secretKey"
        header-align="center"
        align="center"
        width="150"
        :show-overflow-tooltip="true"
        label="密钥">
      </el-table-column>
      <el-table-column
        prop="remark"
        header-align="center"
        min-width="200"
        label="备注" >
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button v-if="isAuth('sys:code:update')" type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
          <el-button v-if="isAuth('sys:code:delete')" type="text" size="small" @click="deleteHandle(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
  </div>
</template>

<script>
  import AddOrUpdate from './code-add-or-update'
  import { treeDataTranslate } from '@/utils'
  export default {
    data () {
      return {
        dataList: [],
        dataListLoading: false,
        addOrUpdateVisible: false
      }
    },
    components: {
      AddOrUpdate
    },
    activated () {
      this.getDataList()
    },
    methods: {
      refreshList (data) {
        this.value = data.id
        this.applicationName = data.applicationName
        this.getDataList()
      },
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/sys/code/queryAll'),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          this.dataList = data.data
          this.dataListLoading = false
        })
      },
      // 新增 / 修改
      addOrUpdateHandle (id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id)
        })
      },
      // 删除
      deleteHandle (id) {
        this.$confirm(`确定对[id=${id}]进行[删除]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl(`/sys/code/delete/${id}`),
            method: 'get',
            data: this.$http.adornData()
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.getDataList()
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        }).catch(() => {})
      }
    }
  }
</script>
