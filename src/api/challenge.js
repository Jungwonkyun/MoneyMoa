import { apiInstance } from './index.js'
import { useCookies } from 'vue3-cookies'

const api = apiInstance()
const { cookies } = useCookies()
const token = cookies.get('accessToken')

//챌린지 생성 API
async function postChallenge(challengeData) {
  try {
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const res = await api.post(`/challenge/create`, JSON.stringify(challengeData), {
      headers
    })
    return res
  } catch (err) {
    console.log(err)
  }
}

//내가 만든 챌린지 목록 API
async function getChallengeList(memberId) {
  try {
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const res = await api.get(`/challenge/list/${memberId}`, {
      headers
    })
    return res
  } catch (err) {
    console.log(err)
  }
}

//챌린지 삭제 API
async function deleteChallenge(challengeId) {
  try {
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const res = await api.delete(`/challenge/delete/${challengeId}`, {
      headers
    })
    return res
  } catch (err) {
    console.log(err)
  }
}

//챌린지 상세 조회 API
async function getChallengeDetail(challengeId) {
  try {
    const res = await api.get(`/challenge/${challengeId}`, {
      Authorization: `Bearer ${token}`
    })
    return res
  } catch (err) {
    console.log(err)
  }
}

//챌린지 수정 API
async function updateChallenge(challengeId, challengeData) {
  try {
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const res = await api.put(`/challenge/update/${challengeId}`, JSON.stringify(challengeData), {
      headers
    })
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
