<template>
  <el-card>
    <template #header>
      <div class="header-row">
        <span>菜单管理</span>
        <div>
          <el-button v-if="canAdd" type="primary" @click="openAddDialog()">新增根菜单</el-button>
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
      <el-table-column prop="menuName" label="菜单名称" min-width="180" />
      <el-table-column prop="menuType" label="类型" width="100" />
      <el-table-column prop="path" label="路径" min-width="180" />
      <el-table-column prop="component" label="组件" min-width="180" />
      <el-table-column prop="routeName" label="路由名" min-width="140" />
      <el-table-column prop="permissionCode" label="权限码" min-width="180" />
      <el-table-column prop="sortNum" label="排序" width="80" />

      <el-table-column label="显示" width="80">
        <template #default="scope">
          <el-tag :type="scope.row.visible === 1 ? 'success' : 'info'">
            {{ scope.row.visible === 1 ? '是' : '否' }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="缓存" width="80">
        <template #default="scope">
          <el-tag :type="scope.row.keepAlive === 1 ? 'success' : 'info'">
            {{ scope.row.keepAlive === 1 ? '是' : '否' }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="200" fixed="right">
        <template #default="scope">
          <el-button
            v-if="canAdd"
            link
            type="primary"
            @click="openAddDialog(scope.row)"
          >
            新增子菜单
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

    <el-dialog v-model="dialogVisible" title="新增菜单" width="560px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="父菜单ID">
          <el-input v-model.number="form.parentId" />
        </el-form-item>

        <el-form-item label="菜单名称">
          <el-input v-model="form.menuName" />
        </el-form-item>

        <el-form-item label="菜单类型">
          <el-select v-model="form.menuType" style="width: 100%">
            <el-option label="目录" value="DIR" />
            <el-option label="菜单" value="MENU" />
            <el-option label="按钮" value="BUTTON" />
          </el-select>
        </el-form-item>

        <el-form-item label="路径">
          <el-input v-model="form.path" />
        </el-form-item>

        <el-form-item label="组件路径">
          <el-input v-model="form.component" />
        </el-form-item>

        <el-form-item label="路由名称">
          <el-input v-model="form.routeName" />
        </el-form-item>

        <el-form-item label="图标">
          <el-input v-model="form.icon" />
        </el-form-item>

        <el-form-item label="权限码">
          <el-input v-model="form.permissionCode" />
        </el-form-item>

        <el-form-item label="是否显示">
          <el-select v-model="form.visible" style="width: 100%">
            <el-option label="显示" :value="1" />
            <el-option label="隐藏" :value="0" />
          </el-select>
        </el-form-item>

        <el-form-item label="是否缓存">
          <el-select v-model="form.keepAlive" style="width: 100%">
            <el-option label="缓存" :value="1" />
            <el-option label="不缓存" :value="0" />
          </el-select>
        </el-form-item>

        <el-form-item label="排序">
          <el-input v-model.number="form.sortNum" />
        </el-form-item>

        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
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
  </el-card>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../utils/request'
import { hasPermission } from '../utils/auth'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)

const canAdd = computed(() => hasPermission('sys:menu:add'))
const canDelete = computed(() => hasPermission('sys:menu:delete'))

const form = reactive({
  parentId: 0,
  menuName: '',
  menuType: 'MENU',
  path: '',
  component: '',
  routeName: '',
  icon: '',
  permissionCode: '',
  visible: 1,
  keepAlive: 1,
  sortNum: 0,
  status: 1,
  remark: ''
})

async function loadTree() {
  loading.value = true
  try {
    const res = await request.get('/api/menus/tree')
    tableData.value = res.data || []
  } finally {
    loading.value = false
  }
}

function resetForm() {
  form.parentId = 0
  form.menuName = ''
  form.menuType = 'MENU'
  form.path = ''
  form.component = ''
  form.routeName = ''
  form.icon = ''
  form.permissionCode = ''
  form.visible = 1
  form.keepAlive = 1
  form.sortNum = 0
  form.status = 1
  form.remark = ''
}

function openAddDialog(row) {
  resetForm()
  if (row && row.id) {
    form.parentId = row.id
  }
  dialogVisible.value = true
}

async function submitAdd() {
  await request.post('/api/menus', form)
  ElMessage.success('新增菜单成功')
  dialogVisible.value = false
  loadTree()
}

async function handleDelete(row) {
  await ElMessageBox.confirm(
    `确认删除菜单【${row.menuName}】吗？`,
    '提示',
    { type: 'warning' }
  )
  await request.delete(`/api/menus/${row.id}`)
  ElMessage.success('删除菜单成功')
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