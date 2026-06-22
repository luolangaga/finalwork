import { defineStore } from 'pinia'
import { ref } from 'vue'
import { springApi } from '@/api'

export const useResourceStore = defineStore('resource', () => {
  const resources = ref([])
  const loading = ref(false)

  async function fetchAll() {
    loading.value = true
    try { resources.value = await springApi.get('/resources') }
    finally { loading.value = false }
  }

  async function search(keyword, type) {
    loading.value = true
    try {
      if (keyword) resources.value = await springApi.get(`/resources/search?keyword=${keyword}`)
      else if (type) resources.value = await springApi.get(`/resources/type/${type}`)
      else resources.value = await springApi.get('/resources')
    } finally { loading.value = false }
  }

  async function add(dto) {
    await springApi.post('/resources', dto)
    await fetchAll()
  }

  async function remove(id) {
    await springApi.delete(`/resources/${id}`)
    await fetchAll()
  }

  return { resources, loading, fetchAll, search, add, remove }
})