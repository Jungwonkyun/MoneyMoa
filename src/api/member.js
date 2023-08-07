import { apiInstance } from './index.js'
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
// 내 정보 API
async function getMyInfoApi(token) {
  try {
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const res = await api.get(`/member/myinfo`, { headers })
    return res
  } catch (err) {
    console.log(err)
  }
}
// test API
async function test(token) {
  try {
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const res = await api.get(`/feed/all`, { headers })
    return res
  } catch (err) {
    console.log(err)
  }
}

// 회원가입시 유저 이메일 인증
async function postEmailauth(email) {
  try {
    const res = await api.post(`/member/emailauth`, email)
    return res.data
  } catch (err) {
    console.log(err)
  }
}
// 비밀번호 찾기
async function postfindpassword(email) {
  try {
    const res = await api.post(`/member/findpassword`, email)
    console.log(res)

    return res.data
  } catch (err) {
    console.log(err)
  }
}

// 회원가입
async function postSignup(member) {
  try {
    const res = await api.post(`/member/signup`, member)
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

// 로그인
async function postLogin(loginInfo) {
  try {
    const res = await api.post('/member/login', loginInfo)
    console.log(res)
    return res
  } catch (err) {
    console.log(err)
  }
}
// 네이버 로그인
async function naverLogin() {
  try {
    const res = await api.get('/auth/naver')
    return res
  } catch (err) {
    console.log(err)
  }
}
// 카카오 로그인
// 카카오 로그인 창열고
async function openkakaoLogin() {
  const redirectUrl = import.meta.env.VITE_KAKAO_APP_API_URL
  window.location.replace(redirectUrl)
}

// 로그인 하기
async function postKakaoLogin(code) {
  try {
    const params = { authorizationCode: code }
    const res = await api.post('/auth/kakao', params)
    return res
  } catch (err) {
    throw err // 예외를 다시 던짐
  }
}
// 카카오 로그아웃
async function openkakaoLogout() {
  const redirectUrl = import.meta.env.VITE_KAKAO_LOGOUT_APP_API_URL
  window.location.replace(redirectUrl)
}

// 네이버 로그인 창
async function openNaverLogin() {
  const redirectUrl = import.meta.env.VITE_NAVER_APP_API_URL
  window.location.replace(redirectUrl)
}
// 네이버 로그인 하기
async function postNaverLogin(code, state) {
  try {
    const body = {
      authorizationCode: code,
      state: state
    }
    const res = await api.post('/auth/naver', body)
    console.log(res)
    return res
  } catch (err) {
    throw err // 예외를 다시 던짐
  }
}

// 유저 탈퇴
async function deletequitService(token) {
  try {
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const res = await api.delete(`/member/quitService`, { headers })
    return res.data
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
  postEmailauth,
  postSignup,
  naverLogin,
  postLogin,
  getMyInfoApi,
  deletequitService,
  test,
  postfindpassword,
  openkakaoLogin,
  postKakaoLogin,
  openkakaoLogout,
  openNaverLogin,
  postNaverLogin
}
