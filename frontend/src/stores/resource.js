import { defineStore } from 'pinia'
import { ref } from 'vue'
import api from '@/api'

export const useResourceStore = defineStore('resource', () => {
  const resources = ref([])
  const loading = ref(false)

  async function fetchAll() {
    loading.value = true
    try { resources.value = await api.get('/resources') }
    finally { loading.value = false }
  }

  async function search(keyword, type) {
    loading.value = true
    try {
      if (keyword) resources.value = await api.get(`/resources/search?keyword=${keyword}`)
      else if (type) resources.value = await api.get(`/resources/type/${type}`)
      else resources.value = await api.get('/resources')
    } finally { loading.value = false }
  }

  async function add(dto) {
    await api.post('/resources', dto)
    await fetchAll()
  }

  async function remove(id) {
    await api.delete(`/resources/${id}`)
    await fetchAll()
  }

  return { resources, loading, fetchAll, search, add, remove }
})