import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { useCookies } from 'vue3-cookies'

export const useAccountStore = defineStore('account', () => {
  const { cookies } = useCookies()
  const memberId = ref('')
  // 비밀번호 확인 여부 상태 변수
  const pwdChecked = ref(false)

  // 비밀번호 확인 여부 변경 함수
  function setPwdChecked(val) {
    pwdChecked.value = val
  }
  // 로그인 정보, 로그인 여부 저장
  // 아마 로그인 여부는 라우터에도 저장해야될듯
  const isLogin = computed(() => {
    return !!cookies.get('accessToken')
  })
  const member = ref(null)
  // 토큰, 멤버정보 저장 함수
  function onLogin(data) {
    console.log(data)
    member.value = data.member
    cookies.set('accessToken', data.token, '30MIN')
  }
  function onLogout() {
    cookies.remove('accessToken')
  }

  return {
    memberId,
    pwdChecked,
    setPwdChecked,
    member,
    onLogin,
    isLogin,
    onLogout
  }
})
