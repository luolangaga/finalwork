import { defineStore } from 'pinia'
import { ref } from 'vue'
import { dotnetApi } from '@/api'

export const useDashboardStore = defineStore('dashboard', () => {
  const trends = ref([])
  const hotResources = ref([])

  async function fetchStatistics() {
    try {
      const [t, h] = await Promise.all([
        dotnetApi.get('/statistics/trends'),
        dotnetApi.get('/statistics/hot-resources')
      ])
      trends.value = t.trends || []
      hotResources.value = h.resources || []
    } catch { /* dotnet service may not be available */ }
  }

  return { trends, hotResources, fetchStatistics }
})