<template xmlns:v-on="http://www.w3.org/1999/xhtml">
  <div>
    <el-button type="primary" @click="router.back()">返回主页</el-button>
  </div>

  <div style="bottom: 20px; margin-bottom: 20px">
    welcome
  </div>

  <div style="bottom: 20px; margin-bottom: 20px">
    {{a}}
  </div>

  <div style="bottom: 20px; margin-bottom: 20px">
    {{data.a}}
    {{data.b}}
    {{data.c}}
  </div>

  <div style="margin-bottom: 20px">
    <input v-model="data.a">
  </div>

  <div style="display: flex;margin-bottom: 20px">
    <div style="font-size: 30px; line-height: 300px; text-align: center; width: 300px; height: 300px; background-color: aqua; margin-right: 10px; " v-for="item in data.arr">
      {{ item }}
    </div>

  </div>
  <div style="margin-bottom: 20px">
    <button @click="click">
      点我加功德
    </button>
  </div>

  <div style="margin-bottom: 20px">
    <div :style="data.box"></div>
    <div>
      <img :src="data.img" alt="">
    </div>
  </div>

  <div style="margin-left: 30px">
    <el-button>Default</el-button>
  </div>

  <div style="margin: 30px">
    <el-icon :size="20">
      <Edit />
    </el-icon>

    <span style="margin-left: 30px">
      <el-icon size="20" style="top:4px"><View /></el-icon> 100
    </span>

    <el-button type="danger" :icon="Delete" circle />

    <el-input
      v-model="data.a"
      style="width: 240px"
      placeholder="Type something"
      :prefix-icon="Search"
    />

  </div>



</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { Delete, Search } from '@element-plus/icons-vue'
import router from '@/router/index.js'


const a = ref(1)
const data = reactive({
  id:router.currentRoute.value.query.id,
  name:router.currentRoute.value.query.name,
  a:1111,
  b:"你是谁",
  c:false,
  arr:[123,312,312],
  box:{
    width:'100px',
    height:'100px',
    backgroundColor:'red'
  },
  img:'https://www.runoob.com/wp-content/uploads/2017/01/vue.png'
})

console.log('获取到的id=' + data.id)
console.log('获取到的id=' + data.id + '获取到的name=' + data.name)

// onMounted(() => {
//   alert("页面加载完成")
// })

const click = () => {
  alert("好运+1")
}

</script>
