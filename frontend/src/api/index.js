import axios from 'axios'

const cfg = window.__APP_CONFIG__ || {}
const SPRING = cfg.SPRING_API || '/api'
const DOTNET = cfg.DOTNET_API || '/api'

const springApi = axios.create({
  baseURL: SPRING,
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' }
})

const dotnetApi = axios.create({
  baseURL: DOTNET,
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' }
});

[springApi, dotnetApi].forEach(api => {
  api.interceptors.response.use(
    res => res.data,
    err => {
      const msg = err.response?.data?.message || err.message || '请求失败'
      return Promise.reject(new Error(msg))
    }
  )
})

export { springApi, dotnetApi }
export default springApi