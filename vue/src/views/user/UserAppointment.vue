<template>
  <div class="appointment-container">
    <div class="appointment-header">
      我的预约记录
    </div>

    <div class="appointment-table">
      <el-table
        :data="appointmentList"
        stripe
        size="large"
        style="width: 100%"
        header-align="center"
      >
        <el-table-column label="序号" type="index" align="center" width="80"/>
        <el-table-column label="预约类型" prop="type" align="center" width="120"></el-table-column>
        <el-table-column label="预约日期" prop="appointmentDate" align="center" width="120"/>
        <el-table-column label="开始时间" prop="startTime" align="center" width="120"/>
        <el-table-column label="结束时间" prop="endTime" align="center" width="120"/>
        <el-table-column label="状态" prop="status" align="center" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="备注" prop="notes" align="center"/>
        <el-table-column label="操作" width="120" align="center">
          <template #default="scope">
            <el-button
              v-if="scope.row.status === '预约中'"
              @click="handleCancel(scope.row)"
              type="danger"
              size="small"
            >
              取消预约
            </el-button>
            <el-button
              v-else-if="scope.row.status === '已取消'"
              @click="handleDelete(scope.row)"
              type="info"
              size="small"
            >
              删除记录
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="appointment-pagination">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :page-sizes="[5, 10, 15, 20]"
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import request from '@/utils/request.js'
import { ElMessage, ElMessageBox } from 'element-plus'

const appointmentList = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const user = ref(JSON.parse(localStorage.getItem('userInfo')))

// 获取预约列表
const loadAppointments = () => {
  if (!user.value || !user.value.userId) {
    ElMessage.error('请先登录')
    return
  }

  request.get('/appointment/userAppointments', {
    params: {
      userId: user.value.userId,
      pageNum: pageNum.value,
      pageSize: pageSize.value
    }
  }).then(res => {
    if (res.code === '200') {
      appointmentList.value = res.data
      total.value = res.data.total
    } else {
      ElMessage.error(res.msg || '获取预约列表失败')
    }
  }).catch(error => {
    console.error('获取预约列表失败:', error)
    ElMessage.error('获取预约列表失败')
  })
}

// 处理分页大小变化
const handleSizeChange = (val) => {
  pageSize.value = val
  loadAppointments()
}

// 处理页码变化
const handleCurrentChange = (val) => {
  pageNum.value = val
  loadAppointments()
}

// 获取状态标签类型
const getStatusType = (status) => {
  switch (status) {
    case '预约中':
      return 'warning'
    case '已完成':
      return 'success'
    case '已取消':
      return 'info'
    default:
      return ''
  }
}

// 取消预约
const handleCancel = (row) => {
  ElMessageBox.confirm(
    '确定要取消该预约吗？',
    '取消预约',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    request.put('/appointment/cancel', {
      appointmentId: row.appointmentId
    }).then(res => {
      if (res.code === '200') {
        ElMessage.success('取消预约成功')
        loadAppointments()
      } else {
        ElMessage.error(res.msg || '取消预约失败')
      }
    }).catch(error => {
      console.error('取消预约失败:', error)
      ElMessage.error('取消预约失败')
    })
  })
}

// 删除预约记录
const handleDelete = (row) => {
  ElMessageBox.confirm(
    '确定要删除该预约记录吗？删除后无法恢复！',
    '删除记录',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    request.delete(`/appointment/delete/${row.appointmentId}`).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        loadAppointments()
      } else {
        ElMessage.error(res.msg || '删除失败')
      }
    }).catch(error => {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    })
  })
}

onMounted(() => {
  loadAppointments()
})
</script>

<style scoped>
.appointment-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.appointment-header {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
}

.appointment-table {
  margin-bottom: 20px;
}

.appointment-pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>