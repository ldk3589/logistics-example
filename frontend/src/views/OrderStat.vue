<template>
  <div class="order-stat">
    <h2>订单统计</h2>

    <el-radio-group v-model="timeRange" @change="fetchStats">
      <el-radio-button value="week">本周</el-radio-button>
      <el-radio-button value="month">本月</el-radio-button>
      <el-radio-button value="year">今年</el-radio-button>
      <el-radio-button value="all">历史</el-radio-button>
    </el-radio-group>

    <div class="chart-container" v-if="statsData.length > 0 && chartOption">
      <v-chart :option="chartOption" autoresize />
    </div>
    <el-empty v-else description="暂无统计数据" style="margin-top: 20px;" />

    <el-table :data="statsData" border style="width: 100%; margin-top: 20px">
      <el-table-column prop="period" label="时间段" />
      <el-table-column prop="count" label="订单数量" />
      <el-table-column label="总金额">
        <template #default="{ row }">
          ¥ {{ Number(row.totalAmount || 0).toFixed(2) }}
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import request from '@/api/request'
import { use } from 'echarts/core'
import VChart from 'vue-echarts'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart, BarChart } from 'echarts/charts' // 建议多注册一个Bar以防万一
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
} from 'echarts/components'

// 必须注册这些组件，vue-echarts 才能工作
use([
  CanvasRenderer,
  LineChart,
  BarChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
])

const timeRange = ref('year')
const statsData = ref([])
const chartOption = ref(null)

const rangeTextMap = {
  week: '本周',
  month: '本月',
  year: '今年',
  all: '历史'
}

const fetchStats = async () => {
  try {
    // 这里的 /orders/statistics 对应你后端的 @GetMapping("/statistics")
    const res = await request.get('/orders/statistics', {
      params: { range: timeRange.value }
    })

    // 重点：根据你的后端 Result 结构判断，如果 res 是包装类，则用 res.data
    // 如果 Axios 拦截器里已经处理了 res.data，则直接用 res
    const rawData = res.data || res 
    statsData.value = Array.isArray(rawData) ? rawData : []

    if (statsData.value.length === 0) {
      chartOption.value = null
      return
    }

    await nextTick() // 等待 DOM 准备
    buildChart()
  } catch (e) {
    console.error('获取订单统计失败', e)
    statsData.value = []
  }
}

const buildChart = () => {
  const xData = statsData.value.map(i => i.period)
  const countData = statsData.value.map(i => i.count)
  const amountData = statsData.value.map(i => Number(i.totalAmount || 0))

  chartOption.value = {
    backgroundColor: '#ffffff',
    title: {
      text: `${rangeTextMap[timeRange.value]}订单趋势`,
      left: 'center'
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' }
    },
    legend: {
      data: ['订单数量', '总金额'],
      bottom: 0
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '10%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: xData,
      axisTick: { alignWithLabel: true }
    },
    yAxis: [
      { type: 'value', name: '订单数量', position: 'left' },
      { type: 'value', name: '金额', position: 'right', axisLabel: { formatter: '¥{value}' } }
    ],
    series: [
      {
        name: '订单数量',
        type: 'bar', // 改为 bar 在单个数据点时显示效果更好
        barWidth: '40%',
        data: countData
      },
      {
        name: '总金额',
        type: 'line',
        yAxisIndex: 1,
        smooth: true,
        data: amountData
      }
    ]
  }
}

onMounted(fetchStats)
</script>

<style scoped>
.order-stat {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}
.chart-container {
  width: 100%;
  height: 450px;
  margin-top: 20px;
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}
</style>