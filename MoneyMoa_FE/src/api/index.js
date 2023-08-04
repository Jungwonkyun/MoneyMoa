import axios from 'axios'

function apiInstance(path) {
  const baseURL = path
    ? `${import.meta.env.VITE_APP_API_URL}/${path}`
    : import.meta.env.VITE_APP_API_URL
  const instance = axios.create({
    withCredentials: true,
    baseURL: baseURL,
    headers: {
      'Content-Type': 'application/json;charset=utf-8'
    }
  })
  return instance
}

export { apiInstance }
