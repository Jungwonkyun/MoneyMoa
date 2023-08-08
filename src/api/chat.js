import { apiInstance } from './index.js'
import { useCookies } from 'vue3-cookies'
const api = apiInstance('chat')
const { cookies } = useCookies()

async function getRooms() {
  try {
    const response = await api.get('rooms')
    return response
  } catch (error) {
    console.log(error)
  }
}

async function getRoomDetail(roomId) {
  try {
    const response = await api.get(`room/${roomId}`)
    return response
  } catch (error) {
    console.log(error)
  }
}

export { getRooms, getRoomDetail }
