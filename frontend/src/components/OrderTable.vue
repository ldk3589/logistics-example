<template>
  <el-table :data="orders">
    <el-table-column prop="id" label="订单ID" />
    <el-table-column prop="status" label="状态" />
    <el-table-column>
      <template #default="scope">
        <el-button
          v-if="role !== 'USER'"
          @click="finish(scope.row.id)"
        >完成</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getOrders, updateStatus } from '@/api/order'
import { ElMessage } from 'element-plus'

const orders = ref([])
const role = localStorage.getItem('role')

onMounted(async () => {
  orders.value = await getOrders()
})

const finish = async id => {
  await updateStatus(id, 'FINISHED')
  ElMessage.success('订单完成，已生成账单并通知用户')
  orders.value = await getOrders()
}
</script>
