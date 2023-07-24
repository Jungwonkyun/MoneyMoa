import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useProductStore = defineStore('product', () => {
  const bank = ref('')
  const condition = ref({
    amount: 0,
    period: 6
  })
  function setCondition(amo, per) {
    condition.amount = amo
    condition.period = per
  }
  return { bank, condition, setCondition }
})
