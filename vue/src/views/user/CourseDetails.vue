<template>
  <div>
    <!-- 课程详情内容 -->
    <div class="course-container">
      <!-- 课程图片和内容部分 -->
      <div class="course-header-container">
        <!-- 课程图片部分 -->
        <div class="course-img" v-if="courseDetail.coverImage">
          <img :src="courseDetail.coverImage" alt="课程封面">
        </div>

        <!-- 课程标题和购买部分 -->
        <div class="course-header">
          <!-- 课程标题部分 -->
          <div class="course-header-left">
            <h2 class="course-subtitle">课程名称：{{ courseDetail.courseName }}</h2>
            <div class="course-meta">
              <span class="meta-item">课程教练：{{ courseDetail.coachName }}</span>
              <span class="meta-separator">|</span>
              <span class="meta-item">课程周期：{{ courseDetail.duration }}</span>
            </div>
          </div>

          <!-- 课程价格和购买按钮部分 -->
          <div class="price-and-buy">
            <span class="price">￥{{ courseDetail.price }}</span>
            <button class="buy-btn" @click="handleAddToCart">加入购物车</button>
          </div>
        </div>
      </div>

      <div class="divider"></div>

      <!-- 课程内容 -->
      <div class="course-content">
        <h2 class="section-title">课程概况</h2>
        <p class="course-description">{{ courseDetail.description }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { useRoute } from 'vue-router'
import request from '@/utils/request.js'
import { ElMessage } from 'element-plus'

const route = useRoute()
const courseDetail = ref({})
const data = reactive({
  user: JSON.parse(localStorage.getItem('userInfo'))
})

onMounted(() => {
  const courseId = route.params.id
  // 修改为新的接口路径
  request.get(`/course/selectById/${courseId}`).then(res => {
    if (res.code === '200') {
      courseDetail.value = res.data
    } else {
      console.error(res.msg)
      ElMessage.error(res.msg || '获取课程详情失败')
    }
  }).catch(error => {
    console.error('获取课程详情失败:', error)
  })
})

const handleAddToCart = () => {
  // 检查用户是否登录
  if (!data.user || !data.user.userId) {
    ElMessage.error('请先登录后再操作')
    return
  }

  // 检查课程信息是否完整
  if (!courseDetail.value.courseId) {
    ElMessage.error('课程信息不完整')
    return
  }

  const cartData = {
    userId: data.user.userId,
    courseId: courseDetail.value.courseId,
    courseName: courseDetail.value.courseName,
    price: courseDetail.value.price,
    coachName: courseDetail.value.coachName,
    createdTime: new Date().toISOString().slice(0, 19).replace('T', ' ')
  }

  request.post('/cart/add', cartData)
    .then(res => {
      if (res.code === '200') {
        ElMessage.success('成功加入购物车')
      } else if (res.code === '500' ) {
        ElMessage.warning('该课程已在购物车中')
      } else {
        ElMessage.error(res.msg || '加入购物车失败')
      }
    })
}

</script>

<style>
.course-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
  color: #333;
  line-height: 1.6;
}

.course-header-container {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
}

.course-img {
  width: 30%;
  margin-right: 20px;
}

.course-img img {
  width: 100%;
  height: auto;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.course-header {
  width: 65%;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}

.course-header-left {
  flex: 1;
}

.course-subtitle {
  font-size: 18px;
  font-weight: bold;
  color: #000;
  margin-bottom: 15px;
}

.course-meta {
  display: flex;
  justify-content: flex-start;
  flex-wrap: wrap;
  gap: 10px;
  font-size: 15px;
  color: #666;
}

.meta-item {
  white-space: nowrap;
}

.meta-separator {
  color: #ccc;
}

.price-and-buy {
  display: flex;
  justify-content: flex-start;
  gap: 10px;
  margin-top: 20px;
}

.price {
  font-size: 20px;
  color: #e74c3c;
  font-weight: bold;
}

.buy-btn {
  padding: 10px 20px;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

.buy-btn:hover {
  background-color: #2980b9;
}

.divider {
  height: 1px;
  background-color: #eaeaea;
  margin: 20px 0;
  border: none;
}

.course-content {
  margin-bottom: 20px;
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  color: #000;
  margin-bottom: 15px;
  padding-bottom: 8px;
  border-bottom: 1px solid #f0f0f0;
}

.course-description {
  font-size: 15px;
  color: #333;
  line-height: 1.8;
  text-align: justify;
  margin-bottom: 16px;
}
</style>
