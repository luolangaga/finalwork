<template>
  <div>
    <el-card class="search-card">
      <el-row :gutter="16">
        <el-col :span="8">
          <el-input v-model="keyword" placeholder="输入关键词搜索..." clearable @clear="doSearch" />
        </el-col>
        <el-col :span="4">
          <el-select v-model="typeFilter" placeholder="全部类型" clearable @change="doSearch">
            <el-option label="全部" value="" />
            <el-option label="书籍" value="BOOK" />
            <el-option label="杂志" value="MAGAZINE" />
            <el-option label="DVD" value="DVD" />
            <el-option label="电子书" value="EBOOK" />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="doSearch">搜索</el-button>
          <el-button @click="refresh">刷新</el-button>
        </el-col>
      </el-row>
    </el-card>

    <el-card style="margin-top: 16px">
      <el-table :data="store.resources" v-loading="store.loading" stripe border>
        <el-table-column prop="id" label="编号" width="100" />
        <el-table-column prop="title" label="标题" min-width="180" />
        <el-table-column prop="type" label="类型" width="90">
          <template #default="{ row }">
            <el-tag :type="typeColor(row.type)">{{ row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'AVAILABLE' ? 'success' : 'warning'">{{ row.status === 'AVAILABLE' ? '可借' : '已借出' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="详情" min-width="200">
          <template #default="{ row }">
            <div v-if="row.extraAttrs" style="font-size: 12px; color: #666">
              <span v-for="(v, k) in row.extraAttrs" :key="k" style="margin-right: 8px">
                {{ k }}: {{ v }}
              </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="借阅者" width="120" prop="borrowerId" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useResourceStore } from '@/stores/resource'

const store = useResourceStore()
const keyword = ref('')
const typeFilter = ref('')

const typeColor = (t) => ({ BOOK: 'primary', MAGAZINE: 'success', DVD: 'warning', EBOOK: 'info' }[t] || '')
const doSearch = () => store.search(keyword.value || null, typeFilter.value || null)
const refresh = () => store.fetchAll()
onMounted(() => store.fetchAll())
</script>