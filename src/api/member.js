import { apiInstance } from './index'
import { useAccountStore } from '@/stores/accountStore.js'

const api = apiInstance()

// 피니아 스토어에서 유저 토큰을 가져오기
function getAccessToken() {
  const accountStore = useAccountStore()
  const accessToken = accountStore.accessToken
  return accessToken
}

// 유저 정보 API
async function callUserInfoApi(memberId) {
  try {
    const accessToken = getAccessToken()
    const res = await api.get(`/member/info/${memberId}`, {
      usertoken: accessToken
    })
    return res
  } catch (err) {
    console.log(err)
  }
}

// 회원가입시 유저 이메일 인증
async function postEmailauth(emailvalue) {
  try {
    const res = await api.post(`http://i9d210.p.ssafy.io:9999/emailauth`, {
      email: emailvalue
    })
    console.log(emailvalue)
    return res.data
  } catch (err) {
    console.log(err)
  }
}

// 팔로잉 API
// 유저 토큰이랑 팔로우 할 사람의 id를 보내면 됨
async function addFollow(memberId) {
  try {
    const accessToken = getAccessToken()
    const res = await api.post(`/member/follow/${memberId}`, {
      usertoken: accessToken
    })
    return res
  } catch (err) {
    console.log(err)
  }
}

// 챌린지 리스트 API
async function fetchChallengeList(memberId) {
  try {
    const accessToken = getAccessToken()
    const res = await api.get(`/member/challengelist/${memberId}`, {
      usertoken: accessToken
    })
    return res
  } catch (err) {
    console.log(err)
  }
}

// 팔로워 유저 목록 API
async function fetchFollowerList(memberId) {
  try {
    const accessToken = getAccessToken()
    const res = await api.get(`/member/followerlist/${memberId}`, {
      usertoken: accessToken
    })
    return res
  } catch (err) {
    console.log(err)
  }
}

// 팔로잉 유저 목록 API
async function fetchFollowingList(memberId) {
  try {
    const accessToken = getAccessToken()
    const res = await api.get(`/member/followinglist/${memberId}`, {
      usertoken: accessToken
    })
    return res
  } catch (err) {
    console.log(err)
  }
}

// 마이 피드 목록 API
async function fetchFeedList(memberId) {
  try {
    const accessToken = getAccessToken()
    const res = await api.get(`/member/feed/${memberId}`, {
      usertoken: accessToken
    })
    return res
  } catch (err) {
    console.log(err)
  }
}

export default {
  callUserInfoApi,
  addFollow,
  fetchChallengeList,
  fetchFollowerList,
  fetchFollowingList,
  fetchFeedList,
  postEmailauth
}
