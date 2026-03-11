<template>
  <el-card>
    <template #header>
      <div class="header-row">
        <span>订单管理</span>
        <el-button v-if="canAdd" type="primary" @click="openAddDialog">新增订单</el-button>
      </div>
    </template>

    <div class="toolbar">
      <el-input v-model="query.orderNo" placeholder="订单号" clearable style="width: 180px" />
      <el-input v-model="query.customerName" placeholder="客户名称" clearable style="width: 180px" />
      <el-input v-model="query.status" placeholder="状态" clearable style="width: 140px" />
      <el-button type="primary" @click="handleSearch">查询</el-button>
      <el-button @click="handleReset">重置</el-button>
    </div>

    <el-table v-loading="loading" :data="tableData" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="orderNo" label="订单号" min-width="180" />
      <el-table-column prop="customerName" label="客户名称" min-width="140" />
      <el-table-column prop="customerPhone" label="客户电话" min-width="140" />
      <el-table-column prop="sourceAddress" label="发货地址" min-width="180" />
      <el-table-column prop="targetAddress" label="收货地址" min-width="180" />
      <el-table-column prop="status" label="状态" width="120" />
      <el-table-column prop="amount" label="金额" width="120" />
      <el-table-column prop="deptId" label="部门ID" width="100" />
      <el-table-column prop="ownerUserId" label="负责人ID" width="110" />

      <el-table-column label="操作" width="220" fixed="right">
        <template #default="scope">
          <el-button
            v-if="canAssign"
            link
            type="primary"
            @click="openAssignDialog(scope.row)"
          >
            分配
          </el-button>

          <el-button
            v-if="canComplete"
            link
            type="success"
            @click="handleComplete(scope.row)"
          >
            完成
          </el-button>

          <el-button
            v-if="canDelete"
            link
            type="danger"
            @click="handleDelete(scope.row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-wrap">
      <el-pagination
        v-model:current-page="query.pageNum"
        v-model:page-size="query.pageSize"
        background
        layout="total, sizes, prev, pager, next"
        :page-sizes="[10, 20, 50]"
        :total="total"
        @size-change="loadData"
        @current-change="loadData"
      />
    </div>

    <!-- 新增订单 -->
    <el-dialog v-model="addDialogVisible" title="新增订单" width="560px">
      <el-form :model="addForm" label-width="90px">
        <el-form-item label="订单号">
          <el-input v-model="addForm.orderNo" />
        </el-form-item>
        <el-form-item label="客户名称">
          <el-input v-model="addForm.customerName" />
        </el-form-item>
        <el-form-item label="客户电话">
          <el-input v-model="addForm.customerPhone" />
        </el-form-item>
        <el-form-item label="发货地址">
          <el-input v-model="addForm.sourceAddress" />
        </el-form-item>
        <el-form-item label="收货地址">
          <el-input v-model="addForm.targetAddress" />
        </el-form-item>
        <el-form-item label="金额">
          <el-input v-model.number="addForm.amount" />
        </el-form-item>
        <el-form-item label="部门ID">
          <el-input v-model.number="addForm.deptId" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAdd">确定</el-button>
      </template>
    </el-dialog>

    <!-- 分配订单 -->
    <el-dialog v-model="assignDialogVisible" title="分配订单" width="520px">
      <div style="margin-bottom: 16px;">
        当前订单：<b>{{ currentOrder.orderNo || '-' }}</b>
      </div>

      <el-form :model="assignForm" label-width="90px">
        <el-form-item label="负责人ID">
          <el-input v-model.number="assignForm.ownerUserId" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="assignDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAssign">保存</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../utils/request'
import { hasPermission } from '../utils/auth'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)

const addDialogVisible = ref(false)
const assignDialogVisible = ref(false)

const currentOrder = reactive({
  id: null,
  orderNo: ''
})

const canAdd = computed(() => hasPermission('order:add'))
const canDelete = computed(() => hasPermission('order:delete'))
const canAssign = computed(() => hasPermission('order:assign'))
const canComplete = computed(() => hasPermission('order:update'))

const query = reactive({
  pageNum: 1,
  pageSize: 10,
  orderNo: '',
  customerName: '',
  status: ''
})

const addForm = reactive({
  orderNo: '',
  customerName: '',
  customerPhone: '',
  sourceAddress: '',
  targetAddress: '',
  amount: 0,
  deptId: 2
})

const assignForm = reactive({
  ownerUserId: null
})

async function loadData() {
  loading.value = true
  try {
    const res = await request.get('/api/orders', { params: query })
    if (Array.isArray(res.data)) {
      tableData.value = res.data
      total.value = res.data.length
    } else {
      tableData.value = res.data?.records || []
      total.value = res.data?.total || 0
    }
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  query.pageNum = 1
  loadData()
}

function handleReset() {
  query.pageNum = 1
  query.pageSize = 10
  query.orderNo = ''
  query.customerName = ''
  query.status = ''
  loadData()
}

function resetAddForm() {
  addForm.orderNo = ''
  addForm.customerName = ''
  addForm.customerPhone = ''
  addForm.sourceAddress = ''
  addForm.targetAddress = ''
  addForm.amount = 0
  addForm.deptId = 2
}

function openAddDialog() {
  resetAddForm()
  addDialogVisible.value = true
}

async function submitAdd() {
  await request.post('/api/orders', addForm)
  ElMessage.success('新增订单成功')
  addDialogVisible.value = false
  loadData()
}

function openAssignDialog(row) {
  currentOrder.id = row.id
  currentOrder.orderNo = row.orderNo
  assignForm.ownerUserId = row.ownerUserId || null
  assignDialogVisible.value = true
}

async function submitAssign() {
  await request.post(`/api/orders/${currentOrder.id}/assign`, {
    ownerUserId: assignForm.ownerUserId
  })
  ElMessage.success('分配订单成功')
  assignDialogVisible.value = false
  loadData()
}

async function handleComplete(row) {
  await ElMessageBox.confirm(
    `确认完成订单【${row.orderNo}】吗？`,
    '提示',
    { type: 'warning' }
  )
  await request.post(`/api/orders/${row.id}/complete`)
  ElMessage.success('完成订单成功')
  loadData()
}

async function handleDelete(row) {
  await ElMessageBox.confirm(
    `确认删除订单【${row.orderNo}】吗？`,
    '提示',
    { type: 'warning' }
  )
  await request.delete(`/api/orders/${row.id}`)
  ElMessage.success('删除订单成功')
  loadData()
}

onMounted(loadData)
</script>

<style scoped>
.header-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.toolbar {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  margin-bottom: 16px;
}
.pagination-wrap {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}
</style>