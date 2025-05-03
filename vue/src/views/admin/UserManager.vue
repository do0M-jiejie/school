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
        <el-table-column label="头像" prop="avatar">
          <template #default="{ row }">
            <img
              :src="row.avatar"
              style="width: 50px; height: 50px; border-radius: 50%; object-fit: cover;"
              v-if="row.avatar"
            />
            <span v-else>无头像</span>
          </template>
        </el-table-column>
        <el-table-column label="姓名" prop="username"/>
        <el-table-column label="账号" prop="account"/>
        <el-table-column label="余额" prop="balance"/>
        <el-table-column label="权限" prop="role"/>
        <el-table-column label="邮箱" prop="email"/>
        <el-table-column label="手机号码" prop="phone"/>
        <el-table-column label="账号状态" prop="status"/>
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

    <el-dialog title="用户信息" v-model="data.formVisible" width="500" destroy-on-close>
      <el-form ref="formRef" :rules="data.rules" :model="data.form" label-width="80px" style="padding-right: 40px; padding-top: 20px">
        <el-form-item label="用户头像">
          <el-upload
            class="avatar-uploader"
            action="http://localhost:8080/files/upload"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
          >
            <!-- 关键修改：始终显示图片（如果存在），覆盖在上传组件上 -->
            <img
              v-if="data.form.avatar"
              :src="data.form.avatar"
              class="avatar"
              style="cursor: pointer;"
            />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="data.form.username" autocomplete="off" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="账号" prop="account">
          <el-input v-model="data.form.account" autocomplete="off" placeholder="请输入账号" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="data.form.password" autocomplete="off" placeholder="请输入密码"/>
        </el-form-item>
        <el-form-item label="身份" prop="role">
          <el-radio-group v-model="data.form.role">
            <el-radio value="普通用户">普通用户</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="data.form.email" autocomplete="off" placeholder="请输入邮箱"/>
        </el-form-item>
        <el-form-item label="手机号码" prop="phone">
          <el-input v-model="data.form.phone" autocomplete="off" placeholder="请输入手机号码"/>
        </el-form-item>
        <el-form-item label="账号状态" prop="status">
          <el-radio-group v-model="data.form.status">
            <el-radio value="正常">正常</el-radio>
            <el-radio value="封禁">封禁</el-radio>
          </el-radio-group>
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
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, Edit, Plus } from '@element-plus/icons-vue'

const data = reactive({
  tableData: [],
  pageNum: 1,
  pageSize: 5,
  total: 0,
  username:null,
  formVisible: false,
  form:{},
  ids:[],
  rules:{
    username: [
      { required: true, message:'请输入用户名', trigger: 'blur' }
    ],
    account: [
      { required: true, message:'请输入账号', trigger: 'blur' }
    ],
    password: [
      { required: true, message:'请输入密码', trigger: 'blur' }
    ],
    role: [
      { required: true, message:'请选择身份', trigger: 'blur' }
    ],
    email: [
      { required: true, message:'请输入邮箱', trigger: 'blur' }
    ],
    phone: [
      { required: true, message:'请输入手机号码', trigger: 'blur' }
    ],
    status: [
      { required: true, message:'请选择账号状态', trigger: 'blur' }
    ]
  }
})

const formRef = ref()

const load = () => {
  request.get('/users/selectPage',{ //这样才能发送这样的请求 ?pageNum=1&pageSize=5
    params: {
      pageNum: data.pageNum,
      pageSize:data.pageSize,
      username:data.username
    }
  }).then(res => {
    data.tableData = res.data.list
    data.total = res.data.total
  })
}
load()

const reset = () => {
  data.username = null
  load()
}

const handleAdd = () => {
  data.formVisible = true
  data.form = {}
}

const save = () => { //一个保存方法中有两种操作 一个新增 一个编辑
  formRef.value.validate((valid)=> {
    if (valid) {
      data.form.userId ? update() : add()
    }
  })
}

const add = () => {
  request.post('/users/add', data.form).then(res => { //新增的对象无id
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
  request.put('/users/update',data.form).then(res => { //编辑的对象有id
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

const del = (userId) => {
  ElMessageBox.confirm('删除数据后无法恢复，您确认删除嘛？','删除确认', {type:"warning"}).then(() => {
    request.delete('/users/deleteById/' + userId).then(res => {
      if(res.code === '200'){
        ElMessage.success('操作成功')
        load() //删除后要重新加载最新数据
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch()
}

const handleSelectionChange = (rows) => { //返回所有返回的行对象数组
  //从选中的行数组里面取出所有行的id组成一个新的数组
  data.ids = rows.map(row => row.userId)
  console.log(data.ids)
}

const delBatch = () => {
  if(data.ids.length === 0){
    ElMessage.warning('请选择数据')
    return
  }
  ElMessageBox.confirm('删除数据后无法恢复，您确认删除嘛？','删除确认', {type:"warning"}).then(() => {
    request.delete('/users/deleteBatch', { data: data.ids }).then(res => {
      if(res.code === '200'){
        ElMessage.success('操作成功')
        load() //删除后要重新加载最新数据
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch()
}

const handleAvatarSuccess = (response, file) => {
  if (response.code === '200') {
    data.form.avatar = response.data;
    ElMessage.success("头像更新成功");
  } else {
    ElMessage.error(response.msg || "上传失败");
  }
};
</script>

<style scoped>
.avatar-uploader .avatar {
  width: 120px;
  height: 120px;
  display: block;
}
</style>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  text-align: center;
}
</style>