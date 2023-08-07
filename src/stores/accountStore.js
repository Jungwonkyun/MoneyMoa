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
  cookies.get('member')
  // 로그인 함수
  function onLogin(data) {
    console.log(data)
    // 로그인 후 자동 새로고침 되게 해놨기 때문에 store에 멤버정보 저장하면 초기화됨, 일단 쿠키에 멤버정보 넣기
    cookies.set('member', data.member, '30MIN')
    cookies.set('accessToken', data.token, '30MIN')
  }
  function onLogout() {
    cookies.remove('accessToken')
    cookies.remove('member')
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
