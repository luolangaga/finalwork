<template>
  <el-card>
    <template #header>
      <el-row justify="space-between">
        <span>通知中心</span>
        <el-button size="small" @click="nStore.fetchAll()">刷新</el-button>
      </el-row>
    </template>
    <el-empty v-if="nStore.notifications.length === 0" description="暂无通知" />
    <el-timeline v-else>
      <el-timeline-item
        v-for="n in nStore.notifications"
        :key="n.id"
        :timestamp="n.createdAt"
        placement="top"
      >
        <el-card :class="{ 'unread-card': !n.isRead }">
          <el-row justify="space-between" align="middle">
            <el-col :span="20">
              <el-tag :type="tagType(n.type)" size="small">{{ n.type }}</el-tag>
              <span style="margin-left: 8px">{{ n.content }}</span>
            </el-col>
            <el-col :span="4" style="text-align: right">
              <el-button v-if="!n.isRead" type="primary" size="small" @click="nStore.markRead(n.id)">
                标为已读
              </el-button>
            </el-col>
          </el-row>
        </el-card>
      </el-timeline-item>
    </el-timeline>
  </el-card>
</template>

<script setup>
import { onMounted } from 'vue'
import { useNotificationStore } from '@/stores/notification'

const nStore = useNotificationStore()
const tagType = (t) => ({ borrow: 'success', overdue: 'danger', info: 'info' }[t] || '')

onMounted(() => nStore.fetchAll())
</script>

<style>
.unread-card { border-left: 3px solid #409eff; }
</style>