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
      </div>
    </div>

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
import { ref, onMounted } from 'vue'
import request from '@/utils/request.js'
import { ElMessage } from 'element-plus'
import { useRoute } from 'vue-router'

const route = useRoute()
const coachDetail = ref({})

onMounted(() => {
  const coachId = route.params.id
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
</style>
