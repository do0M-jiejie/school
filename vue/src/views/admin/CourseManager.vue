<template>
  <div>
    <div>
      <el-card style="margin-bottom: 10px">
        <el-input style="width: 240px" v-model="data.username" placeholder="请输入……" prefix-icon="Search"></el-input>
        <el-button type="primary" style="margin-left: 10px" @click="load">查询</el-button>
        <el-button type="warning" style="margin-left: 10px" @click="reset">重置</el-button>
      </el-card>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-button type="primary" style="margin-left: 10px" @click="handleAdd">新增</el-button>
      <el-button type="danger" style="margin-left: 10px" @click="delBatch">批量删除</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column label="封面" prop="coverImage" width="150">
          <template #default="{ row }">
            <img
              :src="row.coverImage"
              style="width: 100px; height: 100px;"
              v-if="row.coverImage"
            />
            <span v-else>暂无封面</span>
          </template>
        </el-table-column>
        <el-table-column label="课程名称" prop="courseName" width="200"/>
        <el-table-column label="教练名称" prop="coachName" width="100"/>
        <el-table-column label="课程周期" prop="duration" width="100"/>
        <el-table-column label="价格" prop="price" width="100"/>
        <el-table-column label="描述" prop="description" show-overflow-tooltip/>
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button @click="handleUpdate(scope.row)" :icon="Edit" circle type="primary"></el-button>
            <el-button @click="del(scope.row.userId)" :icon="Delete" circle type="danger"></el-button>
          </template>
        </el-table-column>
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

    <el-dialog title="新增课程" v-model="data.formVisible" width="500" destroy-on-close>
      <el-form ref="formRef" :rules="data.rules" :model="data.form" label-width="80px" style="padding-right: 40px; padding-top: 20px">
        <el-form-item label="课程封面">
          <el-upload
            class="avatar-uploader"
            action="http://localhost:8080/files/upload"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
          >
            <img v-if="data.form.coverImage" :src="data.form.coverImage" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="课程名称" prop="courseName">
          <el-input v-model="data.form.courseName" placeholder="请输入课程名称"></el-input>
        </el-form-item>
        <el-form-item label="教练名称" prop="coachName">
          <el-select v-model="data.form.coachName" placeholder="请选择教练" style="width: 100%">
            <el-option
              v-for="coach in data.coachList"
              :key="coach.coachId"
              :label="coach.coachName"
              :value="coach.coachName"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="课程周期" prop="duration">
          <el-input v-model="data.form.duration" placeholder="请输入课程周期"></el-input>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number v-model="data.form.price" :min="0" :precision="2" :step="0.1" placeholder="请输入价格"></el-input-number>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input type="textarea" v-model="data.form.description" placeholder="请输入课程描述"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary"  @click="save">
            保存
          </el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>

import { reactive, ref } from 'vue'
import request from '@/utils/request.js'
import { Delete, Edit, Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const data = reactive({
  tableData: [],
  pageNum: 1,
  pageSize: 5,
  total: 0,
  courseName:null,
  formVisible: false,
  form:{},
  ids:[],
  coachList: [],
})

const load = () => {
  request.get('/course/selectPage',{
    params:{
      pageNum: data.pageNum,
      pageSize:data.pageSize,
      courseName:data.courseName
    }
  }).then(res => {
    data.tableData = res.data.list
    data.total = res.data.total
  })
}
load()

const reset = () => {
  data.courseName = null
  load()
}

const handleAdd = () => {
  data.formVisible = true
  data.form = {}
  request.get('/coach/selectAll').then(res => {
    if(res.code === '200') {
      data.coachList = res.data
    }
  })
}

const save = () => { //一个保存方法中有两种操作 一个新增 一个编辑
  formRef.value.validate((valid)=> {
    if (valid) {
      data.form.courseId ? update() : add()
    }
  })
}

const add = () => {
  request.post('/course/add', data.form).then(res => { //新增的对象无id
    if(res.code === '200'){
      data.formVisible = false
      ElMessage.success('操作成功')
      load() //新增后要重新加载最新数据
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const update = () => {
  request.put('/course/update',data.form).then(res => { //编辑的对象有id
    if(res.code === '200'){
      data.formVisible = false
      ElMessage.success('操作成功')
      load() //更新后要重新加载最新数据
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const handleUpdate = (row) => {
  data.form = JSON.parse(JSON.stringify(row)) //深拷贝一个新的对象 用于编辑 这样不会影响行数据
  data.formVisible = true
}

const handleAvatarSuccess = (response, file) => {
  data.form.coverImage = response.data;
}

const beforeAvatarUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG) {
    ElMessage.error('上传图片只能是 JPG 或 PNG 格式!')
  }
  if (!isLt2M) {
    ElMessage.error('上传图片大小不能超过 2MB!')
  }
  return isJPG && isLt2M
}

</script>

<style scoped>
.avatar-uploader {
  text-align: center;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 178px;
  height: 178px;
}

.avatar-uploader:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
  line-height: 178px;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
  object-fit: cover;
}

.image-preview {
  margin-top: 10px;
}

.preview-image {
  max-width: 100px;
  max-height: 100px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>