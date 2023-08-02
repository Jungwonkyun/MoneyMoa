import { apiInstance } from './index.js'
const api = apiInstance()

// 모든 유저 정보 API
async function getLoadAlluser(token) {
  try {
    const headers = {
      Authorization: `Bearer ${token}`,
    }
    const res = await api.get(`/admin/loadallusers`, { headers })
    console.log(res)
    return res
  } catch (err) {
    console.log(err)
  }
}

export { getLoadAlluser }
