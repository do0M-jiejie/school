<template>
  <!--  顶部导航栏-->
  <div style="height: 60px; background-color: white; display: flex; align-items: center; border-bottom: 1px solid #e0e0e0;">
    <!-- 修炼健身 Logo 和名称 -->
    <div style="width: 200px; display: flex; align-items: center; padding: 0 40px;">
      <img src="../../assets/muscle.png" alt="" style="width: 40px;">
      <span style="font-size: 24px; color: #ff7f00; margin-left: 5px;">修炼健身平台</span>
    </div>

    <!-- 导航栏 -->
    <div style="flex: 1; display: flex; justify-content: space-between; align-items: center; padding: 0 20px;">
      <el-menu mode="horizontal" router :default-active="router.currentRoute.value.path" style="border: 0; padding-left: 0; padding-right: 0; width: 100%;">
        <el-menu-item index="/user/homepage" style="color: gray;" active-style="color: black; text-decoration: underline;">首页</el-menu-item>
        <el-menu-item index="/user/coaches" style="color: gray;" active-style="color: black; text-decoration: underline;">金牌教练</el-menu-item>
        <el-menu-item index="/user/course" style="color: gray;" active-style="color: black; text-decoration: underline;">健身课程</el-menu-item>
        <el-menu-item index="/user/equipment" style="color: gray;" active-style="color: black; text-decoration: underline;">健身器材</el-menu-item>
        <el-menu-item index="/user/cartDetails" style="color: gray;" active-style="color: black; text-decoration: underline;">我的购物车</el-menu-item>
        <el-menu-item index="/user/userAppointment" style="color: gray;" active-style="color: black; text-decoration: underline;">我的预约</el-menu-item>
      </el-menu>
    </div>

    <!-- 用户头像和信息 -->
    <div style="width: fit-content; padding-right: 40px; display: flex; align-items: center;">
      <span style="color: #2e2c2c; margin-left: 5px; padding-right: 10px;width: 50px">{{ data.user.username }}</span>
      <el-dropdown trigger="hover">
        <el-avatar :size="40" :src="data.user.avatar"/>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="goToDetail(data.user.userId)">查看资料</el-dropdown-item>
            <el-dropdown-item @click="ToOrderList(data.user.userId)">我的订单</el-dropdown-item>
            <el-dropdown-item divided @click="LoginOut">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>

  <!--内容区-->
    <div style="flex: 1;  background-color: white; padding: 10px">
      <RouterView/>
    </div>
    <!--内容区-->
</template>

<script setup>
import router from '@/router/index.js'
import { reactive, onUnmounted , onMounted, watch } from 'vue'

const data = reactive({
  user: JSON.parse(localStorage.getItem('userInfo'))
})

// 监听 localStorage 变化
watch(() => localStorage.getItem('userInfo'), (newValue) => {
  if (newValue) {
    data.user = JSON.parse(newValue)
  }
}, { deep: true })

const LoginOut = () => {
  localStorage.clear()
  router.push('/')
}

const goToDetail = (id) => {
  router.push({
    name: 'userInfo',
    params: {
      id:id
    }
  })
}

const ToOrderList = (id) => {
  router.push({
    name: 'orderList',
    params: {
      id:id
    }
  })
}

// 在导航栏组件的setup中添加：
onMounted(() => {
  window.addEventListener('avatar-updated', handleAvatarUpdate);
});

onUnmounted(() => {
  window.removeEventListener('avatar-updated', handleAvatarUpdate);
});

const handleAvatarUpdate = (event) => {
  // 更新本地数据
  data.user.avatar = event.detail.newAvatar;

  // 更新localStorage
  const storedUserInfo = JSON.parse(localStorage.getItem('userInfo'));
  if (storedUserInfo) {
    storedUserInfo.avatar = event.detail.newAvatar;
    localStorage.setItem('userInfo', JSON.stringify(storedUserInfo));
  }
};
</script>