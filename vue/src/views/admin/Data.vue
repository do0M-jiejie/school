<template>
  <div>
    <div>
      <el-card style="margin-bottom: 10px">
        <el-input style="width: 240px" v-model="data.name" placeholder="请输入……" prefix-icon="Search"></el-input>
        <el-button type="primary" style="margin-left: 10px">查询</el-button>
        <el-button type="warning" style="margin-left: 10px">重置</el-button>
      </el-card>
    </div>

    <div class="card" style="margin-bottom: 5px">
        <el-button type="primary" style="margin-left: 10px">新增</el-button>
        <el-button type="primary" style="margin-left: 10px">批量删除</el-button>
        <el-button type="primary" style="margin-left: 10px">导入</el-button>
        <el-button type="primary" style="margin-left: 10px">导出</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" stripe>
        <el-table-column label="日期" prop="date"/>
        <el-table-column label="姓名" prop="name"/>
        <el-table-column label="地址" prop="address"/>
      </el-table>
      <div style="margin-top: 15px">
        <el-pagination
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          :page-sizes="[5, 10, 15, 20]"
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="data.total"
        />
      </div>
    </div>

  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { Search } from '@element-plus/icons-vue'

const data = reactive({
  name:null,
  tableData:[
    {id: 1, date:'2025-4-12', name:'hwj',address:'成都'},
    {id: 2, date:'2025-4-12', name:'lyj',address:'成都'},
    {id: 3, date:'2025-4-12', name:'tp',address:'成都'},
  ],
  pageNum:1,
  pageSize:10,
  total:47

})

</script>