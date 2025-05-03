<template>
  <!-- 头部 -->
  <div style="height: 60px;background-color: #47afdc; display: flex; align-items: center">
    <div style="width: 250px; display: flex; align-items: center;padding: 20px">
      <img src="../../assets/muscle.png" alt="" style="width: 40px">
      <span style="font-size: 24px; color: white;margin-left: 5px"> 后台管理系统</span>
    </div>
    <div style="flex: 1;"></div>
    <div style="width: fit-content;padding-right: 10px; display: flex;align-items: center">
      <img src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" alt="" style="width: 40px; height: 40px">
      <span style="color: white; margin-left: 5px">{{ data.user.coachName }}</span>
    </div>
  </div>
  <!-- 头部结束 -->
  <!-- 其余 -->
  <div style="display: flex">
    <!--左侧导航栏-->
    <div style="width: 200px;border-right: 1px solid #ddd; min-height: calc(100vh - 60px)">
      <el-menu router :default-active="router.currentRoute.value.path" style="border: 0">
        <el-menu-item index="/coach/home"><el-icon><House /></el-icon>系统首页</el-menu-item>
        <el-menu-item index="/coach/appointmentDetail"><el-icon><User /></el-icon>预约管理</el-menu-item>
        <el-menu-item index="/data"><el-icon><DataAnalysis /></el-icon>数据统计</el-menu-item>
        <el-menu-item index="/login"><el-icon><SwitchButton /></el-icon>退出登录</el-menu-item>
      </el-menu>
    </div>
    <!--左侧导航栏-->
    <!--右侧内容区-->
    <div style="flex: 1; width: 0; background-color: #f8f8ff; padding: 10px">
      <RouterView/>
    </div>
    <!--右侧内容区-->

  </div>
</template>

<script setup>
import router from '@/router/index.js'
import { reactive } from 'vue'

const data = reactive({
  user: JSON.parse(localStorage.getItem('userInfo'))
})
</script>