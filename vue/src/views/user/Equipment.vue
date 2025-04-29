<template>
  <div>
    <!--搜索栏-->
    <div style="text-align: center;  padding-top: 20px">
      <el-input style="width: 240px" v-model="data.username" placeholder="请输入器材名称查询" prefix-icon="Search"></el-input>
      <el-button type="primary" style="margin-left: 10px;" @click="load">查询</el-button>
      <el-button type="warning" style="margin-left: 10px;" @click="reset">重置</el-button>
    </div>

    <div class="equipment-grid" style="padding-top: 50px">
      <div v-for="(equipment, index) in data.equipmentList" :key="index" class="equipment-card">
        <div class="equipment-image">
          <img :src="equipment.equipmentImage" alt="器材图片">
        </div>
        <div class="equipment-info">
          <div class="equipment-name">{{ equipment.equipmentName }}</div>
          <div class="equipment-status">{{ equipment.status }}</div>
        </div>
        <div class="btn-container">
          <el-button type="info" class="instruction-btn">使用说明</el-button>
          <el-button type="primary" class="book-btn">我要预约</el-button>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>

import { reactive } from 'vue'
import request from '@/utils/request.js'

const data = reactive ({
  equipmentList:[]
})

const load = () => {
  request.get('/equipment/selectAll',{}).then(res => {
    data.equipmentList = res.data
  })
}
load()
</script>

<style>
.equipment-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr); /* 四列布局 */
  gap: 15px; /* 列和行之间的间距 */
  max-width: 90%; /* 限制整体宽度 */
  margin: 0 auto; /* 居中显示 */
}

.equipment-card {
  border-radius: 8px;
  padding: 10px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  transition: transform 0.3s ease;
}

.equipment-card:hover {
  transform: translateY(-5px);
}

.equipment-image {
  width: 100%;
  height: 280px; /* 固定高度 */
  overflow: hidden;
  border-radius: 8px;
  background-color: #f5f5f5; /* 默认背景色 */
}

.equipment-image img {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 保持图片比例并填充容器 */
  display: block;
}

.equipment-info {
  display: flex;
  justify-content: space-between;
  width: 100%;
  padding: 10px 0;
}

.equipment-name {
  flex: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  padding-right: 10px;
}

.btn-container {
  margin-top: auto;
  width: 100%;
  padding-top: 10px;
  display: flex;
  justify-content: space-between;
  gap: 10px; /* 按钮间距 */
}

/* 使用说明按钮 - 使用info颜色 */
.instruction-btn {
  flex: 1;
  background-color: var(--el-color-info);
  border-color: var(--el-color-info);
}

/* 预约按钮 - 使用primary颜色 */
.book-btn {
  flex: 1;
  background-color: var(--el-color-primary);
  border-color: var(--el-color-primary);
}

/* 悬停效果 */
.instruction-btn:hover {
  background-color: var(--el-color-info-light-3);
  border-color: var(--el-color-info-light-3);
}

.book-btn:hover {
  background-color: var(--el-color-primary-light-3);
  border-color: var(--el-color-primary-light-3);
}

/* 响应式调整 */
@media (max-width: 480px) {
  .btn-container {
    flex-direction: column;
  }
}
</style>