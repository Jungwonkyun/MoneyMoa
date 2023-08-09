import { apiInstance } from './index.js'
import { useCookies } from 'vue3-cookies'

const api = apiInstance()
const { cookies } = useCookies()

// 유저 정보 API
async function callUserInfoApi(memberId) {
  const accessToken = cookies.get('accessToken')
  try {
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
    const res = await api.post(`/member/follow/${memberId}`, {
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

// 이미지 업로드 테스트
async function postUploadFile(img) {
  const formData = new FormData()
  formData.append('file', img[0])
  const headers = {
    'Content-Type': 'multipart/form-data'
  }
  try {
    console.log(img[0])

    const res = await api.post('/file/upload', formData, { headers })
    return res.data
  } catch (err) {
    console.log(err)
  }
}
// 이미지 다운로드 테스트
async function getImgDown(filename) {
  const encodedFilename = encodeURIComponent(filename)
  try {
    const res = await api.get(`/file/download/${encodedFilename}`)
    return res.data
  } catch (err) {
    console.log(err)
  }
}
// 회원정보변경
async function putUpdatedMember(token, member) {
  const Token = `Bearer ${token}`
  const headers = {
    Authorization: Token
  }
  try {
    const res = await api.put('/member/update', member, { headers })
    return res.data
  } catch (err) {
    console.log(err)
  }
}

export default {
  callUserInfoApi,
  addFollow,
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
  postNaverLogin,
  postUploadFile,
  getImgDown,
  putUpdatedMember
}
