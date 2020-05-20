<template>
  <div class="mod-menu">
    <el-form :inline="true">
      <template>
        <el-select v-model="value" placeholder="请选择">
          <el-option
            v-for="item in sysCodeList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
            @click.native="refreshList(item)">
          </el-option>
        </el-select>
      </template>
    </el-form>
    <el-table
      :data="dataList"
      row-key="menuId"
      border
      style="width: 100%; ">
      <el-table-column
        prop="apiName"
        header-align="center"
        min-width="150"
        label="名称" >
      </el-table-column>
      <el-table-column
        prop="applicationName"
        header-align="center"
        min-width="150"
        label="应用名" >
      </el-table-column>
      <el-table-column
        prop="url"
        header-align="center"
        min-width="200"
        label="URL" >
      </el-table-column>
      <el-table-column
        prop="createTime"
        header-align="center"
        min-width="150"
        align="center"
        label="创建时间">
      </el-table-column>
      <el-table-column
        prop="permissions"
        header-align="center"
        align="center"
        width="150"
        :show-overflow-tooltip="true"
        label="权限">
         <template slot-scope="scope">
           <el-dropdown>
            <span class="el-dropdown-link">
              <el-tag v-if="scope.row.permissions === 'anon'" size="small" type="success">公开</el-tag>
              <el-tag v-else-if="scope.row.permissions === 'authc'" size="small">登录</el-tag>
              <el-tag v-else-if="scope.row.permissions.startsWith('per')" size="small" type="warning">权限</el-tag><i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="permissions(scope.row,1)">公开</el-dropdown-item>
              <el-dropdown-item @click.native="permissions(scope.row,2)">登录</el-dropdown-item>
              <el-dropdown-item @click.native="permissions(scope.row,3)">权限</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="current"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="size"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
  </div>
</template>

<script>
  import AddOrUpdate from './code-add-or-update'
  import { treeDataTranslate } from '@/utils'
  export default {
    data () {
      return {
        applicationName: '',
        value: '',
        dataList: [],
        sysCodeList: [],
        dataListLoading: false,
        addOrUpdateVisible: false,
        current: 1,
        size: 10,
        totalPage: 0,
      }
    },
    components: {
      AddOrUpdate
    },
    activated () {
      this.getSysCode()
    },
    methods: {
      refreshList (data) {
        this.value = data.id
        this.applicationName = data.applicationName
        this.getDataList()
      },
      getSysCode () {
        this.$http({
          url: this.$http.adornUrl('/sys/code/queryAll'),
          method: 'get'
        }).then(({data}) => {
          this.sysCodeList = data.data
          this.value = this.sysCodeList[0].id
          this.applicationName = this.sysCodeList[0].applicationName
          this.getDataList()
        })
      },
      permissions (data, state) {
        var permissions = 'anon'
        var digest = data.digest
        if(state === 2) {
          permissions = 'authc'
        }

        if(state === 3) {
          permissions = 'perms[' + digest + ']'
        }

        this.$http({
              url: this.$http.adornUrl(`/sys/rest/update`),
              method: 'post',
              data: this.$http.adornData({
                'digest': digest,
                'permissions': permissions
              })
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
      },
      refreshList (data) {
        this.value = data.id
        this.applicationName = data.applicationName
        this.getDataList()
      },
      // 每页数
      sizeChangeHandle (val) {
        this.size = val
        this.current = 1
        this.getDataList()
      },
      // 当前页
      currentChangeHandle (val) {
        this.current = val
        this.getDataList()
      },
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/sys/rest/list'),
          method: 'post',
          data: this.$http.adornData({
            'size': this.size,
            'current': this.current,
            'params': this.applicationName
          })
        }).then(({data}) => {
          this.dataList = data.restList.records
          this.totalPage = data.restList.total
          this.dataListLoading = false
        })
      }
    }
  }
</script>
