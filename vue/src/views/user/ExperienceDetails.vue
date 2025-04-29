<template>
  <div class="experience-details-container">
    <div class="header">
      <h1>{{ experienceDetail.title }}</h1>
      <p class="author">
        <span>作者: {{ experienceDetail.userName }}</span>
        <span class="publish-time">{{ experienceDetail.publishTime }}</span>
      </p>
    </div>

    <div class="content">
      <p v-html="experienceDetail.content"></p>
    </div>

    <div v-if="!experienceDetail.content" class="loading">
      <p>加载中...</p>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import request from '@/utils/request.js'
import { ElMessage } from 'element-plus'
import { useRoute } from 'vue-router'

const route = useRoute()
const experienceDetail = ref({})

onMounted(() => {
  const experienceId = route.params.id
  request.get(`/experience/selectById/${experienceId}`).then(res => {
    if (res.code === '200') {
      experienceDetail.value = res.data
    } else {
      console.error(res.msg)
      ElMessage.error(res.msg || '获取经验详情失败')
    }
  }).catch(error => {
    console.error('获取经验详情失败:', error)
  })
})
</script>

<style scoped>
.experience-details-container {
  display: flex;
  flex-direction: column;
  align-items: center;  /* 水平居中 */
  min-height: 100vh;  /* 容器最小高度为视口高度 */
  max-width: 1000px;
  margin: 0 auto;/* 宽度自适应屏幕 */
  padding: 20px;
  background-color: #fff;
  box-sizing: border-box;
  overflow-y: auto;  /* 如果内容过多，允许滚动 */
  border-radius: 8px;  /* 保持圆角效果 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);  /* 为容器添加轻微的阴影 */
}

.header {
  margin-bottom: 20px;
  width: 100%;
  max-width: 1000px;  /* 限制最大宽度，避免超出 */
}

.header h1 {
  font-size: 28px;  /* 增大标题字体 */
  font-weight: bold;
  color: #2c3e50;  /* 深色标题 */
  text-align: center;  /* 标题居中 */
}

.author {
  font-size: 16px;
  color: #7f8c8d;  /* 温和的灰色 */
  margin-top: 10px;
  text-align: center;  /* 作者信息居中 */
}

.author span {
  margin-right: 20px;
}

.publish-time {
  color: #95a5a6;
  text-align: center;  /* 发布时间居中 */
}

.content {
  font-size: 18px;
  color: #34495e;  /* 深灰色文字 */
  line-height: 1.8;
  width: 100%;
  max-width: 1000px;  /* 限制最大宽度 */
  margin-top: 20px;
}

.loading {
  text-align: center;
  color: #aaa;
  font-size: 16px;
}
</style>
