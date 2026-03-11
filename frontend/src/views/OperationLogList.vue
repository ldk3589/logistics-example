<template>
  <el-card>
    <template #header>
      <div class="header-row">
        <span>操作日志</span>
      </div>
    </template>

    <div class="toolbar">
      <el-input v-model="query.moduleName" placeholder="模块名称" clearable style="width: 180px" />
      <el-input v-model="query.operationName" placeholder="操作名称" clearable style="width: 180px" />
      <el-input v-model="query.operatorName" placeholder="操作人" clearable style="width: 160px" />
      <el-select v-model="query.operationStatus" placeholder="操作状态" clearable style="width: 140px">
        <el-option label="成功" :value="1" />
        <el-option label="失败" :value="0" />
      </el-select>
      <el-button type="primary" @click="handleSearch">查询</el-button>
      <el-button @click="handleReset">重置</el-button>
    </div>

    <el-table v-loading="loading" :data="tableData" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="moduleName" label="模块" min-width="120" />
      <el-table-column prop="operationName" label="操作" min-width="140" />
      <el-table-column prop="operatorName" label="操作人" min-width="120" />
      <el-table-column label="状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.operationStatus === 1 ? 'success' : 'danger'">
            {{ scope.row.operationStatus === 1 ? '成功' : '失败' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="requestMethod" label="请求方式" width="100" />
      <el-table-column prop="requestUri" label="接口地址" min-width="220" />
      <el-table-column prop="costTimeMs" label="耗时(ms)" width="110" />
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
  moduleName: '',
  operationName: '',
  operatorName: '',
  operationStatus: null
})

async function loadData() {
  loading.value = true
  try {
    const res = await request.get('/api/logs/operations', { params: query })
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
  query.moduleName = ''
  query.operationName = ''
  query.operatorName = ''
  query.operationStatus = null
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