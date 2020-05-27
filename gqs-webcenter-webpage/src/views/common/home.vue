<template>
  <div class="mod-home">
    <el-row :gutter="100">
      <el-col :span="8">
        <el-card shadow="hover">
          <i class="el-icon-user-solid el-warp">{{sysNumbers.user}}</i>
          用户总数
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <i class="el-icon-s-home el-warp">{{sysNumbers.sys}}</i>
          系统总数
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <i class="el-icon-s-custom el-warp">{{sysNumbers.role}}</i>
          角色总数
        </el-card>
      </el-col>
    </el-row>
    <div class="block">
      <el-radio-group v-model="dateFormat" @change="changeCurrentDateFormat">
        <el-radio :label="1">日</el-radio>
        <el-radio :label="2">月</el-radio>
        <el-radio :label="3">年</el-radio>
      </el-radio-group>
      <el-date-picker
        style="float:right"
        v-model="selectDates"
        type="datetimerange"
        align="right"
        unlink-panels
        value-format="yyyy-MM-dd HH:mm:ss"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        :picker-options="pickerOptions"
        @change="dateHandleChange">
      </el-date-picker>
    </div>
    <ve-line :data="chartData"></ve-line>
  </div>
</template>
<script>
import Vue from 'vue'
import VeLine from 'v-charts/lib/line.common'
  export default {
    components: {
      VeLine
    },
    created () {
      this.getUserLoginNumber()
      this.getSysNumber()
    },
    methods: {
      getSysNumber() {
        this.$http({
            url: this.$http.adornUrl(`/sys/statistical/number`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.sysNumbers = data.data
            } else {
              this.$message.error(data.msg)
            }
          })
      },
      changeCurrentDateFormat() {
        this.getUserLoginNumber()
      },
      dateHandleChange() {
        this.getUserLoginNumber()
      },
      getUserLoginNumber() {
        this.$http({
          url: this.$http.adornUrl('/sys/statistical/login'),
          method: 'post',
          data: this.$http.adornData({
            'dateFormat': this.dateFormat,
            'startTime': this.selectDates[0],
            'endTime': this.selectDates[1]
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            var rows = []
            for(var i = 0; i < data.data.length; i++){
              var obj = data.data[i]
              rows.push({"访问用户": obj['num'], "日期": obj['timestamp']})
            }
            this.chartData.rows = rows
          } else {
            this.$message.error(data.msg)
          }
        })
      }
    },
    data () {
      return {
        sysNumbers: {"user": 0, "sys": 0, "role": 0},
        dateFormat: 1,
        pickerOptions: {
          shortcuts: [{
            text: '最近一周',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit('pick', [start, end]);
            }
          }]
        },
        selectDates: [new Date(2020, 1), new Date()],
        chartData: {
          columns: ['日期', '访问用户'],
          rows: [
          ]
        }
      }
    }
  }
</script>

<style>
  .mod-home {
    line-height: 1.5;
  }
  .el-warp{
    font-size: 20px;
  }
  .block{
    margin: 30px 0 30px 0;
  }
</style>

