import { apiInstance } from './index.js'
import { useCookies } from 'vue3-cookies'
import { instanceWithAuth } from './interceptorIndex.js'

const api = apiInstance()
const apiWithAuth = instanceWithAuth()

// 토큰 받아오기
const { cookies } = useCookies()
const token = cookies.get('accessToken')

//챌린지 생성 API
async function postChallenge(challengeData) {
  try {
    const headers = {
      'Content-Type': 'multipart/form-data'
    }
    const res = await apiWithAuth.post(`/challenge/create`, challengeData, { headers })
    return res
  } catch (err) {
    console.log(err)
  }
}

//내가 만든 챌린지 목록 API
async function getChallengeList(memberId) {
  try {
    const res = await apiWithAuth.get(`/challenge/list/${memberId}`)
    return res
  } catch (err) {
    console.log(err)
  }
}

//챌린지 삭제 API
async function deleteChallenge(challengeId) {
  try {
    console.log(challengeId)
    const res = await apiWithAuth.delete(`/challenge/delete/${challengeId}`)
    return res
  } catch (err) {
    console.log(err)
  }
}

//챌린지 상세 조회 API
async function getChallengeDetail(challengeId) {
  try {
    const res = await apiWithAuth.get(`/challenge/${challengeId}`)
    return res
  } catch (err) {
    console.log(err)
  }
}

//챌린지 수정 API
async function updateChallenge(challengeId, challengeData) {
  try {
    const res = await apiWithAuth.put(
      `/challenge/update/${challengeId}`,
      JSON.stringify(challengeData)
    )
    return res
  } catch (err) {
    console.log(err)
  }
}

export default {
  postChallenge,
  getChallengeList,
  deleteChallenge,
  getChallengeDetail,
  updateChallenge
}
