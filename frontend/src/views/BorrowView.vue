<template>
  <div>
    <el-row :gutter="16">
      <el-col :span="12">
        <el-card header="借阅资源">
          <el-form label-width="100px">
            <el-form-item label="借阅者ID">
              <el-input v-model="borrowerId" placeholder="如 B001" />
            </el-form-item>
            <el-form-item label="资源ID">
              <el-input v-model="resourceId" placeholder="如 R001" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="doBorrow" :loading="borrowing">确认借阅</el-button>
            </el-form-item>
          </el-form>
          <el-alert v-if="borrowResult" type="success" :closable="false" style="margin-top: 12px">
            {{ borrowResult }}
          </el-alert>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card header="归还资源">
          <el-form label-width="100px">
            <el-form-item label="资源ID">
              <el-input v-model="returnResourceId" placeholder="如 R001" />
            </el-form-item>
            <el-form-item>
              <el-button type="success" @click="doReturn">确认归还</el-button>
            </el-form-item>
          </el-form>
        </el-card>
        <el-card header="借阅记录" style="margin-top: 16px">
          <el-input v-model="queryBorrowerId" placeholder="输入借阅者ID" style="margin-bottom: 12px">
            <template #append>
              <el-button @click="queryRecords">查询</el-button>
            </template>
          </el-input>
          <el-table :data="records" stripe border size="small">
            <el-table-column prop="recordId" label="记录ID" width="100" />
            <el-table-column prop="resourceId" label="资源" width="100" />
            <el-table-column prop="borrowDate" label="借阅日" width="110" />
            <el-table-column prop="dueDate" label="到期日" width="110" />
            <el-table-column prop="returned" label="状态" width="80">
              <template #default="{ row }">
                <el-tag :type="row.returned ? 'success' : 'danger'">{{ row.returned ? '已还' : '未还' }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useBorrowStore } from '@/stores/borrow'
import { ElMessage } from 'element-plus'

const store = useBorrowStore()
const borrowerId = ref('')
const resourceId = ref('')
const returnResourceId = ref('')
const queryBorrowerId = ref('')
const records = ref([])
const borrowing = ref(false)
const borrowResult = ref(null)

const doBorrow = async () => {
  borrowing.value = true
  try {
    const r = await store.borrow(borrowerId.value, resourceId.value)
    borrowResult.value = `借阅成功！到期日: ${r.dueDate}`
    ElMessage.success('借阅成功')
  } catch (e) {
    ElMessage.error(e.message)
  } finally { borrowing.value = false }
}

const doReturn = async () => {
  try {
    await store.returnResource(returnResourceId.value)
    ElMessage.success('归还成功')
  } catch (e) { ElMessage.error(e.message) }
}

const queryRecords = async () => {
  try { records.value = await store.getRecords(queryBorrowerId.value) }
  catch (e) { ElMessage.error(e.message) }
}
</script>