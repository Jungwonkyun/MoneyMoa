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
async function getSavingList() {
  try {
    const response = await api.get('/saving')
    return response
  } catch (error) {
    console.log(error)
  }
}
async function getCMAList() {
  try {
    const response = await api.get('/cma')
    return response
  } catch (error) {
    console.log(error)
  }
}

//예적금detail리스트 가공
function getMinMaxIntr(product) {}

//찜정보post하는 메서드
async function likeProduct(likeInfo) {
  try {
    const response = await api.post('/myproduct', likeInfo)
    return response
  } catch (error) {
    console.log(error)
  }
}

export { getDepositList, getSavingList, getCMAList }
