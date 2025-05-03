<template>
  <div class="course-container">
    <h2>我的课程</h2>
    <div class="course-grid">
      <div v-for="course in courseList" :key="course.courseId" class="course-card" @click="showCourseDetail(course)">
        <div class="course-image">
          <img :src="course.coverImage" :alt="course.courseName">
        </div>
        <div class="course-info">
          <h3>{{ course.courseName }}</h3>
          <p class="duration">时长：{{ course.duration }}</p>
          <p class="price">价格：¥{{ course.price }}</p>
          <p class="description">{{ course.description }}</p>
        </div>
      </div>
    </div>

    <!-- 课程详情对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="课程详情"
      width="50%"
    >
      <el-form :model="currentCourse" label-width="100px" :rules="rules" ref="formRef">
        <el-form-item label="课程封面">
          <el-upload
            class="avatar-uploader"
            action="http://localhost:8080/files/upload"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
          >
            <img v-if="currentCourse.coverImage" :src="currentCourse.coverImage" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="课程名称" prop="courseName">
          <el-input v-model="currentCourse.courseName" />
        </el-form-item>
        <el-form-item label="课程时长" prop="duration">
          <el-input v-model="currentCourse.duration" :min="1" :max="180" />
        </el-form-item>
        <el-form-item label="课程价格" prop="price">
          <div class="price-input-container">
            <el-input
              v-model="currentCourse.price"
              :min="0"
              :precision="2"
              :step="10"
              class="price-input"
            />
            <span class="unit">元</span>
          </div>
        </el-form-item>
        <el-form-item label="课程描述" prop="description">
          <el-input
            v-model="currentCourse.description"
            type="textarea"
            :rows="4"
            placeholder="请输入课程描述"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveCourse">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request.js'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

const courseList = ref([])
const dialogVisible = ref(false)
const currentCourse = ref({})
const formRef = ref()

const rules = {
  courseName: [
    { required: true, message: '请输入课程名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  duration: [
    { required: true, message: '请输入课程时长', trigger: 'blur' }
  ],
  price: [
    { required: true, message: '请输入课程价格', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入课程描述', trigger: 'blur' }
  ]
}

const loadCourses = () => {
  // 从localStorage获取当前登录教练的信息
  const userInfo = JSON.parse(localStorage.getItem('userInfo'))
  if (!userInfo || !userInfo.coachId) {
    ElMessage.error('未获取到教练信息')
    return
  }

  request.get(`/course/selectByCoachId/${userInfo.coachId}`).then(res => {
    if (res.code === '200') {
      courseList.value = res.data
    } else {
      ElMessage.error(res.msg || '获取课程列表失败')
    }
  }).catch(error => {
    console.error('获取课程列表失败:', error)
    ElMessage.error('获取课程列表失败')
  })
}

const showCourseDetail = (course) => {
  currentCourse.value = JSON.parse(JSON.stringify(course)) // 深拷贝，避免直接修改原数据
  dialogVisible.value = true
}

const saveCourse = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.put('/course/update', currentCourse.value).then(res => {
        if (res.code === '200') {
          ElMessage.success('更新成功')
          dialogVisible.value = false
          loadCourses() // 重新加载课程列表
        } else {
          ElMessage.error(res.msg || '更新失败')
        }
      }).catch(error => {
        console.error('更新失败:', error)
        ElMessage.error('更新失败')
      })
    }
  })
}

const handleAvatarSuccess = (response, file) => {
  // 更新当前课程的封面图片
  currentCourse.value.coverImage = response.data; // 假设response.data是图片URL
  ElMessage.success('封面图片上传成功')
}

onMounted(() => {
  loadCourses()
})
</script>

<style scoped>
.course-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

h2 {
  text-align: center;
  color: #333;
  margin-bottom: 30px;
}

.course-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  padding: 20px;
}

.course-card {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
  cursor: pointer;
}

.course-card:hover {
  transform: translateY(-5px);
}

.course-image {
  width: 100%;
  height: 200px;
  overflow: hidden;
}

.course-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.course-info {
  padding: 15px;
}

.course-info h3 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 1.2em;
}

.duration, .price {
  color: #666;
  margin: 5px 0;
  font-size: 0.9em;
}

.description {
  color: #666;
  font-size: 0.9em;
  margin-top: 10px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.unit {
  margin-left: 8px;
  color: #666;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.price-input-container {
  display: flex;
  align-items: center;
  width: 100%;
}

.price-input {
  width: 100px; /* 或您需要的宽度 */
  margin-right: 8px;
}
</style>