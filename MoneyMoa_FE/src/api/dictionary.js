import { apiInstance } from './index'

const api = apiInstance()

async function getRooms() {
  try {
    const response = await api.get('roomlist')
    return response
  } catch (error) {
    console.log(error)
  }
}

export {}
