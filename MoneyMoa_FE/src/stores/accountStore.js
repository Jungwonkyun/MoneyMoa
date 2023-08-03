import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAccountStore = defineStore('account', () => {
  const memberId = ref('')
  // 비밀번호 확인 여부 상태 변수
  const pwdChecked = ref(false)

  // 비밀번호 확인 여부 변경 함수
  function setPwdChecked(val) {
    pwdChecked.value = val
  }
  // 로그인 정보 저장
  const member = ref(null)
  // 토큰, 멤버정보 저장 함수
  function onLogin(data) {
    console.log(data)
    member.value = data.member
  }

  return {
    memberId,
    pwdChecked,
    setPwdChecked,
    member,
    onLogin
  }
})
