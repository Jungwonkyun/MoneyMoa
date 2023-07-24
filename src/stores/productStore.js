import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useProductStore = defineStore('product', () => {
  const bank = ref('')
  const amount = ref(0)
  const period = ref(6)
  function setBank(bankName) {
    bank.value = bankName
  }
})
