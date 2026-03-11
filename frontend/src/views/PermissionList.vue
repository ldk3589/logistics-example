<template>
  <el-card>
    <template #header>
      <div class="header-row">
        <span>权限管理</span>
        <el-button v-if="canAdd" type="primary" @click="openAddDialog">新增权限</el-button>
      </div>
    </template>

    <div class="toolbar">
      <el-input v-model="query.permissionName" placeholder="权限名称" clearable style="width: 180px" />
      <el-input v-model="query.permissionCode" placeholder="权限编码" clearable style="width: 180px" />
      <el-select v-model="query.status" placeholder="状态" clearable style="width: 120px">
        <el-option label="启用" :value="1" />
        <el-option label="禁用" :value="0" />
      </el-select>
      <el-button type="primary" @click="handleSearch">查询</el-button>
      <el-button @click="handleReset">重置</el-button>
    </div>

    <el-table v-loading="loading" :data="tableData" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="permissionName" label="权限名称" min-width="140" />
      <el-table-column prop="permissionCode" label="权限编码" min-width="180" />
      <el-table-column prop="permissionType" label="权限类型" width="120" />
      <el-table-column prop="httpMethod" label="请求方式" width="100" />
      <el-table-column prop="apiUri" label="接口地址" min-width="220" />
      <el-table-column label="状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="160" fixed="right">
        <template #default="scope">
          <el-button
            v-if="canUpdate"
            link
            type="primary"
            @click="openEditDialog(scope.row)"
          >
            编辑
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

    <!-- 新增权限 -->
    <el-dialog v-model="addDialogVisible" title="新增权限" width="560px">
      <el-form :model="addForm" label-width="90px">
        <el-form-item label="权限名称">
          <el-input v-model="addForm.permissionName" />
        </el-form-item>
        <el-form-item label="权限编码">
          <el-input v-model="addForm.permissionCode" />
        </el-form-item>
        <el-form-item label="接口地址">
          <el-input v-model="addForm.apiUri" />
        </el-form-item>
        <el-form-item label="请求方式">
          <el-select v-model="addForm.httpMethod" style="width: 100%">
            <el-option label="GET" value="GET" />
            <el-option label="POST" value="POST" />
            <el-option label="PUT" value="PUT" />
            <el-option label="DELETE" value="DELETE" />
          </el-select>
        </el-form-item>
        <el-form-item label="权限类型">
          <el-select v-model="addForm.permissionType" style="width: 100%">
            <el-option label="API" value="API" />
            <el-option label="BUTTON" value="BUTTON" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="addForm.status" style="width: 100%">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="addForm.remark" type="textarea" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAdd">确定</el-button>
      </template>
    </el-dialog>

    <!-- 编辑权限 -->
    <el-dialog v-model="editDialogVisible" title="编辑权限" width="560px">
      <el-form :model="editForm" label-width="90px">
        <el-form-item label="权限名称">
          <el-input v-model="editForm.permissionName" />
        </el-form-item>
        <el-form-item label="权限编码">
          <el-input v-model="editForm.permissionCode" />
        </el-form-item>
        <el-form-item label="接口地址">
          <el-input v-model="editForm.apiUri" />
        </el-form-item>
        <el-form-item label="请求方式">
          <el-select v-model="editForm.httpMethod" style="width: 100%">
            <el-option label="GET" value="GET" />
            <el-option label="POST" value="POST" />
            <el-option label="PUT" value="PUT" />
            <el-option label="DELETE" value="DELETE" />
          </el-select>
        </el-form-item>
        <el-form-item label="权限类型">
          <el-select v-model="editForm.permissionType" style="width: 100%">
            <el-option label="API" value="API" />
            <el-option label="BUTTON" value="BUTTON" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="editForm.status" style="width: 100%">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="editForm.remark" type="textarea" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEdit">保存</el-button>
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
const editDialogVisible = ref(false)

const canAdd = computed(() => hasPermission('sys:permission:add'))
const canUpdate = computed(() => hasPermission('sys:permission:update'))
const canDelete = computed(() => hasPermission('sys:permission:delete'))

const query = reactive({
  pageNum: 1,
  pageSize: 10,
  permissionName: '',
  permissionCode: '',
  status: null
})

const addForm = reactive({
  permissionName: '',
  permissionCode: '',
  apiUri: '',
  httpMethod: 'GET',
  permissionType: 'API',
  status: 1,
  remark: ''
})

const editForm = reactive({
  id: null,
  permissionName: '',
  permissionCode: '',
  apiUri: '',
  httpMethod: 'GET',
  permissionType: 'API',
  status: 1,
  remark: ''
})

async function loadData() {
  loading.value = true
  try {
    const res = await request.get('/api/permissions', { params: query })
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
  query.permissionName = ''
  query.permissionCode = ''
  query.status = null
  loadData()
}

function resetAddForm() {
  addForm.permissionName = ''
  addForm.permissionCode = ''
  addForm.apiUri = ''
  addForm.httpMethod = 'GET'
  addForm.permissionType = 'API'
  addForm.status = 1
  addForm.remark = ''
}

function openAddDialog() {
  resetAddForm()
  addDialogVisible.value = true
}

async function submitAdd() {
  await request.post('/api/permissions', addForm)
  ElMessage.success('新增权限成功')
  addDialogVisible.value = false
  loadData()
}

async function openEditDialog(row) {
  const res = await request.get(`/api/permissions/${row.id}`)
  const data = res.data

  editForm.id = data.id
  editForm.permissionName = data.permissionName || ''
  editForm.permissionCode = data.permissionCode || ''
  editForm.apiUri = data.apiUri || ''
  editForm.httpMethod = data.httpMethod || 'GET'
  editForm.permissionType = data.permissionType || 'API'
  editForm.status = data.status
  editForm.remark = data.remark || ''

  editDialogVisible.value = true
}

async function submitEdit() {
  await request.put(`/api/permissions/${editForm.id}`, {
    permissionName: editForm.permissionName,
    permissionCode: editForm.permissionCode,
    apiUri: editForm.apiUri,
    httpMethod: editForm.httpMethod,
    permissionType: editForm.permissionType,
    status: editForm.status,
    remark: editForm.remark
  })
  ElMessage.success('修改权限成功')
  editDialogVisible.value = false
  loadData()
}

async function handleDelete(row) {
  await ElMessageBox.confirm(
    `确认删除权限【${row.permissionName}】吗？`,
    '提示',
    { type: 'warning' }
  )
  await request.delete(`/api/permissions/${row.id}`)
  ElMessage.success('删除权限成功')
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