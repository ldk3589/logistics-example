<template>
  <el-card>
    <template #header>
      <div class="header-row">
        <span>角色管理</span>
        <el-button v-if="canAdd" type="primary" @click="openAddDialog">新增角色</el-button>
      </div>
    </template>

    <div class="toolbar">
      <el-input v-model="query.roleName" placeholder="角色名称" clearable style="width: 180px" />
      <el-input v-model="query.roleCode" placeholder="角色编码" clearable style="width: 180px" />
      <el-select v-model="query.status" placeholder="状态" clearable style="width: 120px">
        <el-option label="启用" :value="1" />
        <el-option label="禁用" :value="0" />
      </el-select>
      <el-button type="primary" @click="handleSearch">查询</el-button>
      <el-button @click="handleReset">重置</el-button>
    </div>

    <el-table v-loading="loading" :data="tableData" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="roleName" label="角色名称" min-width="140" />
      <el-table-column prop="roleCode" label="角色编码" min-width="160" />
      <el-table-column prop="dataScope" label="数据范围" min-width="150" />
      <el-table-column prop="sortNum" label="排序" width="90" />
      <el-table-column label="状态" width="100">
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
            v-if="canAssign"
            link
            type="primary"
            @click="openPermissionDialog(scope.row)"
          >
            分配权限
          </el-button>

          <el-button
            v-if="canAssign"
            link
            type="primary"
            @click="openMenuDialog(scope.row)"
          >
            分配菜单
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

    <!-- 新增角色 -->
    <el-dialog v-model="dialogVisible" title="新增角色" width="520px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="角色名称">
          <el-input v-model="form.roleName" />
        </el-form-item>
        <el-form-item label="角色编码">
          <el-input v-model="form.roleCode" />
        </el-form-item>
        <el-form-item label="数据范围">
          <el-select v-model="form.dataScope" style="width: 100%">
            <el-option label="全部数据" value="ALL" />
            <el-option label="本部门" value="DEPT" />
            <el-option label="本部门及子部门" value="DEPT_AND_CHILD" />
            <el-option label="仅本人" value="SELF" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="排序">
          <el-input v-model.number="form.sortNum" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAdd">确定</el-button>
      </template>
    </el-dialog>

    <!-- 分配权限 -->
    <el-dialog v-model="permissionDialogVisible" title="分配权限" width="560px">
      <div style="margin-bottom: 16px;">
        当前角色：<b>{{ currentRole.roleName || '-' }}</b>
      </div>

      <el-checkbox-group v-model="selectedPermissionIds">
        <div v-for="item in permissionOptions" :key="item.id" style="margin-bottom: 10px;">
          <el-checkbox :label="item.id">
            {{ item.permissionName }}（{{ item.permissionCode }}）
          </el-checkbox>
        </div>
      </el-checkbox-group>

      <template #footer>
        <el-button @click="permissionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAssignPermissions">保存</el-button>
      </template>
    </el-dialog>

    <!-- 分配菜单 -->
    <el-dialog v-model="menuDialogVisible" title="分配菜单" width="640px">
      <div style="margin-bottom: 16px;">
        当前角色：<b>{{ currentRole.roleName || '-' }}</b>
      </div>

      <el-tree
        ref="menuTreeRef"
        :data="menuOptions"
        node-key="id"
        show-checkbox
        default-expand-all
        :props="treeProps"
      />

      <template #footer>
        <el-button @click="menuDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAssignMenus">保存</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { computed, nextTick, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../utils/request'
import { hasPermission } from '../utils/auth'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)

const permissionDialogVisible = ref(false)
const menuDialogVisible = ref(false)

const permissionOptions = ref([])
const menuOptions = ref([])
const selectedPermissionIds = ref([])

const menuTreeRef = ref()
const treeProps = {
  children: 'children',
  label: 'menuName'
}

const currentRole = reactive({
  id: null,
  roleName: ''
})

const canAdd = computed(() => hasPermission('sys:role:add'))
const canDelete = computed(() => hasPermission('sys:role:delete'))
const canAssign = computed(() => hasPermission('sys:role:update'))

const query = reactive({
  pageNum: 1,
  pageSize: 10,
  roleName: '',
  roleCode: '',
  status: null
})

const form = reactive({
  roleName: '',
  roleCode: '',
  dataScope: 'SELF',
  status: 1,
  sortNum: 0,
  remark: ''
})

async function loadData() {
  loading.value = true
  try {
    const res = await request.get('/api/roles', { params: query })
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
  query.roleName = ''
  query.roleCode = ''
  query.status = null
  loadData()
}

function resetForm() {
  form.roleName = ''
  form.roleCode = ''
  form.dataScope = 'SELF'
  form.status = 1
  form.sortNum = 0
  form.remark = ''
}

function openAddDialog() {
  resetForm()
  dialogVisible.value = true
}

async function submitAdd() {
  await request.post('/api/roles', form)
  ElMessage.success('新增角色成功')
  dialogVisible.value = false
  loadData()
}

async function handleDelete(row) {
  await ElMessageBox.confirm(
    `确认删除角色【${row.roleName}】吗？`,
    '提示',
    { type: 'warning' }
  )
  await request.delete(`/api/roles/${row.id}`)
  ElMessage.success('删除角色成功')
  loadData()
}

async function loadPermissionOptions() {
  const res = await request.get('/api/permissions', {
    params: {
      pageNum: 1,
      pageSize: 200
    }
  })

  if (Array.isArray(res.data)) {
    permissionOptions.value = res.data
  } else {
    permissionOptions.value = res.data?.records || []
  }
}

async function openPermissionDialog(row) {
  if (!canAssign.value) {
    ElMessage.error('没有权限执行此操作')
    return
  }

  currentRole.id = row.id
  currentRole.roleName = row.roleName

  await loadPermissionOptions()

  const res = await request.get(`/api/roles/${row.id}/permissions`)
  selectedPermissionIds.value = res.data || []

  permissionDialogVisible.value = true
}

async function submitAssignPermissions() {
  if (!canAssign.value) {
    ElMessage.error('没有权限执行此操作')
    return
  }

  await request.post(`/api/roles/${currentRole.id}/permissions`, {
    permissionIds: selectedPermissionIds.value
  })
  ElMessage.success('分配权限成功')
  permissionDialogVisible.value = false
}

async function loadMenuOptions() {
  const res = await request.get('/api/menus/tree')
  menuOptions.value = res.data || []
}

async function openMenuDialog(row) {
  if (!canAssign.value) {
    ElMessage.error('没有权限执行此操作')
    return
  }

  currentRole.id = row.id
  currentRole.roleName = row.roleName

  await loadMenuOptions()

  menuDialogVisible.value = true

  await nextTick()

  if (menuTreeRef.value) {
    menuTreeRef.value.setCheckedKeys([])
  }

  const res = await request.get(`/api/roles/${row.id}/menus`)
  const checkedKeys = res.data || []

  await nextTick()
  if (menuTreeRef.value) {
    menuTreeRef.value.setCheckedKeys(checkedKeys)
  }
}

async function submitAssignMenus() {
  if (!canAssign.value) {
    ElMessage.error('没有权限执行此操作')
    return
  }

  const checkedKeys = menuTreeRef.value ? menuTreeRef.value.getCheckedKeys() : []
  const halfCheckedKeys = menuTreeRef.value ? menuTreeRef.value.getHalfCheckedKeys() : []
  const allKeys = [...checkedKeys, ...halfCheckedKeys]

  await request.post(`/api/roles/${currentRole.id}/menus`, {
    menuIds: allKeys
  })
  ElMessage.success('分配菜单成功')
  menuDialogVisible.value = false
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