import { apiInstance } from './index.js'
const api = apiInstance()

async function getDepositList() {
  try {
    console.log('예금가져올까?')
    // const response = await api.get('/deposit/list')
    const reponse = await api.get('/api/products/deposit/list')
    console.log('예금가져왔다')
    console.log(response)
    return response
  } catch (error) {
    console.log(error)
  }
}
async function getSavingList() {
  try {
    const response = await api.get('/saving/list')
    return response
  } catch (error) {
    console.log(error)
  }
}
async function getCMAList() {
  try {
    const response = await api.get('/cma/list')
    return response
  } catch (error) {
    console.log(error)
  }
}

async function getDeposit(productCode) {
  try {
    const response = await api.get(`/deposit/${productCode}`)
    return response
  } catch (error) {
    console.log(error)
  }
}
async function getSaving(productCode) {
  try {
    const response = await api.get(`/saving/${productCode}`)
    return response
  } catch (error) {
    console.log(error)
  }
}
async function getCMA(productCode) {
  try {
    const response = await api.get(`/cma/${productCode}`)
    return response
  } catch (error) {
    console.log(error)
  }
}

//찜정보 post
async function likeProduct(likeInfo) {
  try {
    const response = await api.post('/myproduct', likeInfo)
    return response
  } catch (error) {
    console.log(error)
  }
}

/* 상품정보가공(여기서부터 axios아님) */
function getPeriodRange(product) {
  const range = {
    min: product.interestDetails.at(0).period,
    max: product.interestDetails.at(-1).period
  }
  return range
}

function getIntrRange(product) {
  const range = {
    min: Number(product.interestDetails.at(0).basicRate),
    max: Number(product.interestDetails.at(-1).maxRate)
  }
  return range
}

//우대금리 조건배열에서 이율 분리해서 합치기
function spclConditionIntrList(product) {
  return product.spclList.map((str) => {
    const match = str.match(/\d+(\.\d+)?%/)
    const intr = match ? parseFloat(match[0]) : null
    return {
      condition: str,
      intr: intr
    }
  })
}

export {
  getDepositList,
  getSavingList,
  getCMAList,
  getDeposit,
  getSaving,
  getCMA,
  getPeriodRange,
  getIntrRange,
  spclConditionIntrList
}
