import { apiInstance } from './index.js'
const api = apiInstance('products')

async function getDepositList() {
  try {
    const response = await api.get('deposit/list')
    return response
  } catch (error) {
    console.log(error)
  }
}
function getTest() {
  return api.get('deposit/list')
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

//찜정보 post (url수정해야됨)
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
  //product호출이 비동기라서 undefined상태에서 값을 가져오려고 하면 에러가 나서 이렇게 처리했는데 이게 맞는지 모르겠어요
  if (!Object.keys(product).includes('interestDetails')) {
    return {
      min: 0,
      max: 0
    }
  }
  const range = {
    min: product.interestDetails.at(0).period,
    max: product.interestDetails.at(-1).period
  }
  return range
}

function getIntrRange(product) {
  if (!Object.keys(product).includes('interestDetails')) {
    return {
      min: 0,
      max: 0
    }
  }
  const range = {
    min: Number(product.interestDetails.at(0).basicRate),
    max: Number(product.interestDetails.at(-1).maxRate)
  }
  return range
}

function getMatchingDetail(product, period) {
  //입력한 기간이 주어졌을 때, 상품의 intrDetails 중 어느 항목에 해당하는지 리턴
  if (!product || !Object.keys(product).includes('interestDetails') || !period) {
    console.log('몰?루')
    return null
  }
  return product.interestDetails.reduce((prev, curr) => {
    if (curr.period <= period && (!prev || curr.period > prev.period)) {
      return curr
    } else {
      return prev
    }
  }, null)
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
  getTest,
  getDepositList,
  getSavingList,
  getCMAList,
  getDeposit,
  getSaving,
  getCMA,
  getPeriodRange,
  getIntrRange,
  getMatchingDetail,
  spclConditionIntrList
}
