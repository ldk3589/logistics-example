<template>
  <el-card>
    <template #header>
      <div class="header-row">
        <span>部门管理</span>
        <div>
          <el-button v-if="canAdd" type="primary" @click="openAddDialog()">新增根部门</el-button>
          <el-button @click="loadTree">刷新</el-button>
        </div>
      </div>
    </template>

    <el-table
      v-loading="loading"
      :data="tableData"
      row-key="id"
      border
      default-expand-all
      :tree-props="{ children: 'children' }"
    >
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="deptName" label="部门名称" min-width="180" />
      <el-table-column prop="deptCode" label="部门编码" min-width="160" />
      <el-table-column prop="parentId" label="父部门ID" width="100" />
      <el-table-column prop="leaderUserId" label="负责人ID" width="110" />
      <el-table-column prop="sortNum" label="排序" width="80" />

      <el-table-column label="状态" width="90">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="remark" label="备注" min-width="180" />

      <el-table-column label="操作" width="220" fixed="right">
        <template #default="scope">
          <el-button
            v-if="canAdd"
            link
            type="primary"
            @click="openAddDialog(scope.row)"
          >
            新增子部门
          </el-button>

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

    <el-dialog v-model="addDialogVisible" title="新增部门" width="560px">
      <el-form :model="addForm" label-width="100px">
        <el-form-item label="父部门ID">
          <el-input v-model.number="addForm.parentId" />
        </el-form-item>

        <el-form-item label="部门名称">
          <el-input v-model="addForm.deptName" />
        </el-form-item>

        <el-form-item label="部门编码">
          <el-input v-model="addForm.deptCode" />
        </el-form-item>

        <el-form-item label="负责人ID">
          <el-input v-model.number="addForm.leaderUserId" />
        </el-form-item>

        <el-form-item label="排序">
          <el-input v-model.number="addForm.sortNum" />
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

    <el-dialog v-model="editDialogVisible" title="编辑部门" width="560px">
      <el-form :model="editForm" label-width="100px">
        <el-form-item label="父部门ID">
          <el-input v-model.number="editForm.parentId" />
        </el-form-item>

        <el-form-item label="部门名称">
          <el-input v-model="editForm.deptName" />
        </el-form-item>

        <el-form-item label="部门编码">
          <el-input v-model="editForm.deptCode" />
        </el-form-item>

        <el-form-item label="负责人ID">
          <el-input v-model.number="editForm.leaderUserId" />
        </el-form-item>

        <el-form-item label="排序">
          <el-input v-model.number="editForm.sortNum" />
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

const addDialogVisible = ref(false)
const editDialogVisible = ref(false)

const canAdd = computed(() => hasPermission('sys:dept:add'))
const canUpdate = computed(() => hasPermission('sys:dept:update'))
const canDelete = computed(() => hasPermission('sys:dept:delete'))

const addForm = reactive({
  parentId: 0,
  deptName: '',
  deptCode: '',
  leaderUserId: null,
  sortNum: 0,
  status: 1,
  remark: ''
})

const editForm = reactive({
  id: null,
  parentId: 0,
  deptName: '',
  deptCode: '',
  leaderUserId: null,
  sortNum: 0,
  status: 1,
  remark: ''
})

async function loadTree() {
  loading.value = true
  try {
    const res = await request.get('/api/depts')

    // 兼容两种返回：
    // 1. 后端直接返回树数组
    // 2. 后端返回 { records: [...] }
    if (Array.isArray(res.data)) {
      tableData.value = res.data
    } else {
      tableData.value = res.data?.records || []
    }
  } finally {
    loading.value = false
  }
}

function resetAddForm() {
  addForm.parentId = 0
  addForm.deptName = ''
  addForm.deptCode = ''
  addForm.leaderUserId = null
  addForm.sortNum = 0
  addForm.status = 1
  addForm.remark = ''
}

function openAddDialog(row) {
  resetAddForm()
  if (row && row.id) {
    addForm.parentId = row.id
  }
  addDialogVisible.value = true
}

async function submitAdd() {
  await request.post('/api/depts', addForm)
  ElMessage.success('新增部门成功')
  addDialogVisible.value = false
  loadTree()
}

async function openEditDialog(row) {
  const res = await request.get(`/api/depts/${row.id}`)
  const data = res.data

  editForm.id = data.id
  editForm.parentId = data.parentId ?? 0
  editForm.deptName = data.deptName || ''
  editForm.deptCode = data.deptCode || ''
  editForm.leaderUserId = data.leaderUserId
  editForm.sortNum = data.sortNum ?? 0
  editForm.status = data.status ?? 1
  editForm.remark = data.remark || ''

  editDialogVisible.value = true
}

async function submitEdit() {
  await request.put(`/api/depts/${editForm.id}`, {
    parentId: editForm.parentId,
    deptName: editForm.deptName,
    deptCode: editForm.deptCode,
    leaderUserId: editForm.leaderUserId,
    sortNum: editForm.sortNum,
    status: editForm.status,
    remark: editForm.remark
  })
  ElMessage.success('修改部门成功')
  editDialogVisible.value = false
  loadTree()
}

async function handleDelete(row) {
  await ElMessageBox.confirm(
    `确认删除部门【${row.deptName}】吗？`,
    '提示',
    { type: 'warning' }
  )
  await request.delete(`/api/depts/${row.id}`)
  ElMessage.success('删除部门成功')
  loadTree()
}

onMounted(loadTree)
</script>

<style scoped>
.header-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>