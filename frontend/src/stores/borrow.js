import { defineStore } from 'pinia'
import { ref } from 'vue'
import { springApi } from '@/api'

export const useBorrowStore = defineStore('borrow', () => {
  const result = ref(null)

  async function borrow(borrowerId, resourceId) {
    result.value = await springApi.post(`/borrow/borrow?borrowerId=${borrowerId}&resourceId=${resourceId}`)
    return result.value
  }

  async function returnResource(resourceId) {
    await springApi.post(`/borrow/return?resourceId=${resourceId}`)
    result.value = null
  }

  async function getRecords(borrowerId) {
    return await springApi.get(`/borrow/records/${borrowerId}`)
  }

  return { result, borrow, returnResource, getRecords }
})