import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '@/api'

export const useNotificationStore = defineStore('notification', () => {
  const notifications = ref([])
  const unreadCount = computed(() => notifications.value.filter(n => !n.isRead).length)

  async function fetchAll(userId = 'B001') {
    notifications.value = await api.get(`/notifications/${userId}`)
  }

  async function markRead(notificationId) {
    await api.post('/notifications/mark-read', { notificationId })
    const n = notifications.value.find(x => x.id === notificationId)
    if (n) n.isRead = true
  }

  return { notifications, unreadCount, fetchAll, markRead }
})