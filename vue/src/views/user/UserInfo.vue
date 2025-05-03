<template>
  <div class="user-profile">
    <div class="profile-header">
      <div class="avatar-container">
        <img class="avatar" :src="userInfo.avatar" alt="User Avatar" />
      </div>

      <div class="buttons-container">
        <el-button class="recharge-button" type="primary" @click="openRechargeDialog">充值</el-button>
        <el-button class="edit-button" @click="handleUpdate(userInfo)">修改信息</el-button>
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
            <el-input v-model="data.form.username" autocomplete="off" placeholder="请输入名称" />
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="data.form.password" autocomplete="off" placeholder="请输入手机号码"/>
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="data.form.email" autocomplete="off" placeholder="请输入邮箱"/>
          </el-form-item>
          <el-form-item label="手机号码" prop="phone">
            <el-input v-model="data.form.phone" autocomplete="off" placeholder="请输入手机号码"/>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="data.formVisible = false">取消</el-button>
            <el-button type="primary"  @click="update">
              保存
            </el-button>
          </div>
        </template>
      </el-dialog>
    </div>

    <el-dialog v-model="dialogVisible" title="余额充值" @close="handleDialogClose" width="500" destroy-on-close>
      <el-form>
        <el-form-item label="充值账户" prop="username">
          <el-input v-model="userInfo.username" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="充值金额" prop="charge">
          <el-input v-model="rechargeAmount" autocomplete="off" placeholder="请输入金额"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleRecharge">确 定</el-button>
      </span>
    </el-dialog>

    <div class="info-list">
      <div class="info-item">
        <span class="info-label">用户名:</span>
        <span class="info-value">{{ userInfo.username }}</span>
      </div>
      <div class="info-item">
        <span class="info-label">邮箱:</span>
        <span class="info-value">{{ userInfo.email }}</span>
      </div>
      <div class="info-item">
        <span class="info-label">电话号码:</span>
        <span class="info-value">{{ userInfo.phone }}</span>
      </div>
      <div class="info-item">
        <span class="info-label">性别:</span>
        <span class="info-value">{{ userInfo.gender }}</span>
      </div>
      <div class="info-item">
        <span class="info-label">身高:</span>
        <span class="info-value">{{ userInfo.height }} cm</span>
      </div>
      <div class="info-item">
        <span class="info-label">体重:</span>
        <span class="info-value">{{ userInfo.weight }} kg</span>
      </div>
      <div class="info-item">
        <span class="info-label">余额:</span>
        <span class="info-value">{{ userInfo.balance }} 元</span>
      </div>
      <div class="info-item">
        <span class="info-label">注册时间:</span>
        <span class="info-value">{{ userInfo.registrationDate }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive} from 'vue'
import request from '@/utils/request.js'
import { ElMessage } from 'element-plus'
import { useRoute } from 'vue-router'
import { Plus } from '@element-plus/icons-vue'

const route = useRoute()
const userInfo = ref({})
const dialogVisible = ref(false)
const rechargeAmount = ref(1000)
const data = reactive({
  formVisible:false,
  rules:{
    username: [
      { required: true, message:'请输入用户名', trigger: 'blur' }
    ],
    password: [
      { required: true, message:'请输入密码', trigger: 'blur' }
    ],
    email: [
      { required: true, message:'请输入邮箱', trigger: 'blur' }
    ],
    phone: [
      { required: true, message:'请输入手机号码', trigger: 'blur' }
    ]
  }
})

const loadUserInfo = () => {
  const userId = route.params.id
  request.get(`/users/selectById/${userId}`).then(res => {
    if (res.code === '200') {
      userInfo.value = res.data
    }
  })
}

onMounted(() => {
  loadUserInfo()
})

// 打开充值弹窗
const openRechargeDialog = () => {
  rechargeAmount.value = 1000
  dialogVisible.value = true
}

// 处理充值操作
const handleRecharge = () => {
  if (rechargeAmount.value <= 0) {
    ElMessage.error('请输入有效的充值金额')
    return
  }

  // 发送充值金额到后端
  request.put('/users/updateByName',{
    amount: Number(rechargeAmount.value),
    name: userInfo.value.username
  })
    .then(res => {
      if (res.code === '200') {
        ElMessage.success('充值成功')
        // 计算新的余额
        const newBalance = Number(userInfo.value.balance) + Number(rechargeAmount.value)

        // 更新本地 userInfo
        userInfo.value.balance = newBalance

        // 更新 localStorage 中的 userInfo
        const storedUserInfo = JSON.parse(localStorage.getItem('userInfo'))
        if (storedUserInfo) {
          storedUserInfo.balance = newBalance
          localStorage.setItem('userInfo', JSON.stringify(storedUserInfo))
        }
        dialogVisible.value = false  // 关闭弹窗
      } else {
        ElMessage.error(res.msg || '充值失败')
      }
    })
    .catch(error => {
      console.error('充值失败:', error)
      ElMessage.error('充值失败，请稍后再试')
    })
}

// 关闭弹窗
const handleDialogClose = () => {
  rechargeAmount.value = 0 // 清空充值金额
}

const update = () => {
  request.put('/users/update',data.form).then(res => { //编辑的对象有id
    if(res.code === '200'){
      data.formVisible = false
      ElMessage.success('操作成功')
      // 更新 userInfo 为最新数据
      userInfo.value = { ...userInfo.value, ...data.form } // 使用表单中更新的数据更新 userInfo
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const handleUpdate = (row) => {
  data.form = JSON.parse(JSON.stringify(row)) //深拷贝一个新的对象 用于编辑 这样不会影响行数据
  data.formVisible = true
}

const handleAvatarSuccess = (response,file) => {
  if (response.code === '200') {
    data.form.avatar = response.data;
    // 更新 userInfo 中的头像
    userInfo.value.avatar = response.data;
    // 更新 localStorage 中的用户信息
    const storedUserInfo = JSON.parse(localStorage.getItem('userInfo'));
    if (storedUserInfo) {
      storedUserInfo.avatar = response.data;
      localStorage.setItem('userInfo', JSON.stringify(storedUserInfo));
    }
    window.dispatchEvent(new CustomEvent('avatar-updated', {
      detail: { newAvatar: response.data }
    }));
    ElMessage.success("头像更新成功");
  } else {
    ElMessage.error(response.msg || "上传失败");
  }
}

</script>

<style scoped>
.avatar-uploader .avatar {
  width: 120px;
  height: 120px;
  display: block;
}

.user-profile {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 600px;
  width: 400px;
  margin: 0 auto;
  padding: 20px;
  border-radius: 8px;
  background-color: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  position: relative;
}

.profile-header {
  display: flex;
  justify-content: center;
  width: 100%;
  position: relative;
  margin-bottom: 20px;
}

.avatar-container {
  text-align: center;
}

.avatar {
  width: 120px;
  height: 120px;
  //border-radius: 50%;
  object-fit: cover;
}

.buttons-container {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 10px;
}

.recharge-button, .edit-button {
  width: 120px;
}

.info-list {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  font-size: 16px;
  color: #34495e;
}

.info-label {
  font-weight: 600;
}

.info-value {
  font-weight: 400;
  color: #2c3e50;
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
