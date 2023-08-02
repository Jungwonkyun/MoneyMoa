import { apiInstance } from './index.js'
const api = apiInstance('products')

async function getDepositList() {
  try {
    const response = await api.get('/deposit')
    console.log(response)
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



export {
  getDepositList,
  getSavingList,
  getCMAList,
  getDeposit,
  getSaving,
  getCMA,
  getPeriodRange,
  getIntrRange
}
