import { apiInstance } from './index.js'
import { instanceWithAuth } from './interceptorIndex.js'
import { useCookies } from 'vue3-cookies'

const api = apiInstance()
const apiWithAuth = instanceWithAuth()
const { cookies } = useCookies()

// 내 정보 API
async function getMyInfoApi() {
  try {
    const token = cookies.get('accessToken')
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const res = await apiWithAuth.get(`/member/myinfo`, { headers })
    return res
  } catch (err) {
    console.log(err)
  }
}

// 타인 정보 API
async function getSombodyInfoApi(memberId) {
  try {
    const token = cookies.get('accessToken')
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const res = await apiWithAuth.get(`/member/sombodyinfo/${memberId}`, { headers })
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
async function addFollowing(toMemberId) {
  try {
    // 토큰 받아오기
    const { cookies } = useCookies()
    const token = cookies.get('accessToken')
    const headers = {
      Authorization: `Bearer ${token}`
    }
    const res = await apiWithAuth.post(`/follow/following`, toMemberId, { headers })
    return res
  } catch (err) {
    console.log(err)
  }
}

// 팔로워 유저 목록 API
async function fetchFollowerList() {
  try {
    const res = await apiWithAuth.get(`/follow/myfollower`)
    return res
  } catch (err) {
    console.log(err)
  }
}

// 팔로워 유저 목록 API
async function fetchFollowingList() {
  try {
    const res = await apiWithAuth.get(`/follow/myfollowing`)
    return res
  } catch (err) {
    console.log(err)
  }
}

//언팔로우 API
// async function deleteFollow(toMemberId) {
//   try {
//     const res = await apiWithAuth.delete(`/follow/unfollowing`, )
//     return res
//   } catch (err) {
//     console.log(err)
//   }
// }

// 팔로잉 API
// 유저 토큰이랑 팔로우 할 사람의 id를 보내면 됨
async function deleteFollow(toMemberId) {
  try {
    console.log(toMemberId)
    const res = await apiWithAuth.delete(`/follow/unfollowing/${toMemberId}`)
    return res
  } catch (err) {
    console.log(err)
  }
}

// 마이 피드 목록 API
async function fetchFeedList(memberId) {
  try {
    const res = await api.get(`/member/feed/${memberId}`, {})
    return res
  } catch (err) {
    console.log(err)
  }
}
// 비밀번호 확인
async function postCheckPassword(pwd) {
  try {
    console.log(pwd)
    const res = await apiWithAuth.post('/member/checkpassword', pwd)
    console.log(res)
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

// 회원정보변경
async function postUpdatedMember(token, data) {
  const Token = `Bearer ${token}`
  for (const pair of data.entries()) {
    console.log(pair[0], pair[1])
  }
  const headers = {
    Authorization: Token,
    'Content-Type': 'multipart/form-data'
  }
  try {
    const res = await api.post('/member/update', data, { headers })
    return res.data
  } catch (err) {
    console.log(err)
  }
}
// 리프레시 토큰 이용한 토큰 재발급
async function postGetAccessid() {
  const data = {
    accessToken: cookies.get('accessTokenRef'),
    refreshToken: cookies.get('refreshToken')
  }
  try {
    const res = await api.post('/auth/getaccessid', data)
    return res.data
  } catch (err) {
    console.log(err)
  }
}

export default {
  addFollowing,
  fetchFollowerList,
  fetchFollowingList,
  fetchFeedList,
  deleteFollow,
  postEmailauth,
  postSignup,
  naverLogin,
  postLogin,
  getMyInfoApi,
  deletequitService,
  postfindpassword,
  openkakaoLogin,
  postKakaoLogin,
  openkakaoLogout,
  openNaverLogin,
  postNaverLogin,

  postUpdatedMember,
  postGetAccessid,
  postCheckPassword,
  getSombodyInfoApi
}
