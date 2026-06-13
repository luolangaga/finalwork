import { defineStore } from 'pinia'
import { ref } from 'vue'
import api from '@/api'

export const useDashboardStore = defineStore('dashboard', () => {
  const trends = ref([])
  const hotResources = ref([])
  const typeDistribution = ref([])

  async function fetchStatistics() {
    try {
      const [t, h] = await Promise.all([
        api.get('/statistics/trends'),
        api.get('/statistics/hot-resources')
      ])
      trends.value = t.trends || []
      hotResources.value = h.resources || []
    } catch { /* dotnet service may not be available */ }
  }

  return { trends, hotResources, typeDistribution, fetchStatistics }
})