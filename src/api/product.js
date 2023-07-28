import { apiInstance } from './index.js'
const api = apiInstance('products')

async function getDepositList() {
  try {
    const response = await api.get('/deposit')
    return response
  } catch (error) {
    console.log(error)
  }
}
export { getDepositList }
