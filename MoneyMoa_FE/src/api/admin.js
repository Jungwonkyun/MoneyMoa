import { apiInstance } from './index.js'
const api = apiInstance()

// 모든 유저 정보 API
async function getLoadAlluser(token) {
  try {
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const res = await api.get(`/admin/loadallusers`, { headers })
    return res
  } catch (err) {
    console.log(err)
  }
}
// 유저 삭제
async function deleteUser(id, token) {
  try {
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const res = await api.delete(`/admin/${id}`, { headers })
    console.log(res)
    return res
  } catch (err) {
    console.log(err)
  }
}

export default { getLoadAlluser, deleteUser }
