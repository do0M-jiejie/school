<template>
  <div>
    <!--搜索栏-->
    <div style="text-align: center; padding-top: 20px">
      <el-input style="width: 240px" v-model="data.username" placeholder="请输入器材名称查询" prefix-icon="Search"></el-input>
      <el-button type="primary" style="margin-left: 10px;" @click="load">查询</el-button>
      <el-button type="warning" style="margin-left: 10px;" >重置</el-button>
    </div>

    <div class="cookbook-grid" style="padding-top: 50px">
      <div v-for="(cookbook, index) in data.cookbookList" :key="index" class="cookbook-card">
        <div class="cookbook-image">
          <img :src="cookbook.cookBookImage" alt="食谱图片">
        </div>
        <div class="cookbook-info">
          <div class="cookbook-name">{{ cookbook.title }}</div>
        </div>
        <div class="cookbook-time">
          <div>发布时间：{{ cookbook.publishTime }}</div>
        </div>
        <div class="btn-container">
          <el-button type="primary" class="content" @click="showDetails(cookbook)">查看食谱</el-button>
        </div>
      </div>
    </div>

    <!-- 菜谱详情对话框 -->
    <el-dialog v-model="data.formVisible" title="菜谱详情" width="600px">
      <div v-if="data.currentCookbook">
        <!-- 标题和图片 -->
        <div class="detail-header">
          <h2>{{ data.currentCookbook.title }}</h2>
        </div>

        <!-- 营养数据 -->
        <div class="detail-section">
          <h3>营养数据</h3>
          <p>{{ data.currentCookbook.nutrition }}</p>
        </div>

        <!-- 食材 -->
        <div class="detail-section">
          <h3>食材</h3>
          <ul>
            <li v-for="(ingredient, index) in data.currentCookbook.ingredients" :key="index">
              {{ ingredient }}
            </li>
          </ul>
        </div>

        <!-- 制作流程 -->
        <div class="detail-section">
          <h3>制作流程</h3>
          <ol>
            <li v-for="(step, index) in data.currentCookbook.steps" :key="index">
              {{ step }}
            </li>
          </ol>
        </div>

        <!-- 适用场景 -->
        <div class="detail-section">
          <h3>适用场景</h3>
          <p>{{ data.currentCookbook.scenario }}</p>
        </div>
      </div>

      <template #footer>
        <el-button @click="data.formVisible = false">关闭</el-button>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import { reactive } from 'vue'
import request from '@/utils/request.js'

const data = reactive({
  cookbookList: [],
  formVisible: false,
  currentCookbook: [], // 当前选中的菜谱
  username: ''
})

const load = () => {
  request.get('/cookbook/selectAll', {}).then(res => {
    console.log(res.data)
    data.cookbookList = res.data
  })
}

load()

const showDetails = (cookbook) => {
  data.currentCookbook = cookbook;

  // 更健壮的分隔方法
  const contentSections = cookbook.content.split(/\r?\n/) // 兼容两种换行符
    .map(line => line.trim())
    .filter(line => line !== "");

  console.log("分隔后的内容：", contentSections);

  // 解析营养数据（兼容不同格式）
  const nutritionLine = contentSections.find(line => line.startsWith("营养数据"));
  data.currentCookbook.nutrition = nutritionLine
    ? nutritionLine.replace(/营养数据[:：]\s*/, "")
    : "无";

  // 解析食材（兼容不同格式）
  const ingredientsStart = contentSections.findIndex(line => /^食材[:：]/.test(line));
  const stepsStart = contentSections.findIndex(line => /^制作流程[:：]/.test(line));
  data.currentCookbook.ingredients = ingredientsStart !== -1 && stepsStart !== -1
    ? contentSections.slice(ingredientsStart + 1, stepsStart).filter(Boolean)
    : [];

  // 解析制作步骤
  const scenarioStart = contentSections.findIndex(line => /^适用场景[:：]/.test(line));
  data.currentCookbook.steps = stepsStart !== -1
    ? contentSections.slice(stepsStart + 1, scenarioStart !== -1 ? scenarioStart : undefined).filter(Boolean)
    : [];

  // 更健壮的适用场景解析
  const scenarioLine = contentSections.find(line => /^适用场景[:：]/.test(line));
  data.currentCookbook.scenario = scenarioLine
    ? scenarioLine.replace(/^适用场景[:：]\s*/, "") || "无" // 处理空场景
    : "无";

  console.log("解析结果：", {
    nutrition: data.currentCookbook.nutrition,
    ingredients: data.currentCookbook.ingredients,
    steps: data.currentCookbook.steps,
    scenario: data.currentCookbook.scenario
  });

  data.formVisible = true;
}

</script>

<style>
.cookbook-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 15px;
  max-width: 90%;
  margin: 0 auto;
}

.cookbook-card {
  border-radius: 8px;
  padding: 10px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  transition: transform 0.3s ease;
}

.cookbook-card:hover {
  transform: translateY(-5px);
}

.cookbook-image {
  width: 100%;
  height: 280px;
  overflow: hidden;
  border-radius: 8px;
  background-color: #f5f5f5;
}

.cookbook-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.cookbook-info {
  display: flex;
  justify-content: space-between;
  width: 100%;
  padding: 10px 0;
}

.cookbook-name {
  flex: 1;
  font-size: 24px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  padding-right: 10px;
}

.btn-container {
  margin-top: auto;
  width: 100%;
  padding-top: 10px;
}

.detail-header {
  text-align: left;
}

.detail-header h2 {
  font-size: 28px;
  margin-bottom: 20px;
}

.detail-section {
  margin-top: 20px;
}

.detail-section h3 {
  font-size: 20px;
  margin-bottom: 10px;
}

.detail-section p, .detail-section ul, .detail-section ol {
  margin-left: 20px;
}

.detail-section ul {
  list-style-type: disc;
}

.detail-section ol {
  list-style-type: decimal;
}
</style>