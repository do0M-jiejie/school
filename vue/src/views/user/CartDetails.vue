<template>
  <div class="cart-container">
    <h2 class="cart-title">我的购物车</h2>
    
    <!-- 购物车列表 -->
    <div class="cart-list" v-if="cartItems.length > 0">
      <el-table :data="cartItems" style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="courseName" label="课程名称" />
        <el-table-column prop="coachName" label="教练" />
        <el-table-column prop="price" label="价格">
          <template #default="scope">
            ￥{{ scope.row.price }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleSinglePurchase(scope.row)">
              立即购买
            </el-button>
            <el-button type="danger" size="small" @click="handleRemove(scope.row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 底部操作栏 -->
      <div class="cart-footer">
        <div class="cart-total">
          已选择 {{ selectedItems.length }} 件商品
          总计：<span class="total-price">￥{{ totalPrice }}</span>
        </div>
        <div class="cart-actions">
          <el-button type="danger" @click="handleBatchRemove" :disabled="selectedItems.length === 0">
            批量移除
          </el-button>
          <el-button type="primary" @click="handleBatchPurchase" :disabled="selectedItems.length === 0">
            批量购买
          </el-button>
        </div>
      </div>
    </div>

    <!-- 空购物车提示 -->
    <div class="empty-cart" v-else>
      <el-empty description="购物车是空的" />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request.js'

const cartItems = ref([])
const selectedItems = ref([])
const user = ref(JSON.parse(localStorage.getItem('userInfo')))

// 计算总价
const totalPrice = computed(() => {
  return selectedItems.value.reduce((total, item) => total + item.price, 0)
})

// 获取购物车列表
const getCartList = () => {
  const userId = user.value.userId
  if (!userId) {
    ElMessage.error('请先登录')
    return
  }

  request.get(`/cart/list/${userId}`).then(res => {
    if (res.code === '200') {
      cartItems.value = res.data
    } else {
      ElMessage.error(res.msg || '获取购物车列表失败')
    }
  }).catch(error => {
    console.error('获取购物车列表失败:', error)
    ElMessage.error('获取购物车列表失败，请稍后重试')
  })
}

// 表格选择变化时的处理函数
const handleSelectionChange = (selection) => {
  selectedItems.value = selection
}

// 单个购买
const handleSinglePurchase = (item) => {
  const userBalance = user.value.balance
  if (userBalance < item.price) {
    ElMessage.error('账户余额不足，无法完成购买')
    return
  }

  ElMessageBox.confirm(
    `确认购买课程"${item.courseName}"？`,
    '购买确认',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    // 创建订单
    request.post('/order/create', {
      userId: user.value.userId,
      courseId: item.courseId
    }).then(res => {
      if (res.code === '200') {
        // 更新用户余额
        request.put('/users/updateByName', {
          amount: item.price,
          name: user.value.username
        }).then(() => {
          ElMessage.success('购买成功')
          // 从购物车中移除
          handleRemove(item)
          // 刷新用户信息
          user.value.balance -= item.price
          localStorage.setItem('userInfo', JSON.stringify(user.value))
        })
      } else {
        ElMessage.error(res.msg || '购买失败')
      }
    })
  })
}

// 批量购买
const handleBatchPurchase = () => {
  const totalAmount = totalPrice.value
  const userBalance = user.value.balance

  if (userBalance < totalAmount) {
    ElMessage.error('账户余额不足，无法完成购买')
    return
  }

  ElMessageBox.confirm(
    `确认购买选中的 ${selectedItems.value.length} 个课程？`,
    '批量购买确认',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    // 批量创建订单
    const promises = selectedItems.value.map(item => 
      request.post('/order/create', {
        userId: user.value.userId,
        courseId: item.courseId
      })
    )

    Promise.all(promises).then(() => {
      // 更新用户余额
      request.put('/users/updateByName', {
        amount: totalAmount,
        name: user.value.username
      }).then(() => {
        ElMessage.success('批量购买成功')
        // 从购物车中移除已购买的商品
        selectedItems.value.forEach(item => handleRemove(item))
        // 刷新用户信息
        user.value.balance -= totalAmount
        localStorage.setItem('userInfo', JSON.stringify(user.value))
      })
    }).catch(() => {
      ElMessage.error('批量购买失败')
    })
  })
}

// 从购物车中移除
const handleRemove = (item) => {
  ElMessageBox.confirm(
    `确认从购物车中移除课程"${item.courseName}"？`,
    '移除确认',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    request.delete(`/cart/remove/${item.cartId}`).then(res => {
      if (res.code === '200') {
        ElMessage.success('移除成功')
        // 重新获取购物车列表
        getCartList()
      } else {
        ElMessage.error(res.msg || '移除失败')
      }
    }).catch(error => {
      console.error('移除失败:', error)
      ElMessage.error('移除失败，请稍后重试')
    })
  }).catch(() => {
    // 用户取消操作
  })
}

// 批量移除
const handleBatchRemove = () => {
  if (selectedItems.value.length === 0) {
    ElMessage.warning('请选择要移除的课程')
    return
  }

  ElMessageBox.confirm(
    `确认从购物车中移除选中的 ${selectedItems.value.length} 个课程？`,
    '批量移除确认',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    // 批量删除
    const promises = selectedItems.value.map(item => 
      request.delete(`/cart/remove/${item.cartId}`)
    )

    Promise.all(promises).then(() => {
      ElMessage.success('批量移除成功')
      // 重新获取购物车列表
      getCartList()
    }).catch(error => {
      console.error('批量移除失败:', error)
      ElMessage.error('批量移除失败，请稍后重试')
    })
  }).catch(() => {
    // 用户取消操作
  })
}

onMounted(() => {
  getCartList()
})
</script>

<style scoped>
.cart-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.cart-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
}

.cart-list {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.cart-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.cart-total {
  margin-right: 20px;
  font-size: 16px;
}

.total-price {
  color: #e74c3c;
  font-size: 20px;
  font-weight: bold;
}

.empty-cart {
  text-align: center;
  padding: 40px 0;
}

.cart-actions {
  display: flex;
  gap: 10px;
}
</style>