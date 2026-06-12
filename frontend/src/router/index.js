import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', name: 'Resources', component: () => import('@/views/ResourceList.vue') },
  { path: '/borrow', name: 'Borrow', component: () => import('@/views/BorrowView.vue') },
  { path: '/dashboard', name: 'Dashboard', component: () => import('@/views/Dashboard.vue') },
  { path: '/notifications', name: 'Notifications', component: () => import('@/views/Notifications.vue') }
]

export default createRouter({
  history: createWebHistory(),
  routes
})