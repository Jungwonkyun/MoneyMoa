import { apiInstance } from './index.js'
import { instanceWithAuth } from './interceptorIndex.js'
import { useCookies } from 'vue3-cookies'

const api = apiInstance()
const apiWithAuth = instanceWithAuth()
const { cookies } = useCookies()

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
async function getCMA(id) {
  try {
    const response = await api.get(`/cma/${id}`)
    return response
  } catch (error) {
    console.log(error)
  }
}

//찜정보 post
async function likeProduct(productType, likeInfo) {
  console.log(productType + '상품 찜하기')
  try {
    // console.log('token?')
    const token = cookies.get('accessToken')
    const headers = {
      Authorization: `Bearer ${token}`
    }
    console.log(headers)
    const response = await apiWithAuth.post(`/${productType}/like`, JSON.stringify(likeInfo), {
      headers
    })
    console.log('찜했다')
    return response
  } catch (error) {
    console.log(error)
  }
}

//찜정보 get
async function getLiked(productType) {
  try {
    const token = cookies.get('accessToken')
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const response = await apiWithAuth.get(`/${productType}/like`, { headers })
    return response
  } catch (error) {
    console.log(error)
  }
}

//찜정보 delete
async function deleteLike(productType, likedId) {
  try {
    const token = cookies.get('accessToken')
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const response = await apiWithAuth.delete(`/${productType}/like/${likedId}`, { headers })
    return response
  } catch (error) {
    console.log(error)
  }
}

async function writeComment(productType, productCode, comment) {
  //productType에 따라 다른 post url
  try {
    const token = cookies.get('accessToken')
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const response = await apiWithAuth.post(
      `/${productType}/${productCode}/comment`,
      JSON.stringify(comment),
      { headers }
    )
    return response
  } catch (error) {
    console.log(error)
  }
}

async function deleteComment(productType, commentId) {
  try {
    const token = cookies.get('accessToken')
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const response = await apiWithAuth.delete(`/${productType}/comment/${commentId}`, { headers })
    return response
  } catch (error) {
    console.log(error)
  }
}

async function modifyComment(productType, commentId, newContent) {
  try {
    const token = cookies.get('accessToken')
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const response = await apiWithAuth.put(
      `/${productType}/comment/${commentId}`,
      JSON.stringify(newContent),
      { headers }
    )
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

//우대금리 조건배열에서 이율 추출한 객체 생성
function spclConditionIntrList(product) {
  return product.spclList
    .filter((str) => str.trim().length > 0)
    .map((str) => {
      const match = str.match(/\d+(\.\d+)?%/)
      const intr = match ? parseFloat(match[0]) : 0
      return {
        condition: str,
        intr: intr,
        checked: false
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
  likeProduct,
  getLiked,
  deleteLike,
  writeComment,
  deleteComment,
  modifyComment,
  getPeriodRange,
  getIntrRange,
  getMatchingDetail,
  spclConditionIntrList
}
