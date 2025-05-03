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
        <el-table-column label="操作" width="200" align="center">
          <template #default="scope">
            <el-button
              v-if="scope.row.status === '预约中'"
              @click="handleApprove(scope.row)"
              type="success"
              size="small"
            >
              同意预约
            </el-button>
            <el-button
              v-if="scope.row.status === '预约中'"
              @click="handleReject(scope.row)"
              type="danger"
              size="small"
            >
              拒绝预约
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
const coach = ref(JSON.parse(localStorage.getItem('userInfo')))

// 获取预约列表
const loadAppointments = () => {
    request.get('/appointment/coachAppointments', {
    params: {
      coachId: coach.value.coachId,
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
    case '预约成功':
      return 'success'
    case '预约失败':
      return 'danger'
    case '已取消':
      return 'info'
    default:
      return ''
  }
}

// 同意预约
const handleApprove = (row) => {
  ElMessageBox.confirm(
    '确定同意该预约请求吗？',
    '同意预约',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'success'
    }
  ).then(() => {
    request.put('/appointment/approve', {
      appointmentId: row.appointmentId
    }).then(res => {
      if (res.code === '200') {
        ElMessage.success('已同意预约')
        loadAppointments()
      } else {
        ElMessage.error(res.msg || '操作失败')
      }
    }).catch(error => {
      console.error('操作失败:', error)
      ElMessage.error('操作失败')
    })
  })
}

// 拒绝预约
const handleReject = (row) => {
  ElMessageBox.prompt('请输入拒绝原因', '拒绝预约', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputType: 'textarea',
    inputPlaceholder: '请输入拒绝原因',
    inputValidator: (value) => {
      if (!value) {
        return '请输入拒绝原因'
      }
      return true
    }
  }).then(({ value }) => {
    request.put('/appointment/reject', {
      appointmentId: row.appointmentId,
      notes: value
    }).then(res => {
      if (res.code === '200') {
        ElMessage.success('已拒绝预约')
        loadAppointments()
      } else {
        ElMessage.error(res.msg || '操作失败')
      }
    }).catch(error => {
      console.error('操作失败:', error)
      ElMessage.error('操作失败')
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