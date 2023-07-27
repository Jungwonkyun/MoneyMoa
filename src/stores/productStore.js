import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useProductStore = defineStore('product', () => {
  const productType = ref('deposit')
  const selectedProduct = ref({})
  const amount = ref()
  const period = ref()
  function setProductType(type) {
    productType.value = type
  }
  function setAmount(amo) {
    amount.value = amo
  }
  function setPeriod(per) {
    period.value = per
  }
  function setProduct(prod) {
    selectedProduct.value = prod
  }
  return {
    productType,
    amount,
    period,
    selectedProduct,
    setProductType,
    setAmount,
    setPeriod,
    setProduct
  }
})
