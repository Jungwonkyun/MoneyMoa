import { defineStore } from 'pinia'
import { ref, onMounted, computed } from 'vue'
import { useCookies } from 'vue3-cookies'
import functions from '../api/member.js'
export const useAccountStore = defineStore('account', () => {
  const { cookies } = useCookies()
  const memberId = ref('')
  const isAdmin = ref(cookies.get('member') && cookies.get('member').role === 'ADMIN')

  // 비밀번호 확인 여부 상태 변수
  const pwdChecked = ref(false)

  // 비밀번호 확인 여부 변경 함수
  function setPwdChecked(val) {
    pwdChecked.value = val
  }

  // 로그인 정보, 로그인 여부 저장
  // 아마 로그인 여부는 라우터에도 저장해야될듯
  const isLogin = ref(!!cookies.get('accessToken'))
  const member = ref(null)

  // 로그인 함수
  function onLogin(data) {
    cookies.set('accessToken', data.token)
    cookies.set('refreshToken', data.refreshToken, '7D')
    cookies.set('member', data.member)
    isLogin.value = true
    isAdmin.value = data.member.role === 'ADMIN'
  }
  function onLogout() {
    cookies.remove('accessToken')
    cookies.remove('member')
    cookies.remove('refreshToken')
    isLogin.value = false
    isAdmin.value = false
  }
  // 토큰이 없고 리프레시 토큰이 있으면 토큰 재발급받기
  async function getNewToken() {
    if (!cookies.get('accessToken') && cookies.get('refreshToken')) {
      try {
        console.log(cookies.get('refreshToken'))
        const res = await functions.postGetAccessid()
        console.log(res)
        if (res.message === 'success') {
          cookies.set('accessToken', res.RefreshedAccessToken)
          // isLogin값도 갱신해주자
          isLogin.value = true
        }
      } catch (err) {
        // 갱신실패하면 로그아웃
        onLogout()
        console.log(err)
      }
    }
  }
  onMounted(() => {
    getNewToken()
  })
  return {
    memberId,
    pwdChecked,
    setPwdChecked,
    member,
    onLogin,
    isLogin,
    onLogout,
    getNewToken,
    isAdmin
  }
})
