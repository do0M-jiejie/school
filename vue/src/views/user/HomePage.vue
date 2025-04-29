<template>
  <!-- 父路由内容（已有导航栏保持不变） -->

  <!-- 下方内容区（红框区域） -->
  <div class="content-container" style="padding-top: 5px">
    <!-- 轮播图区域 -->
    <div class="carousel-section">
      <el-carousel :interval="3000" type="card" height="300px">
        <el-carousel-item v-for="(item, index) in data.carousel" :key="index">
          <img class="carousel-image" :src="item" :alt="'轮播图'+index">
        </el-carousel-item>
      </el-carousel>
    </div>

    <!-- 文字提示区域 -->
    <div class="text-tip">
      <p>专业健身指导 · 科学训练方案 · 个性化课程定制</p>
    </div>

    <!-- 四个相同数据展示区域 -->
    <div class="data-cards">
      <div v-for="(card, index) in data.courseList" :key="index" class="data-card">
        <div class="card-img" @click="goToDetail(card.courseId)">
          <img :src="card.coverImage" alt="">
        </div>

        <div style="color: #666666; font-size: 17px; font-weight: bold; text-align: left; padding-bottom: 10px">{{ card.courseName }}</div>

        <div style="display: flex; justify-content: space-between;">
          <div style="color: #666666; font-size: 14px">教练:{{ card.coachName }}</div>
          <div style="color: #666666; font-size: 14px">课程周期:{{ card.duration }}</div>
        </div>
      </div>
    </div>

  </div>

</template>

<script setup>
import { reactive } from 'vue'
import lun1 from '@/assets/lun1.jpg'
import lun2 from '@/assets/lun2.jpg'
import lun3 from '@/assets/lun3.jpg'
import request from '@/utils/request.js'
import { useRouter } from 'vue-router'

const router = useRouter()
const data = reactive({
  carousel:[lun1,lun2,lun3],
  courseList:[]
})

const load = () => {
  request.get('/course/selectAll').then(res => {
    data.courseList = res.data.slice(0,4)
  })
}

load()

const goToDetail = (id) => {
  router.push({
    name: 'courseDetails',
    params: {
      id:id
    }
  })
}

</script>

<style scoped>
.content-container {
  padding: 20px;
  background-color: #f8f8f8;
}

/* 轮播图样式 */
.carousel-section {
  width: 80%;
  margin: 0 auto 30px;
  border-radius: 8px;
  overflow: visible; /* 修改为visible */
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 6px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

/* 深度选择器修改组件默认样式 */
:deep(.el-carousel__container) {
  height: 300px !important;
}

:deep(.el-carousel__item) {
  padding: 0 15px;
  transform: scale(0.9);
  transition: transform 0.3s;
}

:deep(.el-carousel__item.is-active) {
  transform: scale(1);
}

/* 文字提示样式 */
.text-tip {
  text-align: center;
  margin: 20px 0 30px;
  font-size: 18px;
  color: #666;
  padding: 15px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* 四个数据卡片样式 */
.data-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr); /* 将内容分为 4 列 */
  gap: 20px; /* 设置列和行之间的间距 */
}

.data-card {
  border-radius: 8px;
  padding: 10px;
  background-color: #fff; /* 设置卡片的背景颜色 */
}

.card-img img {
  width: 100%; /* 让图片充满容器 */
  height: auto; /* 保持图片的纵横比 */
  border-radius: 8px;
}

</style>