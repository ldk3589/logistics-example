<template>
  <div class="order-container">
    <el-card>
      <template #header>
        <div class="header">
          <span class="title">订单状态管理</span>
          <div class="right">
            <el-tag type="info">{{ roleLabel }}</el-tag>

            <el-button
              
              type="primary"
              style="margin-left: 12px"
              @click="showCreateDialog = true"
            >
              新建订单
            </el-button>
          </div>
        </div>
      </template>

      <el-table :data="orders" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />

        <el-table-column prop="amount" label="金额">
          <template #default="{ row }">￥{{ row.amount }}</template>
        </el-table-column>

        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="statusMap[row.status]?.type">
              {{ statusMap[row.status]?.label }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column 
        v-if="['ADMIN_L1'].includes(userRole)"
        prop="adminL2Name" label="二级管理员">
          <template #default="{ row }">
            {{ row.adminL2Name || '未指派' }}
          </template>
        </el-table-column>

        <el-table-column 
          v-if="['ADMIN_L1','ADMIN_L2'].includes(userRole)"
          prop="userName" 
          label="下单客户">
          <template #default="{ row }">
            {{ row.userName || '未知用户' }}
          </template>
        </el-table-column>

        <el-table-column label="操作" width="360">
          <template #default="{ row }">
            <!-- 一级管理员指派 -->
            <el-button
              v-if="userRole === 'ADMIN_L1' && row.status === 'CREATED'"
              type="success"
              size="small"
              @click="openAssignDialog(row)"
            >
              指派
            </el-button>

            <!-- 管理员修改状态 -->
            <el-select
              v-if="userRole !== 'USER'"
              :model-value="row.status"
              size="small"
              style="width: 120px; margin: 0 10px"
              @change="val => updateStatus(row.id, val)"
            >
              <el-option label="处理中" value="PROCESSING" />
              <el-option label="已完成" value="COMPLETED" />
              <el-option label="已取消" value="CANCELLED" />
            </el-select>

            <!-- 一级管理员删除 -->
            <el-button
              v-if="['ADMIN_L1','ADMIN_L2','USER'].includes(userRole)"
              type="danger"
              size="small"
              @click="handleDelete(row.id)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新建订单 -->
    <el-dialog v-model="showCreateDialog" title="新建订单" width="480px">
      <el-form :model="createForm" label-width="100px">
        <el-form-item label="订单金额">
          <el-input-number v-model="createForm.amount" :min="1" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="createForm.phone" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="createForm.email" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" @click="submitCreate">提交</el-button>
      </template>
    </el-dialog>

    <!-- 指派二级管理员 -->
    <el-dialog v-model="showAssignDialog" title="指派二级管理员" width="400px">
      <el-select v-model="assignAdminL2Id" style="width:100%" placeholder="选择管理员">
        <el-option
          v-for="admin in adminL2List"
          :key="admin.id"
          :label="admin.username"
          :value="admin.id"
        />
      </el-select>

      <template #footer>
        <el-button @click="showAssignDialog = false">取消</el-button>
        <el-button type="primary" @click="confirmAssign">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { jwtDecode } from 'jwt-decode'

import {
  getOrderListApi,
  createOrderApi,
  deleteOrderApi,
  updateOrderStatusApi,
  assignOrderApi,
  getAdminL2ListApi
} from '@/api/order'

/* ===== JWT & 身份 ===== */
const token = localStorage.getItem('token')
if (!token) {
  ElMessage.error('未登录')
  throw new Error('No token')
}

const payload = jwtDecode(token)
const userRole = ref(payload.role)
const userId = payload.userId

const roleLabelMap = {
  USER: '普通用户',
  ADMIN_L2: '二级管理员',
  ADMIN_L1: '一级管理员'
}
const roleLabel = computed(() => roleLabelMap[userRole.value])

/* ===== 数据 ===== */
const orders = ref([])
const adminL2List = ref([])
const loading = ref(false)

/* ===== 状态 ===== */
const showCreateDialog = ref(false)
const showAssignDialog = ref(false)
const currentOrderId = ref(null)
const assignAdminL2Id = ref(null)

/* ===== 表单 ===== */
const createForm = reactive({
  amount: 1,
  phone: '',
  email: ''
})

const statusMap = {
  CREATED: { label: '已创建', type: 'info' },
  PROCESSING: { label: '处理中', type: 'warning' },
  COMPLETED: { label: '已完成', type: 'success' },
  CANCELLED: { label: '已取消', type: 'danger' }
}

/* ===== 方法 ===== */
const loadOrders = async () => {
  loading.value = true
  orders.value = await getOrderListApi()
  loading.value = false
}

const submitCreate = async () => {
  await createOrderApi(createForm)
  ElMessage.success('订单创建成功')
  showCreateDialog.value = false
  loadOrders()
}

const updateStatus = async (id, status) => {
  await updateOrderStatusApi(id, status)
  ElMessage.success('状态已更新')
  loadOrders()
}

const handleDelete = async (id) => {
  await ElMessageBox.confirm('确定删除该订单吗？', '警告', { type: 'warning' })
  await deleteOrderApi(id)
  ElMessage.success('删除成功')
  loadOrders()
}

const openAssignDialog = async (row) => {
  currentOrderId.value = row.id
  adminL2List.value = await getAdminL2ListApi()
  assignAdminL2Id.value = null
  showAssignDialog.value = true
}

const confirmAssign = async () => {
  await assignOrderApi(currentOrderId.value, assignAdminL2Id.value)
  ElMessage.success('指派成功')
  showAssignDialog.value = false
  loadOrders()
}

onMounted(loadOrders)
</script>

<style scoped>
.order-container {
  padding: 20px;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.title {
  font-size: 18px;
  font-weight: bold;
}
.right {
  display: flex;
  align-items: center;
}
</style>
