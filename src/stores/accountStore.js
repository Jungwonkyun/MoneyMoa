import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAccountStore = defineStore('account', () => {
  const accessToken = ref('유저토큰')
  const memberId = ref('유저아이디')

  return {
    accessToken,
    memberId
  }
})
