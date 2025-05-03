<template>
  <div class="coach-detail-container">
    <!-- 教练基本信息 -->
    <div class="coach-info">
      <div class="avatar-container">
        <img :src="coachDetail.avatar" :alt="coachDetail.coachName" class="avatar">
      </div>
      <div class="info-container">
        <h1>{{ coachDetail.coachName }}</h1>
        <p class="description">{{ coachDetail.description }}</p>
        <button class="book-btn" @click="showBookingDialog">我要预约</button>
      </div>
    </div>

    <!-- 预约弹窗 -->
    <el-dialog
      v-model="bookingDialogVisible"
      title="预约教练"
      width="500px"
    >
      <el-form :model="bookingForm" label-width="100px">
        <el-form-item label="预约类型">
          <el-radio-group v-model="bookingForm.type">
            <el-radio label="私教预约">私教预约</el-radio>
            <el-radio label="课程预约">课程预约</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 课程选择（仅课程预约时显示） -->
        <el-form-item v-if="bookingForm.type === '课程预约'" label="选择课程">
          <el-select v-model="bookingForm.courseId" placeholder="请选择课程">
            <el-option
              v-for="course in coachCourses"
              :key="course.courseId"
              :label="course.courseName"
              :value="course.courseId"
            />
          </el-select>
        </el-form-item>

        <!-- 开始时间 -->
        <el-form-item label="开始时间">
          <el-date-picker
            v-model="bookingForm.startTime"
            type="datetime"
            placeholder="选择开始时间"
            :disabled-date="disabledDate"
            :disabled-hours="disabledHours"
          />
        </el-form-item>

        <!-- 结束时间（仅课程预约时显示） -->
        <el-form-item v-if="bookingForm.type === '课程预约'" label="结束时间">
          <el-date-picker
            v-model="bookingForm.endTime"
            type="datetime"
            placeholder="选择结束时间"
            :disabled-date="disabledDate"
            :disabled-hours="disabledHours"
          />
        </el-form-item>

        <!-- 备注 -->
        <el-form-item label="备注">
          <el-input
            v-model="bookingForm.notes"
            type="textarea"
            :rows="3"
            :placeholder="bookingForm.type === '私教预约' ? '默认预约3小时' : '请输入备注信息'"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="bookingDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleBooking">确认预约</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 评价列表 -->
    <div class="reviews-list">
      <h2>学员评价</h2>

      <div class="no-reviews">
        暂无评价
      </div>
    </div>

    <!-- 发布评价表单 -->
    <div class="review-form">
      <h2>发表评价</h2>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import request from '@/utils/request.js'
import { ElMessage } from 'element-plus'
import { useRoute } from 'vue-router'

const route = useRoute()
const coachDetail = ref({})
const bookingDialogVisible = ref(false)
const coachCourses = ref([]) // 教练的课程列表

// 预约表单数据
const bookingForm = ref({
  type: '私教预约', // 修改默认值为中文
  courseId: '', // 课程ID
  startTime: '', // 开始时间
  endTime: '', // 结束时间
})

// 显示预约弹窗
const showBookingDialog = () => {
  bookingDialogVisible.value = true
  // 重置表单
  bookingForm.value = {
    type: '私教预约', // 修改默认值为中文
    courseId: '',
    startTime: '',
    endTime: '',
  }
}

// 禁用过去的日期
const disabledDate = (time) => {
  return time.getTime() < Date.now() - 8.64e7
}

// 禁用非工作时间（示例：8:00-22:00）
const disabledHours = () => {
  const hours = []
  for (let i = 0; i < 24; i++) {
    if (i < 8 || i > 22) {
      hours.push(i)
    }
  }
  return hours
}

// 处理预约提交
const handleBooking = () => {
  if (!bookingForm.value.startTime) {
    ElMessage.warning('请选择开始时间')
    return
  }

  if (bookingForm.value.type === '课程预约') { // 修改判断条件为中文
    if (!bookingForm.value.courseId) {
      ElMessage.warning('请选择课程')
      return
    }
    if (!bookingForm.value.endTime) {
      ElMessage.warning('请选择结束时间')
      return
    }
  }

  const data = reactive({
    user: JSON.parse(localStorage.getItem('userInfo'))
  })

  // 构建预约数据
  const appointmentData = {
    userId: data.user.userId,
    coachId: route.params.id,
    type: bookingForm.value.type,
    courseId: bookingForm.value.type === '课程预约' ? bookingForm.value.courseId : null, // 修改判断条件为中文
    appointmentDate: formatDate(bookingForm.value.startTime),
    startTime: formatTime(bookingForm.value.startTime),
    endTime: bookingForm.value.type === '课程预约' ? // 修改判断条件为中文
      formatTime(bookingForm.value.endTime) :
      formatTime(new Date(bookingForm.value.startTime.getTime() + 3 * 60 * 60 * 1000)),
    notes: bookingForm.value.notes || '',
    status: '预约中'
  }

// 辅助函数
  function formatDate(date) {
    return date.toISOString().split('T')[0]
  }

  function formatTime(date) {
    return date.toTimeString().split(' ')[0]
  }

  // 调用预约接口
  request.post('/appointment/add', appointmentData).then(res => {
    if (res.code === '200') {
      ElMessage.success('预约成功')
      bookingDialogVisible.value = false
    } else {
      ElMessage.error('预约失败')
    }
  }).catch(error => {
    console.error('预约失败:', error)
    ElMessage.error('预约失败')
  })
}

onMounted(() => {
  const coachId = route.params.id
  // 获取教练详情
  request.get(`/coach/selectById/${coachId}`).then(res => {
    if (res.code === '200') {
      coachDetail.value = res.data
    } else {
      console.error(res.msg)
      ElMessage.error(res.msg || '获取教练详情失败')
    }
  }).catch(error => {
    console.error('获取教练详情失败:', error)
  })

  // 获取教练课程列表
  request.get(`/course/selectByCoachId/${coachId}`).then(res => {
    if (res.code === '200') {
      coachCourses.value = res.data
    } else {
      console.error(res.msg)
      ElMessage.error(res.msg || '获取课程列表失败')
    }
  }).catch(error => {
    console.error('获取课程列表失败:', error)
  })
})
</script>

<style>
.coach-detail-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'Arial', sans-serif;
}

.coach-info {
  display: flex;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.avatar-container {
  margin-right: 20px;
  width: 150px; /* 确保容器是正方形 */
  height: 150px; /* 确保容器是正方形 */
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  border: 1px solid #e0e0e0; /* 边框 */
}

.avatar {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 保持图片比例并填满容器 */
}

.info-container h1 {
  margin: 0;
  color: #333;
}

.book-btn {
  margin-top: 15px;
  background-color: #1890ff;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s;
}

.book-btn:hover {
  background-color: #40a9ff;
}

.description {
  color: #666;
  line-height: 1.6;
}

.review-form, .reviews-list {
  margin-bottom: 30px;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
}

h2 {
  margin-top: 0;
  color: #444;
  border-bottom: 1px solid #ddd;
  padding-bottom: 10px;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

select, textarea {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-family: inherit;
}

textarea {
  resize: vertical;
}

.submit-btn {
  background-color: #4CAF50;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

.submit-btn:hover {
  background-color: #45a049;
}

.review-item {
  padding: 15px;
  margin-bottom: 15px;
  background-color: white;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.review-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  font-size: 14px;
  color: #666;
}

.review-content {
  line-height: 1.6;
}

.no-reviews {
  text-align: center;
  color: #999;
  padding: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
