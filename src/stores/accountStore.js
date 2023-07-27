import { definestore } from 'pinia'
import { ref } from 'vue'

export const useAccountStore = definestore('account', () => {
  const accessToken = ref('')
  const memberId = ref('')

  return {
    accessToken,
    memberId
  }
})
