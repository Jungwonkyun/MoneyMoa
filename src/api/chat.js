import { apiInstance } from './index.js'
import { useCookies } from 'vue3-cookies'
const api = apiInstance('chat')
const { cookies } = useCookies()

async function getRooms() {
  try {
    const response = await api.get('roomlist')
    return response
  } catch (error) {
    console.log(error)
  }
}

export { getRooms }
