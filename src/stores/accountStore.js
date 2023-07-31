import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAccountStore = defineStore('account', () => {
  const accessToken = ref('')
  const memberId = ref('')
  // 비밀번호 확인 여부 상태 변수
  const pwdChecked = ref(false)

  // 비밀번호 확인 여부 변경 함수
  function setPwdChecked(val) {
    pwdChecked.value = val
  }

  return {
    accessToken,
    memberId,
    pwdChecked,
    setPwdChecked
  }
})
