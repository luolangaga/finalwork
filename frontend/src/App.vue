<template>
  <div id="app-container">
    <el-container>
      <el-header class="app-header">
        <el-row justify="space-between" align="middle">
          <el-col :span="12">
            <h2 class="app-title">📚 图书馆管理系统</h2>
          </el-col>
          <el-col :span="12" style="text-align: right">
            <el-menu mode="horizontal" :router="true" class="nav-menu">
              <el-menu-item index="/">资源浏览</el-menu-item>
              <el-menu-item index="/borrow">借阅管理</el-menu-item>
              <el-menu-item index="/dashboard">数据看板</el-menu-item>
              <el-menu-item index="/notifications">
                通知
                <el-badge :value="unreadCount" :hidden="unreadCount === 0" />
              </el-menu-item>
            </el-menu>
          </el-col>
        </el-row>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useNotificationStore } from '@/stores/notification'
const store = useNotificationStore()
const unreadCount = computed(() => store.unreadCount)
</script>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
body { font-family: 'Helvetica Neue', Arial, sans-serif; background: #f5f7fa; }
.app-header { background: #fff; box-shadow: 0 2px 8px rgba(0,0,0,0.06); padding: 0; }
.app-title { line-height: 60px; padding-left: 24px; color: #409eff; }
.nav-menu { border-bottom: none !important; }
</style>