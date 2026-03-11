<template>
  <el-card>
    <template #header>
      <div class="header-row">
        <span>登录日志</span>
      </div>
    </template>

    <div class="toolbar">
      <el-input v-model="query.username" placeholder="用户名" clearable style="width: 180px" />
      <el-select v-model="query.loginStatus" placeholder="登录状态" clearable style="width: 140px">
        <el-option label="成功" :value="1" />
        <el-option label="失败" :value="0" />
      </el-select>
      <el-button type="primary" @click="handleSearch">查询</el-button>
      <el-button @click="handleReset">重置</el-button>
    </div>

    <el-table v-loading="loading" :data="tableData" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" min-width="120" />
      <el-table-column label="登录状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.loginStatus === 1 ? 'success' : 'danger'">
            {{ scope.row.loginStatus === 1 ? '成功' : '失败' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="loginIp" label="IP" min-width="140" />
      <el-table-column prop="browser" label="浏览器" min-width="120" />
      <el-table-column prop="os" label="系统" min-width="120" />
      <el-table-column prop="message" label="信息" min-width="180" />
      <el-table-column prop="createdAt" label="时间" min-width="180" />
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
  </el-card>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import request from '../utils/request'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)

const query = reactive({
  pageNum: 1,
  pageSize: 10,
  username: '',
  loginStatus: null
})

async function loadData() {
  loading.value = true
  try {
    const res = await request.get('/api/logs/logins', { params: query })
    tableData.value = res.data.records || []
    total.value = res.data.total || 0
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
  query.loginStatus = null
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