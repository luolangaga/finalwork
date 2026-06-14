<template>
  <div>
    <el-row :gutter="16">
      <el-col :span="12">
        <el-card header="借阅趋势（近30天）">
          <div ref="trendChart" style="height: 350px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card header="热门资源 Top10">
          <div ref="hotChart" style="height: 350px"></div>
        </el-card>
      </el-col>
    </el-row>
    <div style="margin-top: 16px; text-align: center">
      <el-button type="primary" @click="store.fetchStatistics" :loading="loading">刷新数据</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, nextTick } from 'vue'
import { useDashboardStore } from '@/stores/dashboard'
import * as echarts from 'echarts'

const store = useDashboardStore()
const loading = ref(false)
const trendChart = ref(null)
const hotChart = ref(null)
let trendInstance, hotInstance

const renderCharts = () => {
  nextTick(() => {
    if (trendChart.value) {
      trendInstance = echarts.init(trendChart.value)
      const dates = store.trends.map(t => t.date)
      trendInstance.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['借阅', '归还'] },
        xAxis: { type: 'category', data: dates },
        yAxis: { type: 'value' },
        series: [
          { name: '借阅', type: 'line', data: store.trends.map(t => t.borrow), smooth: true, color: '#409eff' },
          { name: '归还', type: 'line', data: store.trends.map(t => t.return), smooth: true, color: '#67c23a' }
        ]
      })
    }
    if (hotChart.value) {
      hotInstance = echarts.init(hotChart.value)
      hotInstance.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: store.hotResources.map(r => r.resourceId) },
        yAxis: { type: 'value' },
        series: [{ type: 'bar', data: store.hotResources.map(r => r.count), color: '#e6a23c' }]
      })
    }
  })
}

watch(() => store.trends, renderCharts)
onMounted(async () => {
  loading.value = true
  await store.fetchStatistics()
  loading.value = false
  renderCharts()
})
</script>