<template>
  <div class="experience-container">
    <div class="search-section">
        <el-input style="width: 240px;" v-model="data.title" placeholder="请输入……" prefix-icon="Search"></el-input>
        <el-button type="primary" style="margin-left: 10px" @click="load">查询</el-button>
        <el-button type="warning" style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="table-section" style="margin-bottom: 20px">
      <el-table :data="data.tableData" stripe border="true">
        <el-table-column label="经验标题">
          <template #default="scope">
            <el-link type="primary" @click="goToDetail(scope.row.experienceId)">
              {{ scope.row.title }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column label="发布人" prop="userName"/>
        <el-table-column label="经验内容" prop="content" :formatter="formatContent"/>
        <el-table-column label="发布时间" prop="publishTime"/>
      </el-table>
      <div style="margin-top: 15px">
        <el-pagination
          @size-change="load"
          @current-change="load"
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          :page-sizes="[5, 10, 15, 20]"
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="data.total"
        />
      </div>
    </div>

    <div class="footer-section">
      <h3>资料与自查，打造更强大的自己</h3>
      <div class="inspirational-text">
        <p>以兴趣为动力，享受每一次的锻炼时光</p>
        <p>从初学者到进阶，逐步形成自我</p>
        <p>持续锻炼与自我饮食，塑造健康身体</p>
      </div>
    </div>

  </div>
</template>

<script setup>
import { reactive } from 'vue'
import request from '@/utils/request.js'
import router from '@/router/index.js'


const data = reactive({
  tableData:[],
  pageNum: 1,
  pageSize: 5,
  total: 0,
  title:null,
})

const load = () => {
  request.get('/experience/selectPage',{ //这样才能发送这样的请求 ?pageNum=1&pageSize=5
    params: {
      pageNum: data.pageNum,
      pageSize:data.pageSize,
      title:data.title
    }
  }).then(res => {
    console.log(res)
    data.tableData = res.data.list
    data.total = res.data.total
  })
}
load()

const formatContent = (row) => {
  return row.content.length > 30
    ? row.content.substring(0, 30) + '...'
    : row.content
}

const goToDetail = (experienceId) => {
  router.push({
    name: 'experienceDetails',
    params: {
      id:experienceId, // 假设每条经验有唯一id
    }
  })
}

</script>

<style>
.experience-container {
  display: flex;
  flex-direction: column;
  justify-content: space-between;  /* 保证内容在垂直方向上分布 */
  min-height: 100vh;
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);  /* 阴影效果 */
  border-radius: 10px;  /* 圆角效果 */
  background-color: #fff;  /* 背景颜色 */
}

.sub-header span {
  cursor: pointer;
  padding: 5px 10px;
}

.search-section {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
}

.table-section {
  margin-bottom: 30px;
}

.footer-section {
  text-align: center;
}

.footer-section h3 {
  font-size: 18px;
  color: #333;
  margin-bottom: 15px;
}

.inspirational-text {
  margin-bottom: 15px;
  line-height: 1.6;
}

/* 表格样式覆盖 */
:deep(.el-table) {
  margin-top: 20px;
}

:deep(.el-table__header) {
  background-color: #f5f7fa;
}

:deep(.el-table th) {
  background-color: transparent;
  font-weight: bold;
}
</style>