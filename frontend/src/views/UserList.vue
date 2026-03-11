<template>
  <el-card>
    <template #header>
      <div class="header-row">
        <span>用户管理</span>
        <el-button v-if="canAdd" type="primary" @click="openAddDialog">新增用户</el-button>
      </div>
    </template>

    <div class="toolbar">
      <el-input v-model="query.username" placeholder="用户名" clearable style="width: 180px" />
      <el-input v-model="query.nickname" placeholder="昵称" clearable style="width: 180px" />
      <el-select v-model="query.status" placeholder="状态" clearable style="width: 120px">
        <el-option label="启用" :value="1" />
        <el-option label="禁用" :value="0" />
      </el-select>
      <el-button type="primary" @click="handleSearch">查询</el-button>
      <el-button @click="handleReset">重置</el-button>
    </div>

    <el-table v-loading="loading" :data="tableData" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" min-width="140" />
      <el-table-column prop="nickname" label="昵称" min-width="120" />
      <el-table-column prop="realName" label="真实姓名" min-width="120" />
      <el-table-column prop="phone" label="手机号" min-width="140" />
      <el-table-column prop="email" label="邮箱" min-width="180" />
      <el-table-column prop="deptId" label="部门ID" width="100" />

      <el-table-column label="状态" width="120">
        <template #default="scope">
          <el-switch
            v-if="canUpdate"
            :model-value="scope.row.status === 1"
            @change="val => handleChangeStatus(scope.row, val)"
          />
          <el-tag v-else :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="260" fixed="right">
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
            v-if="canAssignRole"
            link
            type="primary"
            @click="openRoleDialog(scope.row)"
          >
            分配角色
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

    <!-- 新增用户 -->
    <el-dialog v-model="addDialogVisible" title="新增用户" width="520px">
      <el-form :model="addForm" label-width="90px">
        <el-form-item label="用户名">
          <el-input v-model="addForm.username" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="addForm.password" type="password" show-password />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="addForm.nickname" />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="addForm.realName" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="addForm.phone" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="addForm.email" />
        </el-form-item>
        <el-form-item label="部门ID">
          <el-input v-model.number="addForm.deptId" />
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

    <!-- 编辑用户 -->
    <el-dialog v-model="editDialogVisible" title="编辑用户" width="520px">
      <el-form :model="editForm" label-width="90px">
        <el-form-item label="用户名">
          <el-input v-model="editForm.username" disabled />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="editForm.nickname" />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="editForm.realName" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="editForm.phone" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="editForm.email" />
        </el-form-item>
        <el-form-item label="部门ID">
          <el-input v-model.number="editForm.deptId" />
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

    <!-- 分配角色 -->
    <el-dialog v-model="roleDialogVisible" title="分配角色" width="520px">
      <div style="margin-bottom: 16px;">
        当前用户：<b>{{ currentUser.username || '-' }}</b>
      </div>

      <el-checkbox-group v-model="selectedRoleIds">
        <div v-for="item in roleOptions" :key="item.id" style="margin-bottom: 10px;">
          <el-checkbox :label="item.id">
            {{ item.roleName }}（{{ item.roleCode }}）
          </el-checkbox>
        </div>
      </el-checkbox-group>

      <template #footer>
        <el-button @click="roleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAssignRoles">保存</el-button>
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
const roleDialogVisible = ref(false)

const roleOptions = ref([])
const selectedRoleIds = ref([])

const currentUser = reactive({
  id: null,
  username: ''
})

const canAdd = computed(() => hasPermission('sys:user:add'))
const canUpdate = computed(() => hasPermission('sys:user:update'))
const canDelete = computed(() => hasPermission('sys:user:delete'))
const canAssignRole = computed(() => hasPermission('sys:user:update'))

const query = reactive({
  pageNum: 1,
  pageSize: 10,
  username: '',
  nickname: '',
  status: null
})

const addForm = reactive({
  username: '',
  password: '',
  nickname: '',
  realName: '',
  phone: '',
  email: '',
  deptId: 2,
  status: 1,
  remark: ''
})

const editForm = reactive({
  id: null,
  username: '',
  nickname: '',
  realName: '',
  phone: '',
  email: '',
  deptId: 2,
  status: 1,
  remark: ''
})

async function loadData() {
  loading.value = true
  try {
    const res = await request.get('/api/users', { params: query })
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
  query.username = ''
  query.nickname = ''
  query.status = null
  loadData()
}

function resetAddForm() {
  addForm.username = ''
  addForm.password = ''
  addForm.nickname = ''
  addForm.realName = ''
  addForm.phone = ''
  addForm.email = ''
  addForm.deptId = 2
  addForm.status = 1
  addForm.remark = ''
}

function openAddDialog() {
  resetAddForm()
  addDialogVisible.value = true
}

async function submitAdd() {
  await request.post('/api/users', addForm)
  ElMessage.success('新增用户成功')
  addDialogVisible.value = false
  loadData()
}

async function openEditDialog(row) {
  const res = await request.get(`/api/users/${row.id}`)
  const data = res.data

  editForm.id = data.id
  editForm.username = data.username
  editForm.nickname = data.nickname || ''
  editForm.realName = data.realName || ''
  editForm.phone = data.phone || ''
  editForm.email = data.email || ''
  editForm.deptId = data.deptId || 2
  editForm.status = data.status
  editForm.remark = data.remark || ''

  editDialogVisible.value = true
}

async function submitEdit() {
  await request.put(`/api/users/${editForm.id}`, {
    nickname: editForm.nickname,
    realName: editForm.realName,
    phone: editForm.phone,
    email: editForm.email,
    deptId: editForm.deptId,
    status: editForm.status,
    remark: editForm.remark
  })
  ElMessage.success('修改用户成功')
  editDialogVisible.value = false
  loadData()
}

async function handleDelete(row) {
  await ElMessageBox.confirm(
    `确认删除用户【${row.username}】吗？`,
    '提示',
    { type: 'warning' }
  )
  await request.delete(`/api/users/${row.id}`)
  ElMessage.success('删除用户成功')
  loadData()
}

async function handleChangeStatus(row, value) {
  const status = value ? 1 : 0
  await request.put(`/api/users/${row.id}/status`, { status })
  ElMessage.success('修改状态成功')
  loadData()
}

async function loadRoleOptions() {
  const res = await request.get('/api/roles', {
    params: {
      pageNum: 1,
      pageSize: 100
    }
  })

  if (Array.isArray(res.data)) {
    roleOptions.value = res.data
  } else {
    roleOptions.value = res.data?.records || []
  }
}

async function openRoleDialog(row) {
  if (!canAssignRole.value) {
    ElMessage.error('没有权限执行此操作')
    return
  }

  currentUser.id = row.id
  currentUser.username = row.username

  await loadRoleOptions()

  const res = await request.get(`/api/users/${row.id}/roles`)
  selectedRoleIds.value = res.data || []

  roleDialogVisible.value = true
}

async function submitAssignRoles() {
  if (!canAssignRole.value) {
    ElMessage.error('没有权限执行此操作')
    return
  }

  await request.post(`/api/users/${currentUser.id}/roles`, {
    roleIds: selectedRoleIds.value
  })
  ElMessage.success('分配角色成功')
  roleDialogVisible.value = false
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