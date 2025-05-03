<template>
  <h1>教练后台首页</h1>
  <h1>{{data.user}}</h1>
</template>
<script setup>

import { reactive } from 'vue'

const data = reactive({
  user: JSON.parse(localStorage.getItem('userInfo'))
})

</script>