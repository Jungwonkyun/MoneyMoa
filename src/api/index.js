import axios from 'axios'

function apiInstance() {
  const instance = axios.create({
    baseURL: import.meta.env.VITE_APP_API_URL,
    headers: {
      'Content-Type': 'application/json;charset=utf-8'
    }
  })
  return instance
}

export { apiInstance }
