<template>
  <div class="order-container">
    <div class="order-header">
      我的购买记录({{ data.total }})
    </div>

    <div class="order-table">
      <el-table
        :data="data.tableData"
        stripe
        size="large"
        style="width: 100%"
        header-align="center"
      >
        <el-table-column label="序号" prop="orderId" align="center" width="100"/>
        <el-table-column label="课程名称" prop="courseName" align="center" width="300"/>
        <el-table-column label="教练姓名" prop="coachName" align="center" width="100"/>
        <el-table-column label="订单编号" prop="orderNo" align="center" width="200"/>
        <el-table-column label="课程价格" prop="price" align="center" width="100">
          <template #default="scope">
            ￥{{ scope.row.price }}
          </template>
        </el-table-column>
        <el-table-column label="下单时间" prop="orderTime" align="center"/>
        <el-table-column label="操作" width="120" align="center">
          <template #default="scope">
            <el-button
              @click="del(scope.row.userId)"
              :icon="Delete"
              circle
              type="danger"
            />
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="order-pagination">
      <el-pagination
        @size-change="load"
        @current-change="load"
        v-model:current-page="data.pageNum"
        v-model:page-size="data.pageSize"
        :page-sizes="[5, 10, 15, 20]"
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="data.total"
      />
    </div>
  </div>
</template>

<script setup>

import { onMounted, reactive } from 'vue'
import request from '@/utils/request.js'
import { useRoute } from 'vue-router'
import { Delete } from '@element-plus/icons-vue'

const route = useRoute()
const orderList =reactive({})
const data = reactive({
  tableData: [
  ],
  pageNum: 1,
  pageSize: 5,
  total: 0,
  username:null,
  formVisible: false,
  form:{},
  ids:[]
})

onMounted(() => {
  load()
})

const load = () => {
  const userId = route.params.id
  request.get('/order/selectPage',{
    params:{
      pageNum: data.pageNum,
      pageSize:data.pageSize,
      userId:userId
    }
  }).then(res => {
    data.tableData = res.data.list
    data.total = res.data.total
  })
}
</script>

<style>
.order-container {
  width: 90%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.order-header {
  text-align: center;
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
}

.order-table {
  margin: 20px 0;
}

.order-pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

/* 表格单元格内容居中 */
:deep(.el-table th > .cell),
:deep(.el-table td > .cell) {
  text-align: center;
}

/* 分页样式调整 */
:deep(.el-pagination) {
  padding: 10px 0;
}

/* 按钮样式调整 */
:deep(.el-button) {
  margin: 0 auto;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>