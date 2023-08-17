import { apiInstance } from './index'

const api = apiInstance()

async function getDictionary() {
  try {
    const response = await api.get('/wiki/list')
    return response
  } catch (error) {
    console.log(error)
  }
}

export default { getDictionary }
