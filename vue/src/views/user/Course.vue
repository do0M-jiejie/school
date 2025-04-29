<template>
  <div>
    <!--搜索栏-->
    <div style="text-align: center;  padding-top: 20px">
      <el-input style="width: 240px" v-model="data.username" placeholder="请输入课程名称查询" prefix-icon="Search"></el-input>
      <el-button type="primary" style="margin-left: 10px;" @click="load">查询</el-button>
      <el-button type="warning" style="margin-left: 10px;" @click="reset">重置</el-button>
    </div>

    <!--课程展示区域-->
    <div class="data-cards">
      <div v-for="(card, index) in data.courseList" :key="index" class="data-card" style="padding-bottom: 10px">
        <div class="card-img" @click="goToDetail(card.courseId)">
          <img :src="card.coverImage" alt="">
        </div>

        <div style="color: #666666; font-size: 17px; font-weight: bold; text-align: left; padding-bottom: 10px; display: flex; justify-content: space-between; width: 100%">
          <div style="font-size: 15px">{{ card.courseName }}</div>
          <div style="font-size: 15px; color: red">{{ card.price }}￥</div>
        </div>

        <div style="display: flex; justify-content: space-between; width: 100%">
          <div style="color: #666666; font-size: 14px">教练:{{ card.coachName }}</div>
          <div style="color: #666666; font-size: 14px">课程周期:{{ card.duration }}</div>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>


import { reactive } from 'vue'
import request from '@/utils/request.js'
import router from '@/router/index.js'

const data = reactive ({
  courseList:[]
})

const load = () => {
  request.get('/course/selectAll',{}).then(res => {
    console.log(res)
    data.courseList = res.data
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

<style>
.data-cards {
  padding-top: 50px;
  display: grid;
  grid-template-columns: repeat(4, 1fr); /* 四列布局 */
  gap: 15px; /* 列和行之间的间距 */
  max-width: 90%; /* 限制整体宽度 */
  margin: 0 auto; /* 居中显示 */
}
.data-card {
  border-radius: 8px;
  padding: 10px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  transition: transform 0.3s ease;
}
.data-card:hover {
  transform: translateY(-5px);
}
.card-img {
  width: 300px;
  height: 280px; /* 固定高度 */
  overflow: hidden;
  border-radius: 8px;
  background-color: #f5f5f5; /* 默认背景色 */
}
</style>